<#
.SYNOPSIS
    OJ 风格样例批量测试器（ACM 输入输出）
.DESCRIPTION
    扫描 testcases/<题目类名>/ 目录下的 *.in 与同名 *.out，
    通过标准输入喂给 java 单文件源码模式运行，并将实际输出与期望输出比对。
    使用 System.Diagnostics.Process 直接写入 stdin 字节流，
    规避 PowerShell 5.1 管道向原生命令注入 BOM / 额外换行的问题。
.EXAMPLE
    powershell -NoProfile -ExecutionPolicy Bypass -File tools/oj.ps1 TwoSum
    powershell -NoProfile -ExecutionPolicy Bypass -File tools/oj.ps1
    （不带参数时自动选择 src/main/java 下最近修改的 .java）
#>
param(
    # 题目类名（如 TwoSum）、文件名或完整路径；省略时取最近修改的 Java 文件
    [Parameter(Position = 0)]
    [string]$Problem
)

$ErrorActionPreference = 'Stop'

# 保证中文提示在控制台正确显示
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# 项目根目录（tools 的上一级）
$root = Split-Path -Parent $PSScriptRoot
$srcRoot = Join-Path $root 'src\main\java'

# 1. 定位题目源文件
if ([string]::IsNullOrWhiteSpace($Problem)) {
    $src = Get-ChildItem -Path $srcRoot -Recurse -Filter *.java -ErrorAction SilentlyContinue |
        Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($src) {
        Write-Host "未指定题目，自动选择最近修改的文件: $($src.FullName.Substring($root.Length + 1))" -ForegroundColor DarkYellow
    }
}
elseif (Test-Path -LiteralPath $Problem) {
    $src = Get-Item -LiteralPath $Problem
}
else {
    $matches = @(Get-ChildItem -Path $srcRoot -Recurse -Filter "$Problem.java" -ErrorAction SilentlyContinue)
    if ($matches.Count -gt 1) {
        Write-Host "找到多个同名文件，使用第一个: $($matches[0].FullName.Substring($root.Length + 1))" -ForegroundColor DarkYellow
    }
    $src = $matches | Select-Object -First 1
}

if (-not $src) {
    Write-Host "找不到题目源文件: $Problem" -ForegroundColor Red
    exit 2
}

$className = [System.IO.Path]::GetFileNameWithoutExtension($src.Name)
$caseDir = Join-Path $root "testcases\$className"

if (-not (Test-Path -Path $caseDir)) {
    Write-Host "样例目录不存在: testcases\$className\（请在其中放入 1.in / 1.out 等成对文件）" -ForegroundColor Red
    exit 2
}

# 2. 收集全部样例（按编号排序：1.in -> 2.in -> 10.in）
$cases = @(Get-ChildItem -Path $caseDir -Filter *.in |
    Sort-Object @{ Expression = { [int]([regex]::Replace($_.BaseName, '\D', '')) } }, Name)
if ($cases.Count -eq 0) {
    Write-Host "样例目录中没有 .in 文件: $caseDir" -ForegroundColor Red
    exit 2
}

