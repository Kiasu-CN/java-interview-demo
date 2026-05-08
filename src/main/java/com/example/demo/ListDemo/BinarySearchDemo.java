package com.example.demo.ListDemo;

import java.util.*;

public class BinarySearchDemo {
    public static void main(String[] args) {
        // 1) 必须先排序
        List<Integer> nums = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        int idx1 = Collections.binarySearch(nums, 5); // 找到
        int idx2 = Collections.binarySearch(nums, 6); // 找不到

        System.out.println("5 的下标: " + idx1); // 2
        System.out.println("6 的返回值: " + idx2); // -4 => 插入点是 3

        // 插入点计算方式：insertionPoint = -(result + 1)
        int insertionPoint = -(idx2 + 1);
        System.out.println("6 应插入下标: " + insertionPoint); // 3

        // 2) 自定义比较器示例（按字符串长度排序后再查）
        List<String> words = new ArrayList<>(Arrays.asList("a", "bb", "ccc", "dddd"));
        Comparator<String> byLen = Comparator.comparingInt(String::length);

        int idx3 = Collections.binarySearch(words, "zz", byLen); // 长度2
        System.out.println("\"zz\"(长度2) 的下标: " + idx3); // 1
    }
}
