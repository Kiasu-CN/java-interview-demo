package com.example.demo.DataStructrues_Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回溯算法 + 贪心算法 + 面试高频题
 * 面试高频：⭐⭐⭐ 回溯是递归进阶，贪心是优化思维
 */
public class BacktrackingGreedyDemo {

    // ======================== 回溯算法 ========================

    /**
     * 1. 全排列（LeetCode 46）⭐⭐⭐
     * 思路：回溯 + used[] 数组标记已使用的元素
     * 时间 O(n*n!)，空间 O(n)
     */
    static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackPermute(nums, new ArrayList<>(), new boolean[nums.length], result);
        return result;
    }

    private static void backtrackPermute(int[] nums, List<Integer> path,
                                          boolean[] used, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path)); // 注意要拷贝
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue; // 跳过已使用的
            used[i] = true;
            path.add(nums[i]);
            backtrackPermute(nums, path, used, result);
            path.remove(path.size() - 1); // 回溯
            used[i] = false;
        }
    }

    /**
     * 2. 子集生成（LeetCode 78）⭐⭐
     * 思路：每个元素有"选"或"不选"两种选择
     * 时间 O(2^n)，空间 O(n)
     */
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackSubsets(int[] nums, int start, List<Integer> path,
                                           List<List<Integer>> result) {
        result.add(new ArrayList<>(path)); // 每个路径都是一个子集
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            backtrackSubsets(nums, i + 1, path, result); // 从 i+1 开始，避免重复
            path.remove(path.size() - 1); // 回溯
        }
    }

    /**
     * 3. N皇后（LeetCode 51）⭐⭐⭐
     * 思路：逐行放置皇后，检查列、主对角线、副对角线
     * 时间 O(n!)
     */
    static List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');
        backtrackNQueens(board, 0, result);
        return result;
    }

    private static void backtrackNQueens(char[][] board, int row, List<List<String>> result) {
        if (row == board.length) {
            result.add(constructBoard(board));
            return;
        }
        for (int col = 0; col < board.length; col++) {
            if (!isValidPlacement(board, row, col)) continue;
            board[row][col] = 'Q';
            backtrackNQueens(board, row + 1, result);
            board[row][col] = '.'; // 回溯
        }
    }

    /** 检查 (row, col) 是否可以放皇后 */
    private static boolean isValidPlacement(char[][] board, int row, int col) {
        // 检查同列
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') return false;
        }
        // 检查左上对角线
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') return false;
        }
        // 检查右上对角线
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 'Q') return false;
        }
        return true;
    }

    private static List<String> constructBoard(char[][] board) {
        List<String> list = new ArrayList<>();
        for (char[] row : board) list.add(new String(row));
        return list;
    }

    /**
     * 4. 电话号码字母组合（LeetCode 17）⭐⭐
     * 思路：数字到字母的映射 + 回溯组合
     */
    static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return result;
        Map<Character, String> phoneMap = new HashMap<>();
        phoneMap.put('2', "abc"); phoneMap.put('3', "def");
        phoneMap.put('4', "ghi"); phoneMap.put('5', "jkl");
        phoneMap.put('6', "mno"); phoneMap.put('7', "pqrs");
        phoneMap.put('8', "tuv"); phoneMap.put('9', "wxyz");
        backtrackLetters(digits, 0, new StringBuilder(), phoneMap, result);
        return result;
    }

    private static void backtrackLetters(String digits, int index, StringBuilder path,
                                           Map<Character, String> phoneMap, List<String> result) {
        if (index == digits.length()) {
            result.add(path.toString());
            return;
        }
        String letters = phoneMap.get(digits.charAt(index));
        for (char c : letters.toCharArray()) {
            path.append(c);
            backtrackLetters(digits, index + 1, path, phoneMap, result);
            path.deleteCharAt(path.length() - 1); // 回溯
        }
    }

    // ======================== 贪心算法 ========================

    /**
     * 5. 跳跃游戏（LeetCode 55）⭐⭐
     * 思路：维护当前能到达的最远位置 maxReach
     * 如果遍历到某位置超过了 maxReach，说明不可达
     * 时间 O(n)
     */
    static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false; // 当前位置不可达
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }

    /**
     * 6. 区间调度（无重叠区间，LeetCode 435）⭐⭐
     * 思路：按区间结束时间排序，贪心选择最早结束的区间
     * 选中的区间数最多 <=> 需要移除的区间数最少
     * 时间 O(n log n)
     */
    static int maxNonOverlappingIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        // 按结束时间排序
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int count = 1;
        int end = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= end) {
                count++;
                end = intervals[i][1];
            }
        }
        return count;
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. 全排列（LeetCode 46）⭐⭐⭐ =====");
        int[] nums1 = {1, 2, 3};
        System.out.println("输入: " + Arrays.toString(nums1));
        List<List<Integer>> perms = permute(nums1);
        System.out.println("全排列 (" + perms.size() + "种):");
        for (List<Integer> p : perms) System.out.println("  " + p);

        System.out.println("\n===== 2. 子集生成（LeetCode 78）⭐⭐ =====");
        int[] nums2 = {1, 2, 3};
        System.out.println("输入: " + Arrays.toString(nums2));
        List<List<Integer>> subs = subsets(nums2);
        System.out.println("所有子集 (" + subs.size() + "个):");
        for (List<Integer> s : subs) System.out.println("  " + s);

        System.out.println("\n===== 3. N皇后（LeetCode 51，n=4）⭐⭐⭐ =====");
        List<List<String>> queens = solveNQueens(4);
        for (int i = 0; i < queens.size(); i++) {
            System.out.println("解法" + (i + 1) + ":");
            for (String row : queens.get(i)) System.out.println("  " + row);
        }
        System.out.println("共 " + queens.size() + " 种解法"); // 2

        System.out.println("\n===== 4. 电话号码字母组合（LeetCode 17）⭐⭐ =====");
        System.out.println("输入: \"23\"");
        System.out.println("组合: " + letterCombinations("23"));
        System.out.println("输入: \"\"");
        System.out.println("组合: " + letterCombinations("")); // []

        System.out.println("\n===== 5. 跳跃游戏（LeetCode 55）⭐⭐ =====");
        int[] jump1 = {2, 3, 1, 1, 4};
        System.out.println("数组: " + Arrays.toString(jump1));
        System.out.println("能否到达末尾: " + canJump(jump1)); // true
        int[] jump2 = {3, 2, 1, 0, 4};
        System.out.println("数组: " + Arrays.toString(jump2));
        System.out.println("能否到达末尾: " + canJump(jump2)); // false

        System.out.println("\n===== 6. 区间调度（贪心）⭐⭐ =====");
        int[][] intervals = {{1, 3}, {2, 4}, {3, 5}, {0, 6}, {5, 7}, {8, 9}, {5, 9}};
        System.out.println("区间: ");
        for (int[] inv : intervals) System.out.print(Arrays.toString(inv) + " ");
        System.out.println();
        System.out.println("最多可选: " + maxNonOverlappingIntervals(intervals) + " 个区间"); // 4
    }
}
