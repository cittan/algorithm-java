package com.algorithm.array;

import java.util.*;

/**
 * Stdin
 */
public class Stdin {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String x = s.substring(1, s.length() - 1);
        String[] inner = x.split("\\],\\s*\\[");
        String[] part1 = inner[0].split(",");
        float[] f1 = new float[part1.length];
        for (int i = 0; i < part1.length; i++) {
            f1[i] = Float.parseFloat(part1[i]);
        }
        String[] part2 = inner[1].split(",");
        int[] ints = Arrays.stream(part2)
            .map(String::trim)
            .mapToInt(Integer::parseInt)
            .toArray();
        System.out.println(Arrays.toString(f1));
        System.out.println(Arrays.toString(ints));
        float u = sc.nextFloat();
        float h = Math.round(u * 1000) / 1000.0f;
        System.out.println(h);
        System.out.printf("%.3f%n",h);
        System.out.println(String.format("%.3f", h));
        sc.close();
    }
}
