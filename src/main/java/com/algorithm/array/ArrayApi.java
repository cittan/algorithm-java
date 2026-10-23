package com.algorithm.array;

import java.util.Arrays;
import java.util.Scanner;


/**
 * ArrayApi
 */
public class ArrayApi {

    static int[] a = {1,3,2};
    static int[] b = {1,3,2};
    public static void main(String[] args) {
        Arrays.sort(a);
        Arrays.fill(a,0);
        Arrays.toString(a);
        Arrays.equals(a,b);
        Arrays.copyOf(a,5); //[1,3,2,0,0]
        Arrays.binarySearch(a, 3);
        Arrays.stream(a).filter(x -> x > 2).toArray();
        Arrays.asList("a", "b");   
        Scanner sc = new Scanner(System.in);
        String[] x = sc.nextLine().trim().split("\\s+");
        Arrays.stream(x).mapToInt(Integer::parseInt).toArray();
    }
}