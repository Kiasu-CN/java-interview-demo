package com.example.demo.DataStructrues_Algorithm;

/**
 * 手写单链表实现 + 链表面试高频题
 * 面试高频：⭐⭐⭐ 反转链表、快慢指针、环检测、合并有序链表
 */
public class LinkedListDemo {

    // ======================== 内部类：链表节点 ========================
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        /** 从数组快速构建链表 */
        static ListNode of(int... vals) {
            ListNode dummy = new ListNode(0);
            ListNode cur = dummy;
            for (int v : vals) {
                cur.next = new ListNode(v);
                cur = cur.next;
            }
            return dummy.next;
        }

        /** 打印链表 */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            ListNode cur = this;
            int count = 0;
            while (cur != null && count < 50) { // 防止环链表死循环
                if (sb.length() > 0) sb.append(" -> ");
                sb.append(cur.val);
                cur = cur.next;
                count++;
            }
            sb.append(" -> null");
            return sb.toString();
        }
    }

    // ======================== 核心算法方法 ========================

    /**
     * 1. 链表反转（迭代法）⭐⭐⭐
     * 思路：三个指针 pre/cur/nxt，逐个反转指向
     * 时间 O(n)，空间 O(1)
     */
    static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next; // 暂存下一个
            curr.next = prev;          // 反转指向
            prev = curr;               // prev 前进
            curr = next;               // curr 前进
        }
        return prev; // prev 就是新头
    }

    /**
     * 2. 链表反转（递归法）⭐⭐
     * 思路：递归到末尾，从后往前反转
     * 时间 O(n)，空间 O(n)（递归栈）
     */
    static ListNode reverseListRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseListRecursive(head.next);
        head.next.next = head; // 后继节点指向自己
        head.next = null;      // 断开原来的指向
        return newHead;
    }

    /**
     * 3. 快慢指针找中间节点 ⭐⭐⭐
     * 思路：慢指针走1步，快指针走2步，快指针到尾时慢指针在中间
     * 偶数个节点时返回靠右的中间节点
     */
    static ListNode findMiddle(ListNode head) {
        if (head == null) return null;
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 4. 环检测（Floyd 龟兔赛跑）⭐⭐⭐
     * 思路：快慢指针，若有环必在环中相遇
     * 时间 O(n)，空间 O(1)
     */
    static boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    /**
     * 5. 环的入口节点（LeetCode 142）⭐⭐
     * 思路：相遇后，一个指针从head出发，一个从相遇点出发，再次相遇即为入口
     * 数学证明：a = (k-1)(b+c) + c，其中a为入口前距离，b+c为环长
     */
    static ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        boolean hasCycle = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }
        if (!hasCycle) return null;
        // 从 head 和相遇点同时出发
        ListNode p1 = head, p2 = slow;
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }

    /**
     * 6. 合并两个有序链表（LeetCode 21）⭐⭐⭐
     * 思路：虚拟头节点 + 双指针，谁小连谁
     * 时间 O(m+n)，空间 O(1)
     */
    static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }
        // 拼接剩余部分
        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    /**
     * 7. 删除倒数第N个节点（LeetCode 19）⭐⭐
     * 思路：虚拟头节点 + 双指针保持N的距离
     * fast 先走 N 步，然后 slow/fast 同步走，fast 到尾时 slow.next 就是要删的
     */
    static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++) { // fast 先走 n+1 步
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next; // 删除节点
        return dummy.next;
    }

    /**
     * 8. 判断回文链表（LeetCode 234）⭐⭐
     * 思路：找中点 → 反转后半段 → 双指针比较 → 恢复（可选）
     * 时间 O(n)，空间 O(1)
     */
    static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;
        // 找中点（慢指针走1步，快指针走2步）
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 反转后半段
        ListNode secondHalf = reverseList(slow.next);
        // 比较
        ListNode p1 = head, p2 = secondHalf;
        boolean result = true;
        while (p2 != null) {
            if (p1.val != p2.val) {
                result = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        return result;
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. 链表反转（迭代法，LeetCode 206）⭐⭐⭐ =====");
        ListNode list1 = ListNode.of(1, 2, 3, 4, 5);
        System.out.println("反转前: " + list1);
        System.out.println("反转后: " + reverseList(list1));

        System.out.println("\n===== 2. 链表反转（递归法）⭐⭐ =====");
        ListNode list2 = ListNode.of(1, 2, 3, 4, 5);
        System.out.println("反转前: " + list2);
        System.out.println("递归反转后: " + reverseListRecursive(list2));

        System.out.println("\n===== 3. 快慢指针找中间节点（LeetCode 876）⭐⭐⭐ =====");
        ListNode list3 = ListNode.of(1, 2, 3, 4, 5);
        System.out.println("链表: " + list3);
        System.out.println("中间节点: " + findMiddle(list3).val); // 3
        ListNode list3b = ListNode.of(1, 2, 3, 4, 5, 6);
        System.out.println("偶数链表: " + list3b);
        System.out.println("中间节点: " + findMiddle(list3b).val); // 4（靠右）

        System.out.println("\n===== 4. 环检测（LeetCode 141/142）⭐⭐⭐ =====");
        // 构造有环链表: 1 -> 2 -> 3 -> 4 -> 2（环回节点2）
        ListNode cycleHead = ListNode.of(1, 2, 3, 4);
        ListNode entry = cycleHead.next; // 节点 2
        ListNode tail = cycleHead;
        while (tail.next != null) tail = tail.next;
        tail.next = entry; // 4 -> 2 形成环

        System.out.println("有环链表: 1 -> 2 -> 3 -> 4 -> (环回2)");
        System.out.println("hasCycle: " + hasCycle(cycleHead)); // true
        ListNode cycleEntry = detectCycle(cycleHead);
        System.out.println("环入口节点值: " + (cycleEntry != null ? cycleEntry.val : "null")); // 2

        // 无环链表
        ListNode noCycle = ListNode.of(1, 2, 3);
        System.out.println("无环链表 hasCycle: " + hasCycle(noCycle)); // false

        System.out.println("\n===== 5. 合并两个有序链表（LeetCode 21）⭐⭐⭐ =====");
        ListNode la = ListNode.of(1, 3, 5);
        ListNode lb = ListNode.of(2, 4, 6);
        System.out.println("L1: " + la);
        System.out.println("L2: " + lb);
        ListNode merged = mergeTwoLists(ListNode.of(1, 3, 5), ListNode.of(2, 4, 6));
        System.out.println("合并: " + merged); // 1->2->3->4->5->6

        System.out.println("\n===== 6. 删除倒数第N个节点（LeetCode 19）⭐⭐ =====");
        ListNode list6 = ListNode.of(1, 2, 3, 4, 5);
        System.out.println("原链表: " + list6);
        System.out.println("删除倒数第2个: " + removeNthFromEnd(list6, 2)); // 1->2->3->5

        System.out.println("\n===== 7. 回文链表（LeetCode 234）⭐⭐ =====");
        System.out.println("1->2->3->2->1 是否回文: " + isPalindrome(ListNode.of(1, 2, 3, 2, 1))); // true
        System.out.println("1->2->3->4->5 是否回文: " + isPalindrome(ListNode.of(1, 2, 3, 4, 5))); // false
    }
}
