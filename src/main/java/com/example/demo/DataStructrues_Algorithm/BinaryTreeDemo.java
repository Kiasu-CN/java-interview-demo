package com.example.demo.DataStructrues_Algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 手写二叉树 + 遍历 + 面试高频题
 * 面试高频：⭐⭐⭐ 前中后序遍历、层序遍历、最大深度、翻转二叉树、BST验证
 */
public class BinaryTreeDemo {

    // ======================== 内部类：树节点 ========================
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) { this.val = val; }

        /**
         * 从层序数组构建二叉树（null 表示空节点）
         * 例如：[1,2,3,4,5,null,6] 构建：
         *        1
         *       / \
         *      2   3
         *     / \   \
         *    4   5   6
         */
        static TreeNode of(Integer... vals) {
            if (vals == null || vals.length == 0 || vals[0] == null) return null;
            TreeNode root = new TreeNode(vals[0]);
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            int i = 1;
            while (i < vals.length) {
                TreeNode node = queue.poll();
                if (i < vals.length && vals[i] != null) {
                    node.left = new TreeNode(vals[i]);
                    queue.offer(node.left);
                }
                i++;
                if (i < vals.length && vals[i] != null) {
                    node.right = new TreeNode(vals[i]);
                    queue.offer(node.right);
                }
                i++;
            }
            return root;
        }
    }

    // ======================== 遍历算法 ========================

    /** 前序遍历（根-左-右）⭐⭐⭐ */
    static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private static void preorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);            // 根
        preorderHelper(node.left, result);  // 左
        preorderHelper(node.right, result); // 右
    }

    /** 中序遍历（左-根-右）⭐⭐⭐ */
    static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);  // 左
        result.add(node.val);              // 根
        inorderHelper(node.right, result); // 右
    }

    /** 后序遍历（左-右-根）⭐⭐⭐ */
    static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private static void postorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postorderHelper(node.left, result);  // 左
        postorderHelper(node.right, result); // 右
        result.add(node.val);                // 根
    }

    /** 层序遍历（BFS）⭐⭐⭐ */
    static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // ======================== 面试高频题 ========================

    /**
     * 最大深度（LeetCode 104）⭐⭐⭐
     * 思路：max(左子树深度, 右子树深度) + 1
     */
    static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /**
     * 翻转二叉树（LeetCode 226）⭐⭐⭐
     * 思路：递归交换每个节点的左右子树
     */
    static TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(temp);
        return root;
    }

    /**
     * 验证BST（LeetCode 98）⭐⭐
     * 思路：中序遍历必须严格递增，或递归时传递上下界
     */
    static boolean isValidBST(TreeNode root) {
        return isValidBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean isValidBSTHelper(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return isValidBSTHelper(node.left, min, node.val)
            && isValidBSTHelper(node.right, node.val, max);
    }

    /**
     * 最近公共祖先（LeetCode 236）⭐⭐
     * 思路：如果 p,q 分别在左右子树中，当前节点就是 LCA
     */
    static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root; // 分布在两侧
        return left != null ? left : right;
    }

    /**
     * 路径总和（LeetCode 112）⭐⭐
     * 思路：DFS，到叶子节点时判断剩余sum是否等于节点值
     */
    static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        if (root.left == null && root.right == null) return root.val == targetSum;
        return hasPathSum(root.left, targetSum - root.val)
            || hasPathSum(root.right, targetSum - root.val);
    }

    /**
     * 二叉树的右视图（LeetCode 199）⭐⭐
     * 思路：层序遍历每层最后一个节点
     */
    static List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (i == levelSize - 1) result.add(node.val); // 每层最后一个
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        /*
         * 构建示例树:
         *        1
         *       / \
         *      2   3
         *     / \   \
         *    4   5   6
         */
        TreeNode root = TreeNode.of(1, 2, 3, 4, 5, null, 6);

        System.out.println("===== 1. 二叉树遍历 =====");
        System.out.println("示例树: 1(2(4,5),3(null,6))");
        System.out.println("前序遍历(根-左-右): " + preorderTraversal(root));  // [1,2,4,5,3,6]
        System.out.println("中序遍历(左-根-右): " + inorderTraversal(root));   // [4,2,5,1,3,6]
        System.out.println("后序遍历(左-右-根): " + postorderTraversal(root)); // [4,5,2,6,3,1]
        System.out.println("层序遍历(BFS): " + levelOrder(root));             // [[1],[2,3],[4,5,6]]

        System.out.println("\n===== 2. 最大深度（LeetCode 104）⭐⭐⭐ =====");
        System.out.println("最大深度: " + maxDepth(root)); // 3

        System.out.println("\n===== 3. 翻转二叉树（LeetCode 226）⭐⭐⭐ =====");
        TreeNode root3 = TreeNode.of(1, 2, 3, 4, 5, null, 6);
        System.out.println("翻转前层序: " + levelOrder(root3));
        invertTree(root3);
        System.out.println("翻转后层序: " + levelOrder(root3)); // [[1],[3,2],[6,5,4]]

        System.out.println("\n===== 4. 验证BST（LeetCode 98）⭐⭐ =====");
        // 示例树不是 BST（节点3在右子树但小于根1的左子树节点2...实际上3>1所以右边ok，但左子树2的子节点需要都<2）
        TreeNode notBst = TreeNode.of(5, 1, 4, null, null, 3, 6);
        System.out.println("树 [5,1,4,null,null,3,6] 是否BST: " + isValidBST(notBst)); // false（3 < 5 但在右子树）
        TreeNode bst = TreeNode.of(5, 3, 7, 2, 4, 6, 8);
        System.out.println("树 [5,3,7,2,4,6,8] 是否BST: " + isValidBST(bst)); // true

        System.out.println("\n===== 5. 最近公共祖先（LeetCode 236）⭐⭐ =====");
        TreeNode root5 = TreeNode.of(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
        TreeNode p5 = root5.left;   // 节点5
        TreeNode q5 = root5.right;  // 节点1
        TreeNode lca = lowestCommonAncestor(root5, p5, q5);
        System.out.println("节点 " + p5.val + " 和 " + q5.val + " 的LCA: " + lca.val); // 3

        System.out.println("\n===== 6. 路径总和（LeetCode 112）⭐⭐ =====");
        TreeNode root6 = TreeNode.of(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1);
        System.out.println("路径和=22: " + hasPathSum(root6, 22));  // true (5->4->11->2=22，但这是到叶子，2是叶子)
        System.out.println("路径和=26: " + hasPathSum(root6, 26));  // true (5->8->13 不对，13不是叶子；5->8->4->1=18 不对)
        // 实际: 5->4->11->2 = 22 ✓; 5->8->4->1 = 18; 5->4->11->7 = 27
        System.out.println("路径和=27: " + hasPathSum(root6, 27));  // true (5->4->11->7)

        System.out.println("\n===== 7. 二叉树右视图（LeetCode 199）⭐⭐ =====");
        TreeNode root7 = TreeNode.of(1, 2, 3, null, 5, null, 4);
        System.out.println("右视图: " + rightSideView(root7)); // [1, 3, 4]
    }
}
