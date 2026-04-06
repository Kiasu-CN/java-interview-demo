package com.example.demo.DataStructrues_Algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 双指针技巧 + 滑动窗口 + 面试高频题
 * 面试高频：⭐⭐⭐ 双指针和滑动窗口是最高频算法技巧之一
 */
public class TwoPointersSlidingWindowDemo {

    // ======================== 双指针 ========================

    /**
     * 两数之和 II（有序数组，LeetCode 167）⭐⭐⭐
     * 思路：左右指针，和小→左指针右移，和大→右指针左移
     * 时间 O(n)，空间 O(1)
     */
    static int[] twoSumSorted(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1}; // 题目要求1-indexed
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 删除排序数组中的重复项（LeetCode 26）⭐⭐
     * 思路：慢指针指向不重复的位置，快指针遍历
     * 返回去重后的长度，原地去重
     */
    static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int slow = 0; // slow 指向最后一个不重复元素
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }
        return slow + 1;
    }

    /**
     * 盛最多水的容器（LeetCode 11）⭐⭐⭐
     * 思路：左右指针从两端向中间，每次移动较短的边
     * 面积 = min(height[left], height[right]) * (right - left)
     * 时间 O(n)
     */
    static int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            // 移动较短的边（贪心：保留较高的边才可能获得更大面积）
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    /**
     * 移动零（LeetCode 283）⭐⭐
     * 思路：快慢指针，快指针找非零元素，换到慢指针位置
     * 保持非零元素的相对顺序
     */
    static void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                int temp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = temp;
                slow++;
            }
        }
    }

    // ======================== 滑动窗口 ========================

    /**
     * 无重复字符的最长子串（LeetCode 3）⭐⭐⭐
     * 思路：滑动窗口 + HashSet，遇到重复就收缩左边界
     * 时间 O(n)，空间 O(min(m,n))，m为字符集大小
     */
    static int lengthOfLongestSubstring(String s) {
        Set<Character> window = new HashSet<>();
        int left = 0, maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            // 如果 c 已在窗口中，收缩左边界直到移除 c
            while (window.contains(c)) {
                window.remove(s.charAt(left));
                left++;
            }
            window.add(c);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    /**
     * 大小为 K 的子数组最大和 ⭐⭐
     * 思路：固定大小滑动窗口
     * 维护窗口内的和，滑动时减去离开的、加上进入的
     * 时间 O(n)
     */
    static int maxSumSubarray(int[] nums, int k) {
        if (nums.length < k) return -1;
        // 计算第一个窗口的和
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }
        int maxSum = windowSum;
        // 滑动窗口
        for (int i = k; i < nums.length; i++) {
            windowSum = windowSum - nums[i - k] + nums[i]; // 减去离开的，加上进入的
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    /**
     * 长度最小的子数组（LeetCode 209）⭐⭐
     * 思路：可变大小滑动窗口，和 >= target 时收缩左边界
     * 时间 O(n)
     */
    static int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. 两数之和II（LeetCode 167）⭐⭐⭐ =====");
        int[] nums1 = {2, 7, 11, 15};
        System.out.println("数组: " + Arrays.toString(nums1) + ", target=9");
        System.out.println("结果索引: " + Arrays.toString(twoSumSorted(nums1, 9))); // [1,2]

        System.out.println("\n===== 2. 删除排序数组重复项（LeetCode 26）⭐⭐ =====");
        int[] nums2 = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println("原数组: " + Arrays.toString(nums2));
        int newLen = removeDuplicates(nums2);
        System.out.println("去重后长度: " + newLen + ", 数组: " + Arrays.toString(Arrays.copyOf(nums2, newLen)));

        System.out.println("\n===== 3. 盛最多水的容器（LeetCode 11）⭐⭐⭐ =====");
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("高度: " + Arrays.toString(heights));
        System.out.println("最大面积: " + maxArea(heights)); // 49

        System.out.println("\n===== 4. 移动零（LeetCode 283）⭐⭐ =====");
        int[] nums4 = {0, 1, 0, 3, 12};
        System.out.println("原数组: " + Arrays.toString(nums4));
        moveZeroes(nums4);
        System.out.println("移动后: " + Arrays.toString(nums4)); // [1,3,12,0,0]

        System.out.println("\n===== 5. 无重复字符的最长子串（LeetCode 3）⭐⭐⭐ =====");
        System.out.println("\"abcabcbb\" -> " + lengthOfLongestSubstring("abcabcbb")); // 3
        System.out.println("\"bbbbb\" -> " + lengthOfLongestSubstring("bbbbb"));       // 1
        System.out.println("\"pwwkew\" -> " + lengthOfLongestSubstring("pwwkew"));     // 3

        System.out.println("\n===== 6. 大小为K的子数组最大和 ⭐⭐ =====");
        int[] nums6 = {2, 1, 5, 1, 3, 2};
        System.out.println("数组: " + Arrays.toString(nums6) + ", k=3");
        System.out.println("最大和: " + maxSumSubarray(nums6, 3)); // 9 (5+1+3)

        System.out.println("\n===== 7. 长度最小的子数组（LeetCode 209）⭐⭐ =====");
        int[] nums7 = {2, 3, 1, 2, 4, 3};
        System.out.println("数组: " + Arrays.toString(nums7) + ", target=7");
        System.out.println("最小长度: " + minSubArrayLen(7, nums7)); // 2 ([4,3])
    }
}
