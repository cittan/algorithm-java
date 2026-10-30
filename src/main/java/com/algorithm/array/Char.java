package com.algorithm.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Char {

    static String s = "sedaweq31231";
    static char[] arr = s.toCharArray();

    public static void main(String[] args) {
        for (int i = 0; i < arr.length; i++) {
            Character.isDigit(arr[i]);
            Character.isLetter(arr[i]);
            Character.isLetterOrDigit(arr[i]);
            Character.isUpperCase(arr[i]);
            Character.isLowerCase(arr[i]);
            Character.isWhitespace(arr[i]);
        }
        StringBuilder sb = new StringBuilder("213414");
        sb.reverse();
        sb.toString();
        sb.insert(1, "312");
        sb.deleteCharAt(0);
        sb.delete(1, 3);
        sb.setLength(3);
        sb.substring(1,4);
        List<String> res = new ArrayList<>(List.of("q","w","e","a"));
        List<String> res1 = new ArrayList<>(Arrays.asList("a","b","c"));
        List<String> res2 = Arrays.asList("a", "b", "c");
        List<String> res3 = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));
        List<String> res4 = Stream.of("t", "c", "q").collect(Collectors.toList());
    }
}
