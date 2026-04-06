package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmDemo {
    public static void main(String[] args) {
        System.out.println("计算桃子总数量：");
        int peachNumber = calculatePeachTotal(1,9);
        System.out.println(peachNumber);   

        System.out.println("递归计算桃子总数量：");
        int peachNumber1 = calculatePeachTotal(1,9);
        System.out.println(peachNumber1);  
        
        
    }

    public static int calculatePeachTotal(int leave, int days) {
        int result = leave;
        for (int i = 1; i <= days; i++) {
            result = getPeachNum(result);
        }
        return result;
    }

    public static int getPeachNum (int num) {
        return 2 * (num + 1);
    }

    //递归
    public static int calculatePeachRecursive (int leave, int days) {

        int[] a = new int[0];
        Arrays.sort(a);

        if (days < 0) {
            System.out.println("天数有误");
            return -1;
        }

        days--;
        if(days == 0) return leave;

        return (calculatePeachRecursive(leave, days) + 1) * 2;
    }


}
