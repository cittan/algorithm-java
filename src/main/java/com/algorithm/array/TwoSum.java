import java.util.*;

public class TwoSum {

    public static int findMinMaxOccupancy(int[] resources, int[] taskLengths) {
        int n = resources.length, m = taskLengths.length;
        if (m == 0) return 0;

        // 快速可行性检查：总长度超过资源数必然无解
        long totalLen = 0;
        for (int l : taskLengths) totalLen += l;
        if (totalLen > n) throw new IllegalArgumentException("任务总长度超过资源数，无解");

        // 在排序去重后的资源值上二分（答案一定是某个 resources[i]）
        int[] sorted = Arrays.stream(resources).distinct().sorted().toArray();

        int lo = 0, hi = sorted.length - 1, ans = sorted[hi];
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (canPlaceAll(resources, taskLengths, sorted[mid])) {
                ans = sorted[mid];
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }

    /**
     * 验证：O(n + m log n)
     * 好位置 = 值 <= maxVal 的位置；前缀和 O(1) 判定窗口是否全好；
     * 任务按顺序推进 pos，天然不重叠，无需 occupied 数组。
     */
    private static boolean canPlaceAll(int[] resources, int[] taskLengths, int maxVal) {
        int n = resources.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++)
            prefix[i + 1] = prefix[i] + (resources[i] <= maxVal ? 1 : 0);

        int pos = 0;
        for (int len : taskLengths) {
            // 二分找 [pos, n-len] 中第一个使窗口全好的起点
            int lo = pos, hi = n - len, found = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (prefix[mid + len] - prefix[mid] == len) { found = mid; hi = mid - 1; }
                else lo = mid + 1;
            }
            if (found < 0) return false;
            pos = found + len;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] resources = {3, 1, 2, 5, 3, 1, 2};
        int[] taskLengths = {2, 3};
        System.out.println(findMinMaxOccupancy(resources, taskLengths)); // 3

        // 无解示例：显式抛异常，而不是静默返回一个误导值
        // findMinMaxOccupancy(new int[]{1, 2}, new int[]{2, 2});
    }
}