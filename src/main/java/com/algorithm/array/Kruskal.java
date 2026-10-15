package com.algorithm.array;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Kruskal
 */
public class Kruskal {

    static int[] parent;
    static int[] rank;

    public static int find(int x) {
        if (x != parent[x]) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public static boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) {
            return false;
        }
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootY] < rank[rootX]) {
            parent[rootY] = rootX;
        } else {
            parent[rootX] = rootY;
            rank[rootY]++;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            edges[i][0] = sc.nextInt();
            edges[i][1] = sc.nextInt();
            edges[i][2] = sc.nextInt();
        }
        sc.close();
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);
        parent = new int[n + 1];
        rank = new int[n + 1];
        for(int i = 1;i<=n;i++){
            parent[i] = i;
            rank[i] = 0;
        }
        int minCost = 0;
        int edgeCount = 0;
        for(int[] e: edges){
            int u = e[0], v = e[1], w = e[2];
            if(union(u,v)) {
                minCost += w;
                edgeCount ++;
            }
            if(edgeCount == n-1){
                break;
            }
        }
        System.out.println(edgeCount == n - 1 ? minCost : -1);
    }
}
