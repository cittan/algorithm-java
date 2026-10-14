package com.algorithm.array;

import java.util.Arrays;
import java.util.Scanner;


/**
 * MaxSubArray
 */
public class MaxSubArray {

    //输出最大子数组和，子数组的第一个索引，长度，子数组和一样用长度长的，长度一样用子数组第一个索引小的
    public static int[] maxSubArray(int[] nums) {
        if(nums.length == 0){
            return new int[3];
        }
        int[] dp = new int[nums.length];
        int[] len = new int[nums.length];
        dp[0] = nums[0];
        len[0] = 1;
        int res = nums[0];
        for(int i = 1;i<nums.length;i++){
            if(nums[i] > dp[i-1] + nums[i]) {
                dp[i] = nums[i];
                len[i] = 1;
            } else {
                dp[i] = dp[i-1] + nums[i];
                len[i] = len[i-1] + 1;
            }
            res = Math.max(res, dp[i]);
        }
        int max = Integer.MIN_VALUE;
        int start = 0;
        for(int i = 0;i<nums.length;i++){
            if(dp[i] == res) {
                if(len[i] > max) {
                    max = len[i];
                    start = i - len[i] + 1;
                }else if(len[i] == max) {
                    start = start < (i - len[i] + 1) ? start : (i - len[i] + 1);  
                }
            }
        }
        return new int[]{res, start, max};
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] parts = s.split(" ");
        int[] x = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
        int[] res = maxSubArray(x);
        System.out.println(Arrays.toString(res));
        sc.close();
    }
}