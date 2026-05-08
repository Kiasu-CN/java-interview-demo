package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AlgorithmDemo {
    private int maxDiameter;

    public static void main(String[] args) {

        /**
         * 计算桃子数量
         */
        // System.out.println("计算桃子总数量：");
        // int peachNumber = calculatePeachTotal(1,9);
        // System.out.println(peachNumber);

        // System.out.println("递归计算桃子总数量：");
        // int peachNumber1 = calculatePeachTotal(1,9);
        // System.out.println(peachNumber1);

        /**
         * Leetcode 20. 有效的括号
         */
        String s = "([]})";
        System.out.println(isValid(s));

    }

    public static int calculatePeachTotal(int leave, int days) {
        int result = leave;
        for (int i = 1; i <= days; i++) {
            result = getPeachNum(result);
        }
        return result;
    }

    public static int getPeachNum(int num) {
        return 2 * (num + 1);
    }

    // 递归
    public static int calculatePeachRecursive(int leave, int days) {

        int[] a = new int[0];
        Arrays.sort(a);

        if (days < 0) {
            System.out.println("天数有误");
            return -1;
        }

        days--;
        if (days == 0)
            return leave;

        return (calculatePeachRecursive(leave, days) + 1) * 2;
    }

    // Leetcode 20. 有效的括号
    public static boolean isValid(String s) {
        Deque<Character> stack = new LinkedList<Character>();

        for (Character c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty())
                    return false;

                char top = stack.pop();
                if (c == ')' && top != '('
                        || c == ']' && top != '['
                        || c == '}' && top != '{')
                    return false;
            }
        }

        return stack.isEmpty();
    }

    /**
     * LC26 删除有序数组中的重复项
     */

    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;

        int slow = 0;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }

        return slow + 1;
    }

    /**
     * LinkedHashset 去重
     */
    public static int removeDuplicatesHashSet(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;

        Set<Integer> set = new LinkedHashSet<>();
        for (int x : nums) {
            set.add(x);
        }

        int i = 0;
        for (int x : set) {
            nums[i++] = x;
        }
        return i;
    }

    /**
     * Hashset 去重
     */
    public static int removeDuplicatesHashSetNoOrder(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;

        HashSet<Integer> set = new HashSet<>();
        for (int x : nums)
            set.add(x);

        int i = 0;
        for (int x : set) {
            nums[i++] = x;
        }

        return i;
    }

    /**
     * LC121 买卖股票的最佳时机
     * 
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int currProfit = 0;
        int maxProfit = 0;
        for (int i = 0; i < prices.length; i++) {
            minPrice = minPrice > prices[i] ? prices[i] : minPrice;
            currProfit = prices[i] - minPrice;
            maxProfit = maxProfit > currProfit ? maxProfit : currProfit;
        }

        return maxProfit;
    }

    /**
     * LC206 反转链表
     * 
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if (head == null)
            return null;
        Deque<Integer> stack = new LinkedList<Integer>();

        ListNode curr = head;
        while (curr != null) {
            stack.push(curr.val);
            curr = curr.next;
        }

        ListNode newNode = new ListNode(stack.pop());
        curr = newNode;
        while (!stack.isEmpty()) {
            ListNode nextNode = new ListNode(stack.pop());
            curr.next = nextNode;
            curr = nextNode;
        }

        return newNode;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * LC206 反转链表 解法2
     * 
     * @param head
     * @return
     */
    public ListNode reverseList2(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    /**
     * LC141 环形链表
     * 
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        if (head == null)
            return false;
        Set<ListNode> hashSet = new HashSet<ListNode>();
        ListNode curr = head;
        while (curr.next != null) {
            if (hashSet.contains(curr)) {
                return true;
            }
            hashSet.add(curr);
            curr = curr.next;
        }

        return false;
    }

    /**
     * LC141 环形链表 解法2
     * 
     * @param head
     * @return
     */
    public boolean hasCycle2(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast)
                return true;
        }
        return false;
    }

    /**
     * LC125 验证回文串
     * 
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        if (s.isEmpty())
            return true;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }

        int l = 0, r = sb.length() - 1;
        while (l < r) {
            if (sb.charAt(r) == sb.charAt(l)) {
                l++;
                r--;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * LC125 验证回文串 2
     * 
     * @param s
     * @return
     */
    public boolean isPalindrome2(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l)))
                l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r)))
                r--;

            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    /**
     * LC704 二分查找
     * 
     * @param nums
     * @param target
     * @return
     */
    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0, right = nums.length - 1;
        int mid;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 
     * LC53 最大子数组和
     * 
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
        // 如果前面累加为负，则要么最大值出现在前面，要么出现在不包含这部分的剩余数组中
        int sum = 0, max = nums[0];
        for (int i = 0; i < nums.length; i++) {
            sum = sum + nums[i];
            max = sum > max ? sum : max;
            if (sum < 0) {
                sum = 0;
            }
        }
        return max;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * LC102 二叉树的层序遍历 广度
     * 优先搜索
     * 
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> treeQueue = new LinkedList<>();
        treeQueue.offer(root);

        while (!treeQueue.isEmpty()) {
            int size = treeQueue.size();
            List<Integer> leavl = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode treeNode = treeQueue.poll();
                leavl.add(treeNode.val);
                if (treeNode.left != null) {
                    treeQueue.offer(treeNode.left);
                }
                if (treeNode.right != null) {
                    treeQueue.offer(treeNode.right);
                }
            }
            result.add(leavl);
        }
        return result;
    }

    /**
     * LC104 二叉树的最大深度
     * 
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        // List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> treeQueue = new LinkedList<>();
        treeQueue.offer(root);
        int leavlNum = 0;

        while (!treeQueue.isEmpty()) {
            int size = treeQueue.size();
            // List<Integer> leavl = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode treeNode = treeQueue.poll();
                // leavl.add(treeNode.val);
                if (treeNode.left != null) {
                    treeQueue.offer(treeNode.left);
                }
                if (treeNode.right != null) {
                    treeQueue.offer(treeNode.right);
                }
            }
            // result.add(leavl);
            leavlNum++;
        }

        return leavlNum;
    }

    /**
     * LC104 二叉树的最大深度 解法2 深度优先搜索
     * 
     * @param root
     * @return
     */
    public int maxDepth2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * LC226 翻转二叉树
     * 
     * @param root
     * @return
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;

        if (root.left != null)
            invertTree(root.left);
        if (root.right != null)
            invertTree(root.right);

        return root;
    }

    /**
     * LC98 验证二叉搜索树
     * 
     * @param root
     * @return
     */
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }

        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static boolean validate(TreeNode node, long min, long max) {
        if (node == null) {
            return true;
        }
        if (node.val <= min || node.val >= max) {
            return false;
        }
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }

    /**
     * LC112 路径总和 深度优先解法
     * 
     * @param root
     * @return
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }

        if (root.left == null && root.right == null) {
            return targetSum == root.val;
        }

        return hasPathSum(root.left, targetSum - root.val)
                || hasPathSum(root.right, targetSum - root.val);
    }

    /**
     * LC112 路径总和 广度优先解法
     * 
     * @param root
     * @return
     */
    public boolean hasPathSum2(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }

        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<Integer> sumQueue = new LinkedList<>();

        nodeQueue.offer(root);
        sumQueue.offer(root.val);

        while (!nodeQueue.isEmpty()) {
            TreeNode treeNode = nodeQueue.poll();
            int sum = sumQueue.poll();

            if (treeNode.left == null && treeNode.right == null && sum == targetSum) {
                return true;
            }

            if (treeNode.left != null) {
                nodeQueue.offer(treeNode.left);
                sumQueue.offer(sum + treeNode.left.val);
            }

            if (treeNode.right != null) {
                nodeQueue.offer(treeNode.right);
                sumQueue.offer(sum + treeNode.right.val);
            }
        }

        return false;
    }

    /**
     * LC543 二叉树的直径
     * 
     * @param root
     * @return
     */
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null)
            return 0;
        maxDiameter = 0;
        diameterofMaxDepth(root);
        return maxDiameter;
    }

    public int diameterofMaxDepth(TreeNode node) {
        if (node == null)
            return -1;

        int left = diameterofMaxDepth(node.left);
        int right = diameterofMaxDepth(node.right);

        maxDiameter = Math.max(maxDiameter, left + right + 2);
        return Math.max(left, right) + 1;
    }

    /**
     * LC46 全排列
     * 
     * @param nums
     * @return
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, path, result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }

            used[i] = true;
            path.add(nums[i]);

            backtrack(nums, used, path, result);

            path.remove(path.size() - 1);
            used[i] = false;
        }
    }

    /**
     * LC78 子集
     * 
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<Integer> path = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, path, result);
        return result;
    }

    private void backtrack(int[] nums, int startIndex, List<Integer> path, List<List<Integer>> result) {
        // 每一次path操作后都加入到result中
        result.add(new ArrayList<>(path));

        for (int i = startIndex; i < nums.length; i++) { 
            path.add(nums[i]);
            backtrack(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

}
