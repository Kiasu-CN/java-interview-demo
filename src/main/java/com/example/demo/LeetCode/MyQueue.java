package com.example.demo.LeetCode;

import java.util.Deque;
import java.util.LinkedList;

public class MyQueue {

    private final Deque<Integer> inStack;

    private final Deque<Integer> outStack;

    public MyQueue() {
        inStack = new LinkedList<>();
        outStack = new LinkedList<>();
    }

    public void push(int x) {
        inStack.push(x);
    }

    public int pop() {
        moveToOutIfNeeded();
        return outStack.pop();
    }

    public int peek() {
        moveToOutIfNeeded();
        return outStack.peek();
    }

    public boolean empty() {
        return outStack.isEmpty() && inStack.isEmpty();
    }

    public void moveToOutIfNeeded() {
        if(outStack.isEmpty()) {
            while(!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }
}

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */
