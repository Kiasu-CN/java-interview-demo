package com.example.demo.ListDemo;

import java.util.ArrayList;
import java.util.List;

public class ArrayListThreadUnsafeDemo {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 理论期望是 20000，但多次运行可能出现小于20000，甚至抛异常
        System.out.println("size = " + list.size());
    }
}
