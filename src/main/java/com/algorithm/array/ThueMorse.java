package com.algorithm.array;

import java.util.Scanner;

/**
 * ThueMorse
 */
public class ThueMorse {


    public static char rounds(int i, int k) {
        int filps = 0;
        int size = 1 << i;
        while(size > 2){
            size = size / 2;
            if(k > size){
                k -= size;
                filps ++;
            }
        }
        char base = (k == 1) ? '0' : '1';
        if(filps % 2 == 0){
            return base;
        } else {
            return (base == '0') ? '1' : '0';
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 输入轮数 i 和位置 k，例如：4 5
        int i = sc.nextInt();
        int k = sc.nextInt();

        // 输出第 i 轮中第 k 个字符
        System.out.println(rounds(i, k));

        sc.close();
    }
}