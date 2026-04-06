package com.example.demo.api;

import java.util.Arrays;

/**
 * Arrays 工具类常用 API 示例
 * 面试高频：排序算法、二分查找前提（必须有序）、asList陷阱
 */
public class ArraysApiDemo {
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};

        // ========== 1. toString — 数组转字符串打印 ==========
        System.out.println("原数组: " + Arrays.toString(arr));

        // ========== 2. sort — 排序（双轴快排，O(n log n)） ==========
        int[] arr1 = arr.clone();
        Arrays.sort(arr1);
        System.out.println("sort排序后: " + Arrays.toString(arr1));

        // 部分排序 [from, to)
        int[] arr2 = arr.clone();
        Arrays.sort(arr2, 0, 4); // 只排序前4个元素
        System.out.println("部分排序[0,4): " + Arrays.toString(arr2));

        // ========== 3. binarySearch — 二分查找（必须先排序！） ==========
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        int index = Arrays.binarySearch(sorted, 25);
        System.out.println("binarySearch(25)索引: " + index);
        // 查找不存在的元素，返回负数：-(插入点+1)
        int notFound = Arrays.binarySearch(sorted, 50);
        System.out.println("binarySearch(50)不存在: " + notFound); // 负数

        // ========== 4. copyOf / copyOfRange — 复制数组 ==========
        int[] copied = Arrays.copyOf(arr, 10); // 扩容到10，多出的补0
        System.out.println("copyOf扩容: " + Arrays.toString(copied));

        int[] range = Arrays.copyOfRange(arr, 1, 4); // 复制[1,4)
        System.out.println("copyOfRange[1,4): " + Arrays.toString(range));

        // ========== 5. fill — 填充 ==========
        int[] filled = new int[5];
        Arrays.fill(filled, 7);
        System.out.println("fill填充7: " + Arrays.toString(filled));

        // 部分填充 [from, to)
        Arrays.fill(filled, 1, 3, 0);
        System.out.println("部分fill[1,3)为0: " + Arrays.toString(filled));

        // ========== 6. equals — 比较数组内容 ==========
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        System.out.println("equals比较内容: " + Arrays.equals(a, b));           // true
        System.out.println("== 比较引用: " + (a == b));                         // false

        // ========== 7. asList — 数组转List（面试陷阱！） ==========
        System.out.println("\n--- asList 陷阱 ---");
        String[] strArr = {"a", "b", "c"};
        var list = Arrays.asList(strArr);
        System.out.println("asList结果: " + list);
        list.set(0, "x"); // 可以修改元素
        System.out.println("set修改后: " + list);

        try {
            list.add("d"); // 不支持！抛出 UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("asList不支持add: " + e.getClass().getSimpleName());
        }

        // 正确做法：用 new ArrayList 包一层
        var realList = new java.util.ArrayList<>(Arrays.asList(strArr));
        realList.add("d");
        System.out.println("new ArrayList后add成功: " + realList);

        // ========== 8. stream — 数组转流 ==========
        System.out.println("\n--- stream ---");
        int[] nums = {1, 2, 3, 4, 5};
        int sum = Arrays.stream(nums).sum();
        System.out.println("stream求和: " + sum);

        double avg = Arrays.stream(nums).average().orElse(0);
        System.out.println("stream平均值: " + avg);

        // ========== 9. 多维数组 ==========
        int[][] matrix = {{3, 1}, {2, 4}};
        System.out.println("二维数组deepToString: " + Arrays.deepToString(matrix));

        // ========== 10. parallelSort — 并行排序（大数据量更快） ==========
        int[] big = new int[100];
        for (int i = 0; i < 100; i++) big[i] = 100 - i;
        Arrays.parallelSort(big);
        System.out.println("parallelSort前5个: " + Arrays.toString(Arrays.copyOf(big, 5)));
    }
}
