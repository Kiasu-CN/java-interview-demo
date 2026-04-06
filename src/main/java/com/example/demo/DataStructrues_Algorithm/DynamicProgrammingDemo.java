package com.example.demo.DataStructrues_Algorithm;

import java.util.Arrays;

/**
 * 动态规划经典面试题
 * 面试高频：⭐⭐⭐ DP是面试必考算法，核心是状态定义和状态转移方程
 */
public class DynamicProgrammingDemo {

    /**
     * 1. 爬楼梯（LeetCode 70）⭐⭐⭐
     * 状态定义：dp[i] = 爬到第i阶的方法数
     * 状态转移：dp[i] = dp[i-1] + dp[i-2]
     * 边界：dp[0]=1, dp[1]=1
     * 时间 O(n)，空间 O(1)（可压缩）
     */
    static int climbStairs(int n) {
        if (n <= 1) return 1;
        int prev2 = 1, prev1 = 1; // dp[0], dp[1]
        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }

    static int climbStairsWithDP(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 2. 零钱兑换（LeetCode 322）⭐⭐⭐
     * 状态定义：dp[i] = 凑出金额i需要的最少硬币数
     * 状态转移：dp[i] = min(dp[i - coin] + 1) 对每个 coin <= i
     * 边界：dp[0]=0，其余初始化为无穷大
     * 时间 O(amount * coins.length)，空间 O(amount)
     */
    static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // 用 amount+1 代替 infinity
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    /**
     * 3. 最长公共子序列（LeetCode 1143）⭐⭐
     * 状态定义：dp[i][j] = text1前i个字符与text2前j个字符的LCS长度
     * 状态转移：
     *   若 text1[i-1]==text2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
     *   否则: dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     * 时间 O(m*n)
     */
    static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 4. 0-1背包问题 ⭐⭐
     * 状态定义：dp[i][w] = 前i个物品、容量w时的最大价值
     * 状态转移：
     *   不选第i个物品: dp[i][w] = dp[i-1][w]
     *   选第i个物品:   dp[i][w] = dp[i-1][w-weights[i-1]] + values[i-1]
     *   取两者较大值
     * 时间 O(n*W)
     */
    static int knapsack01(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w]; // 不选
                if (w >= weights[i - 1]) {
                    dp[i][w] = Math.max(dp[i][w],
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]);
                }
            }
        }
        return dp[n][capacity];
    }

    /**
     * 5. 最大子数组和（LeetCode 53）⭐⭐⭐
     * Kadane 算法
     * 状态定义：dp[i] = 以nums[i]结尾的最大子数组和
     * 状态转移：dp[i] = max(nums[i], dp[i-1] + nums[i])
     * 时间 O(n)，空间 O(1)
     */
    static int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }

    /**
     * 6. 不同路径（LeetCode 62）⭐⭐
     * 状态定义：dp[i][j] = 从(0,0)到(i,j)的路径数
     * 状态转移：dp[i][j] = dp[i-1][j] + dp[i][j-1]
     * 边界：第一行和第一列全为1
     * 时间 O(m*n)，空间 O(n)（可压缩为一维）
     */
    static int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // 第一行全为1
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] = dp[j] + dp[j - 1]; // dp[j]=上方，dp[j-1]=左方
            }
        }
        return dp[n - 1];
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. 爬楼梯（LeetCode 70）⭐⭐⭐ =====");
        int n = 5;
        System.out.println("n=" + n);
        System.out.println("DP数组: [1, 1, 2, 3, 5, 8]");
        System.out.println("方法数: " + climbStairs(n) + " (空间压缩)");
        System.out.println("方法数: " + climbStairsWithDP(n) + " (完整DP)");

        System.out.println("\n===== 2. 零钱兑换（LeetCode 322）⭐⭐⭐ =====");
        int[] coins = {1, 2, 5};
        int amount = 11;
        System.out.println("coins=" + Arrays.toString(coins) + ", amount=" + amount);
        System.out.println("最少硬币数: " + coinChange(coins, amount)); // 3 (5+5+1)
        System.out.println("amount=0: " + coinChange(coins, 0)); // 0
        System.out.println("coins=[2], amount=3: " + coinChange(new int[]{2}, 3)); // -1

        System.out.println("\n===== 3. 最长公共子序列（LeetCode 1143）⭐⭐ =====");
        System.out.println("\"abcde\" vs \"ace\" LCS长度: " + longestCommonSubsequence("abcde", "ace")); // 3
        System.out.println("\"abc\" vs \"abc\" LCS长度: " + longestCommonSubsequence("abc", "abc"));     // 3
        System.out.println("\"abc\" vs \"def\" LCS长度: " + longestCommonSubsequence("abc", "def"));     // 0

        System.out.println("\n===== 4. 0-1背包问题 ⭐⭐ =====");
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 6};
        int cap = 8;
        System.out.println("物品: " + Arrays.toString(weights) + " (重量)");
        System.out.println("价值: " + Arrays.toString(values));
        System.out.println("容量: " + cap);
        System.out.println("最大价值: " + knapsack01(weights, values, cap)); // 10

        System.out.println("\n===== 5. 最大子数组和（LeetCode 53）⭐⭐⭐ =====");
        int[] nums5 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("数组: " + Arrays.toString(nums5));
        System.out.println("最大和: " + maxSubArray(nums5)); // 6 (子数组[4,-1,2,1])

        System.out.println("\n===== 6. 不同路径（LeetCode 62）⭐⭐ =====");
        System.out.println("m=3, n=7 路径数: " + uniquePaths(3, 7)); // 28
        System.out.println("m=3, n=2 路径数: " + uniquePaths(3, 2)); // 3
    }
}
