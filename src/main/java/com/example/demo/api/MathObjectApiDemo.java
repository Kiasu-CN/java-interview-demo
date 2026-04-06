package com.example.demo.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;

/**
 * Math / Object / 包装类 / 异常处理 / 文件操作 / Comparator 综合示例
 */
public class MathObjectApiDemo {
    public static void main(String[] args) {
        // ==================== Math ====================
        System.out.println("===== Math =====");
        System.out.println("max(10, 20): " + Math.max(10, 20));
        System.out.println("min(10, 20): " + Math.min(10, 20));
        System.out.println("abs(-5): " + Math.abs(-5));
        System.out.println("pow(2, 10): " + Math.pow(2, 10));       // 2的10次方
        System.out.println("sqrt(144): " + Math.sqrt(144));         // 平方根
        System.out.println("round(3.6): " + Math.round(3.6));      // 四舍五入
        System.out.println("floor(3.9): " + Math.floor(3.9));      // 向下取整: 3.0
        System.out.println("ceil(3.1): " + Math.ceil(3.1));        // 向上取整: 4.0
        System.out.println("random(): " + Math.random());           // [0.0, 1.0)

        // 生成 [min, max] 范围的随机整数
        int min = 1, max = 100;
        int randomNum = (int) (Math.random() * (max - min + 1)) + min;
        System.out.println("随机数[1,100]: " + randomNum);

        // ==================== 包装类 ====================
        System.out.println("\n===== 包装类 =====");

        // 字符串 → 基本类型
        int num = Integer.parseInt("123");
        System.out.println("parseInt: " + num);
        double d = Double.parseDouble("3.14");
        System.out.println("parseDouble: " + d);

        // 字符串 → 包装类
        Integer wrapped = Integer.valueOf("456");
        System.out.println("valueOf: " + wrapped);

        // 包装类 → 字符串
        System.out.println("toString: " + Integer.toString(789));

        // 常量
        System.out.println("MAX_VALUE: " + Integer.MAX_VALUE); // 2147483647
        System.out.println("MIN_VALUE: " + Integer.MIN_VALUE);

        // 自动装箱拆箱（面试题：Integer缓存池 -128~127）
        Integer a = 127;
        Integer b = 127;
        System.out.println("127 == 127: " + (a == b));   // true（缓存池内）
        Integer c = 128;
        Integer d2 = 128;
        System.out.println("128 == 128: " + (c == d2));  // false（超出缓存池）

        // Character 常用方法
        System.out.println("\n--- Character ---");
        char ch = 'A';
        System.out.println("isDigit('A'): " + Character.isDigit(ch));
        System.out.println("isLetter('A'): " + Character.isLetter(ch));
        System.out.println("isUpperCase('A'): " + Character.isUpperCase(ch));
        System.out.println("toLowerCase('A'): " + Character.toLowerCase(ch));
        System.out.println("isLetterOrDigit('A'): " + Character.isLetterOrDigit(ch));

        // ==================== Object 方法 ====================
        System.out.println("\n===== Object =====");

        // 演示：自定义类正确重写 toString / equals / hashCode
        class Point {
            int x, y;
            Point(int x, int y) { this.x = x; this.y = y; }

            @Override
            public String toString() { return "(" + x + "," + y + ")"; }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return x == point.x && y == point.y;
            }

            @Override
            public int hashCode() { return Objects.hash(x, y); }
        }

        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println("toString: " + p1);
        System.out.println("equals: " + p1.equals(p2));     // true
        System.out.println("hashCode p1: " + p1.hashCode() + ", p2: " + p2.hashCode());
        System.out.println("getClass: " + p1.getClass().getSimpleName());

        // Objects 工具类
        System.out.println("Objects.equals(null,null): " + Objects.equals(null, null)); // true
        System.out.println("Objects.isNull(null): " + Objects.isNull(null));           // true
        System.out.println("Objects.requireNonNull: 会抛NPE如果为null");

        // ==================== 异常处理 ====================
        System.out.println("\n===== 异常处理 =====");

        // try-catch-finally
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("捕获算术异常: " + e.getMessage());
        } finally {
            System.out.println("finally始终执行");
        }

        // 多重catch
        try {
            String s = null;
            s.length(); // NullPointerException
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("多重catch: " + e.getClass().getSimpleName());
        }

        // NumberFormatException
        try {
            Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("数字格式异常: " + e.getMessage());
        }

        // throw 主动抛出
        try {
            validateAge(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("throw抛出: " + e.getMessage());
        }

        // ==================== Comparator ====================
        System.out.println("\n===== Comparator =====");
        class Emp {
            String name; int salary;
            Emp(String name, int salary) { this.name = name; this.salary = salary; }
            @Override public String toString() { return name + ":" + salary; }
        }

        java.util.List<Emp> emps = new java.util.ArrayList<>(java.util.Arrays.asList(
            new Emp("张三", 8000),
            new Emp("李四", 12000),
            new Emp("王五", 8000)
        ));

        // 按薪资排序
        emps.sort(Comparator.comparing(e -> e.salary));
        System.out.println("按薪资升序: " + emps);

        // 按薪资降序
        emps.sort(Comparator.comparing((Emp e) -> e.salary).reversed());
        System.out.println("按薪资降序: " + emps);

        // 多级排序：先按薪资，再按姓名
        emps.sort(Comparator.comparing((Emp e) -> e.salary).thenComparing(e -> e.name));
        System.out.println("薪资+姓名排序: " + emps);

        // ==================== 文件操作 ====================
        System.out.println("\n===== 文件操作 =====");
        try {
            // 创建临时文件并写入
            Path tempFile = Files.createTempFile("demo", ".txt");
            Files.write(tempFile, "Hello Java NIO\n第二行内容".getBytes());

            // 判断存在
            System.out.println("文件是否存在: " + Files.exists(tempFile));

            // 读取所有行
            java.util.List<String> lines = Files.readAllLines(tempFile);
            System.out.println("readAllLines:");
            lines.forEach(line -> System.out.println("  " + line));

            // Stream按行读取
            System.out.println("Files.lines:");
            try (java.util.stream.Stream<String> stream = Files.lines(tempFile)) {
                stream.forEach(line -> System.out.println("  " + line));
            }

            // 删除临时文件
            Files.delete(tempFile);
            System.out.println("临时文件已删除");
        } catch (IOException e) {
            System.out.println("文件异常: " + e.getMessage());
        }
    }

    // throw 示例方法
    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数: " + age);
        }
    }
}
