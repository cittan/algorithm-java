package com.algorithm.array;

import java.util.*;

class Solution2 {

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for(String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            List<String> list = map.getOrDefault(key, new ArrayList<>());
            list.add(str);
            map.put(key,list);
        }
        return new ArrayList<>(map.values());
    }
    
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] strs = input.split(" ");
        Solution2 solution = new Solution2();
        List<List<String>> result = solution.groupAnagrams(strs);
        System.out.println(result);
        sc.close();
    }
}