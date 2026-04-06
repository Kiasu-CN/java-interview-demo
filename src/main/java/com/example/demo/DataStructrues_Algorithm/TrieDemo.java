package com.example.demo.DataStructrues_Algorithm;

/**
 * 手写前缀树（Trie）+ 面试高频题
 * 面试高频：⭐⭐ Trie树、前缀匹配、单词搜索
 */
public class TrieDemo {

    // ======================== 内部类：Trie 节点 ========================
    static class TrieNode {
        TrieNode[] children = new TrieNode[26]; // 26个小写字母
        boolean isEnd; // 是否是某个单词的结尾

        TrieNode() {
            isEnd = false;
        }
    }

    // ======================== 内部类：Trie ========================
    /**
     * 前缀树（Trie / 字典树）
     * 核心思想：利用字符串的公共前缀节省空间
     * 插入/查找时间复杂度：O(m)，m为字符串长度
     */
    static class Trie {
        private TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        /** 插入单词 */
        void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) {
                    node.children[idx] = new TrieNode();
                }
                node = node.children[idx];
            }
            node.isEnd = true;
        }

        /** 搜索单词（完全匹配） */
        boolean search(String word) {
            TrieNode node = searchNode(word);
            return node != null && node.isEnd;
        }

        /** 前缀匹配：是否有以 prefix 开头的单词 */
        boolean startsWith(String prefix) {
            return searchNode(prefix) != null;
        }

        /** 查找节点（搜索的公共部分） */
        private TrieNode searchNode(String str) {
            TrieNode node = root;
            for (char c : str.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) return null;
                node = node.children[idx];
            }
            return node;
        }
    }

    // ======================== 算法题 ========================

    /**
     * 单词搜索（LeetCode 79）⭐⭐
     * 思路：DFS + 回溯，从每个格子出发尝试四个方向
     * 时间 O(m*n*4^L)，L为单词长度
     */
    static boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;
        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (dfs(board, word, i, j, 0, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(char[][] board, String word, int row, int col,
                               int index, boolean[][] visited) {
        if (index == word.length()) return true; // 匹配完成
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return false;
        if (visited[row][col] || board[row][col] != word.charAt(index)) return false;

        visited[row][col] = true;
        // 四个方向搜索
        boolean found = dfs(board, word, row + 1, col, index + 1, visited)
                     || dfs(board, word, row - 1, col, index + 1, visited)
                     || dfs(board, word, row, col + 1, index + 1, visited)
                     || dfs(board, word, row, col - 1, index + 1, visited);
        visited[row][col] = false; // 回溯
        return found;
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. Trie 基本操作（LeetCode 208）⭐⭐ =====");
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");
        trie.insert("banana");
        System.out.println("插入: apple, app, application, banana");
        System.out.println("search(\"apple\"): " + trie.search("apple"));       // true
        System.out.println("search(\"app\"): " + trie.search("app"));           // true
        System.out.println("search(\"appl\"): " + trie.search("appl"));         // false（不是完整单词）
        System.out.println("startsWith(\"app\"): " + trie.startsWith("app"));   // true
        System.out.println("startsWith(\"ban\"): " + trie.startsWith("ban"));   // true
        System.out.println("startsWith(\"orange\"): " + trie.startsWith("orange")); // false

        System.out.println("\n===== 2. 单词搜索（LeetCode 79）⭐⭐ =====");
        char[][] board = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}
        };
        System.out.println("棋盘:");
        for (char[] row : board) {
            System.out.println("  " + java.util.Arrays.toString(row));
        }
        System.out.println("search(\"ABCCED\"): " + exist(board, "ABCCED")); // true
        System.out.println("search(\"SEE\"): " + exist(board, "SEE"));         // true
        System.out.println("search(\"ABCB\"): " + exist(board, "ABCB"));       // false
        System.out.println("search(\"ABFSAB\"): " + exist(board, "ABFSAB"));   // false（需要回溯，B用过后不能再用）
    }
}
