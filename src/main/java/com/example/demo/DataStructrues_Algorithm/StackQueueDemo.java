package com.example.demo.DataStructrues_Algorithm;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 手写栈和队列 + 面试高频题
 * 面试高频：⭐⭐⭐ 有效括号、最小栈、用栈实现队列、循环队列
 */
public class StackQueueDemo {

    // ======================== 1. 手写数组栈 ========================
    /**
     * 基于数组的栈实现
     * 核心：push/pop/peek 均为 O(1)
     */
    static class MyStack {
        private int[] data;
        private int size;
        private static final int DEFAULT_CAPACITY = 10;

        MyStack() {
            data = new int[DEFAULT_CAPACITY];
            size = 0;
        }

        void push(int val) {
            if (size == data.length) {
                int[] newData = new int[data.length * 2];
                System.arraycopy(data, 0, newData, 0, size);
                data = newData;
            }
            data[size++] = val;
        }

        int pop() {
            if (isEmpty()) throw new RuntimeException("栈为空");
            return data[--size];
        }

        int peek() {
            if (isEmpty()) throw new RuntimeException("栈为空");
            return data[size - 1];
        }

        boolean isEmpty() {
            return size == 0;
        }

        int size() {
            return size;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                if (i > 0) sb.append(", ");
                sb.append(data[i]);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    // ======================== 2. 最小栈（LeetCode 155）========================
    /**
     * 最小栈：能在 O(1) 时间内获取栈中最小元素
     * 思路：辅助栈同步记录每个位置对应的最小值
     */
    static class MinStack {
        private Deque<Integer> mainStack;
        private Deque<Integer> minStack; // 辅助栈，栈顶始终是当前最小值

        MinStack() {
            mainStack = new ArrayDeque<>();
            minStack = new ArrayDeque<>();
        }

        void push(int val) {
            mainStack.push(val);
            // 辅助栈：如果为空或新值更小，压入新值；否则压入当前最小值
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            } else {
                minStack.push(minStack.peek());
            }
        }

        void pop() {
            mainStack.pop();
            minStack.pop();
        }

        int top() {
            return mainStack.peek();
        }

        int getMin() {
            return minStack.peek();
        }
    }

    // ======================== 3. 循环队列 ========================
    /**
     * 基于数组的循环队列
     * 核心：front/rear 指针 + 取模运算实现循环
     * 牺牲一个存储单元来区分"满"和"空"
     */
    static class MyCircularQueue {
        private int[] data;
        private int front; // 队头指针
        private int rear;  // 队尾指针（指向下一个插入位置）
        private int capacity;

        MyCircularQueue(int k) {
            capacity = k + 1; // 多留一个位置区分满/空
            data = new int[capacity];
            front = 0;
            rear = 0;
        }

        boolean enQueue(int value) {
            if (isFull()) return false;
            data[rear] = value;
            rear = (rear + 1) % capacity;
            return true;
        }

        boolean deQueue() {
            if (isEmpty()) return false;
            front = (front + 1) % capacity;
            return true;
        }

        int front() {
            if (isEmpty()) throw new RuntimeException("队列为空");
            return data[front];
        }

        int rear() {
            if (isEmpty()) throw new RuntimeException("队列为空");
            return data[(rear - 1 + capacity) % capacity];
        }

        boolean isEmpty() {
            return front == rear;
        }

        boolean isFull() {
            return (rear + 1) % capacity == front;
        }
    }

    // ======================== 4. 用栈实现队列（LeetCode 232）========================
    /**
     * 两个栈实现队列
     * 思路：输入栈 + 输出栈，倒栈时实现 FIFO
     * 均摊时间复杂度 O(1)
     */
    static class MyQueueViaStack {
        private Deque<Integer> inStack;   // 输入栈
        private Deque<Integer> outStack;  // 输出栈

        MyQueueViaStack() {
            inStack = new ArrayDeque<>();
            outStack = new ArrayDeque<>();
        }

        void push(int x) {
            inStack.push(x);
        }

        int pop() {
            if (outStack.isEmpty()) transfer();
            return outStack.pop();
        }

        int peek() {
            if (outStack.isEmpty()) transfer();
            return outStack.peek();
        }

        boolean empty() {
            return inStack.isEmpty() && outStack.isEmpty();
        }

        /** 将输入栈全部倒入输出栈（关键操作） */
        private void transfer() {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    // ======================== 算法题方法 ========================

    /**
     * 有效的括号（LeetCode 20）⭐⭐⭐
     * 思路：遇到左括号入栈，遇到右括号弹出栈顶匹配
     * 时间 O(n)，空间 O(n)
     */
    static boolean isValidParentheses(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == ']' && top != '[') return false;
                if (c == '}' && top != '{') return false;
            }
        }
        return stack.isEmpty();
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. 手写数组栈 MyStack =====");
        MyStack stack = new MyStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("push(1), push(2), push(3)");
        System.out.println("栈内容: " + stack);
        System.out.println("peek: " + stack.peek()); // 3
        System.out.println("pop: " + stack.pop());   // 3
        System.out.println("pop: " + stack.pop());   // 2
        System.out.println("栈中剩余: " + stack);     // [1]

        System.out.println("\n===== 2. 有效的括号（LeetCode 20）⭐⭐⭐ =====");
        System.out.println("\"({[]})\" -> " + isValidParentheses("({[]})"));     // true
        System.out.println("\"([)]\" -> " + isValidParentheses("([)]"));         // false
        System.out.println("\"((()))\" -> " + isValidParentheses("((()))"));     // true
        System.out.println("\"\" -> " + isValidParentheses(""));                 // true
        System.out.println("\"(\" -> " + isValidParentheses("("));               // false
        System.out.println("\")\" -> " + isValidParentheses(")"));               // false

        System.out.println("\n===== 3. 最小栈（LeetCode 155）⭐⭐ =====");
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println("push(-2), push(0), push(-3)");
        System.out.println("getMin: " + minStack.getMin()); // -3
        minStack.pop();
        System.out.println("pop() 后 top: " + minStack.top()); // 0
        System.out.println("getMin: " + minStack.getMin());     // -2

        System.out.println("\n===== 4. 循环队列 MyCircularQueue =====");
        MyCircularQueue cq = new MyCircularQueue(3);
        System.out.println("创建容量为3的循环队列");
        System.out.println("enQueue(1): " + cq.enQueue(1)); // true
        System.out.println("enQueue(2): " + cq.enQueue(2)); // true
        System.out.println("enQueue(3): " + cq.enQueue(3)); // true
        System.out.println("enQueue(4): " + cq.enQueue(4)); // false（队列已满）
        System.out.println("Front: " + cq.front() + ", Rear: " + cq.rear()); // 1, 3
        System.out.println("deQueue: " + cq.deQueue());     // true
        System.out.println("enQueue(4): " + cq.enQueue(4)); // true
        System.out.println("Rear: " + cq.rear());           // 4

        System.out.println("\n===== 5. 用栈实现队列（LeetCode 232）⭐⭐ =====");
        MyQueueViaStack queue = new MyQueueViaStack();
        queue.push(1);
        queue.push(2);
        queue.push(3);
        System.out.println("push(1), push(2), push(3)");
        System.out.println("peek: " + queue.peek()); // 1
        System.out.println("pop: " + queue.pop());   // 1
        System.out.println("pop: " + queue.pop());   // 2
        System.out.println("empty: " + queue.empty()); // false
    }
}
