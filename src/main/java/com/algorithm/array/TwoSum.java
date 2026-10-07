package com.algorithm.array;

import java.util.Scanner;

/**
 * LeetCode 1. 两数之和（ACM 输入输出模板）
 * 样例文件放在 testcases/TwoSum/ 目录，成对命名 1.in / 1.out
 * 输入约定：第一行为 n target，第二行为 n 个整数
 */
public class TwoSum {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 数组长度与目标和
        int n = sc.nextInt();
        int target = sc.nextInt();
        // 读取 n 个整数构成目标数组
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }

        int[] ans = twoSum(nums, target);
        System.out.println(ans[0] + " " + ans[1]);
        sc.close();
    }

    /**
     * 在数组中找出和为 target 的两个数的下标
     *
     * @param nums   目标数组
     * @param target 目标和
     * @return 两个数的下标；不存在时返回空数组
     */
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }
}
