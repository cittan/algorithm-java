package com.algorithm.array;

import java.util.Arrays;
import java.util.Scanner;

/**
 * TestResource
 */
public class TestResource {


    public static int findMin(int[] resources, int[] taskLength) {
        int n = resources.length;
        int m = taskLength.length;
        if(m == 0){
            return 0;
        }
        long totalLength = 0;
        for(int len : taskLength){
            totalLength += len;
        }
        if(totalLength > n) {
            return 0;
        }
        int[] sorted = Arrays.stream(resources).distinct().sorted().toArray();
        int left = 0, right = sorted.length - 1;
        int res = sorted[right];
        while(left <= right){
            int mid = left + right >>> 1;
            if(canPlaceAll(resources, taskLength, sorted[mid])){
                res = sorted[mid];
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }
        return res;
    }

    public static boolean canPlaceAll(int[] resources, int[] taskLength, int maxVal) {
        int n = resources.length;
        int[] prefix = new int[n+1];
        for(int i = 0;i<n;i++){
            prefix[i+1] = prefix[i] + (resources[i] <= maxVal ? 1 : 0);
        }
        int pos = 0;
        for(int len : taskLength){
            int found = -1;
            for (int p = pos; p + len <= n; p++) { 
                if (prefix[p + len] - prefix[p] == len) { 
                    found = p;
                    break;
                }
            }
            if(found < 0){
                return false;
            }
            pos = found + len;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        int[] resources = new int[x];
        int[] taskLength = new int[y];
        for(int i = 0;i<x;i++){
            resources[i] = sc.nextInt();
        } 
        for(int j = 0;j<y;j++){
            taskLength[j] = sc.nextInt();
        }
        sc.close();
        int res = findMin(resources, taskLength);
        System.out.println(res);
    }
}
