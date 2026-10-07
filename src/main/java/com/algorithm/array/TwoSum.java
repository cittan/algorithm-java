package com.algorithm.array;

/**
 * LeetCode 1. 两数之和（示例，用于验证项目骨架）
 * 后续算法题可按 array / linkedlist / string 等包分类存放
 */
public class TwoSum {

    /**
     * 在数组中找出和为 target 的两个数的下标
     *
     * @param nums   目标数组
     * @param target 目标和
     * @return 两个数的下标；不存在时返回空数组
     */
    public int[] twoSum(int[] nums, int target) {
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