# 运行单个 Java 源文件并喂入标准输入，返回退出码与 UTF-8 输出
function Invoke-JavaFile {
    param(
        [string]$WorkDir,   # 工作目录（源文件所在目录）
        [string]$JavaFile,  # 相对源文件名（如 TwoSum.java）
        [string]$InputText  # 标准输入文本
    )

    # 将输入落为 UTF-8 无 BOM 临时文件，通过 cmd 的 < 重定向送入 stdin，
    # 字节流与 OJ 评测机完全一致（绕开 .NET Framework Process 标准输入会注入 BOM 的问题）
    $inPath = [System.IO.Path]::GetTempFileName()
    try {
        [System.IO.File]::WriteAllBytes($inPath, [System.Text.Encoding]::UTF8.GetBytes($InputText))

        $psi = New-Object System.Diagnostics.ProcessStartInfo
        $psi.FileName = 'cmd.exe'
        $psi.Arguments = '/c java -Dfile.encoding=UTF-8 "' + $JavaFile + '" < "' + $inPath + '"'
        $psi.WorkingDirectory = $WorkDir
        $psi.RedirectStandardOutput = $true
        $psi.RedirectStandardError = $true
        $psi.UseShellExecute = $false

        $p = [System.Diagnostics.Process]::Start($psi)

        # 异步读取 stdout/stderr，避免输出撑满管道缓冲区导致死锁
        $outMs = New-Object System.IO.MemoryStream
        $errMs = New-Object System.IO.MemoryStream
        $outTask = $p.StandardOutput.BaseStream.CopyToAsync($outMs)
        $errTask = $p.StandardError.BaseStream.CopyToAsync($errMs)

        $p.WaitForExit()
        $outTask.Wait()
        $errTask.Wait()

        return [pscustomobject]@{
            ExitCode = $p.ExitCode
            StdOut   = [System.Text.Encoding]::UTF8.GetString($outMs.ToArray())
            StdErr   = [System.Text.Encoding]::UTF8.GetString($errMs.ToArray())
        }
    }
    finally {
        Remove-Item -LiteralPath $inPath -Force -ErrorAction SilentlyContinue
    }
}

# 输出归一化：统一换行、去掉首尾空白，避免换行符差异造成误判
function Normalize([string]$s) {
    if ($null -eq $s) { return '' }
    return ($s -replace "`r`n", "`n").Trim()
}

Write-Host "题目: $className    样例目录: testcases\$className\    样例数: $($cases.Count)" -ForegroundColor Cyan
Write-Host ('-' * 60)

$pass = 0
$fail = 0

foreach ($inFile in $cases) {
    $tag = [System.IO.Path]::GetFileNameWithoutExtension($inFile.Name)
    $outFile = [System.IO.Path]::ChangeExtension($inFile.FullName, '.out')
    $inputText = Get-Content -Path $inFile.FullName -Raw -Encoding UTF8

    $result = Invoke-JavaFile -WorkDir $src.DirectoryName -JavaFile $src.Name -InputText $inputText

    if ($result.ExitCode -ne 0) {
        $fail++
        Write-Host "[$tag] X 运行失败(退出码 $($result.ExitCode))" -ForegroundColor Red
        Write-Host ($result.StdErr + $result.StdOut) -ForegroundColor DarkRed
        continue
    }

    $actual = $result.StdOut

    # 只有 .in 没有 .out：作为调试运行，只打印实际输出，不计入判定
    if (-not (Test-Path -Path $outFile)) {
        Write-Host "[$tag] ? 缺少 $tag.out，仅运行，实际输出:" -ForegroundColor Yellow
        Write-Host $actual
        continue
    }

    $expected = Get-Content -Path $outFile -Raw -Encoding UTF8
    if ((Normalize $actual) -eq (Normalize $expected)) {
        $pass++
        Write-Host "[$tag] PASS" -ForegroundColor Green
    }
    else {
        $fail++
        Write-Host "[$tag] FAIL" -ForegroundColor Red
        Write-Host "--- 输入 ---" -ForegroundColor DarkGray
        Write-Host $inputText.Trim() -ForegroundColor DarkGray
        Write-Host "--- 期望输出 ---" -ForegroundColor Yellow
        Write-Host (Normalize $expected) -ForegroundColor Yellow
        Write-Host "--- 实际输出 ---" -ForegroundColor Red
        Write-Host (Normalize $actual) -ForegroundColor Red
    }
}

Write-Host ('-' * 60)
if ($fail -eq 0) {
    Write-Host "汇总: $pass/$($cases.Count) 组全部通过" -ForegroundColor Green
}
else {
    Write-Host "汇总: $pass 通过 / $fail 失败 / 共 $($cases.Count) 组" -ForegroundColor Red
    exit 1
}
