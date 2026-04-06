package com.example.demo.api;

/**
 * String / StringBuilder 常用 API 示例
 * 面试高频：== vs equals、String不可变性、StringBuilder vs StringBuffer
 */
public class StringApiDemo {
    public static void main(String[] args) {
        // ========== 1. 创建与基本操作 ==========
        String s = "Hello, Java!";
        System.out.println("原字符串: " + s);
        System.out.println("长度 length(): " + s.length());
        System.out.println("第1个字符 charAt(0): " + s.charAt(0));
        System.out.println("是否为空 isEmpty(): " + s.isEmpty());

        // ========== 2. 比较（面试重点！） ==========
        String a = "abc";
        String b = "abc";       // 字符串常量池，a 和 b 指向同一个对象
        String c = new String("abc"); // 堆上新对象

        System.out.println("\n--- == vs equals ---");
        System.out.println("a == b: " + (a == b));         // true  → 同一个引用
        System.out.println("a == c: " + (a == c));         // false → 不同引用
        System.out.println("a.equals(c): " + a.equals(c)); // true  → 内容相同

        // compareTo 按字典序比较
        System.out.println("\"apple\".compareTo(\"banana\"): " + "apple".compareTo("applea")); // 负数

        // ========== 3. 查找 ==========
        String str = "Hello World, Hello Java";
        System.out.println("\n--- 查找 ---");
        System.out.println("indexOf(\"Hello\"): " + str.indexOf("Hello"));    // 0
        System.out.println("lastIndexOf(\"Hello\"): " + str.lastIndexOf("Hello")); // 12
        System.out.println("contains(\"World\"): " + str.contains("World"));  // true
        System.out.println("startsWith(\"Hello\"): " + str.startsWith("Hello")); // true
        System.out.println("endsWith(\"Java\"): " + str.endsWith("Java"));    // true

        // ========== 4. 截取与分割 ==========
        System.out.println("\n--- 截取与分割 ---");
        System.out.println("substring(6): " + str.substring(6));           // "World, Hello Java"
        System.out.println("substring(6, 11): " + str.substring(6, 11));   // "World"

        String csv = "张三,李四,王五";
        String[] names = csv.split(",");
        System.out.println("split 结果:");
        for (String name : names) {
            System.out.println("  " + name);
        }

        // ========== 5. 替换 ==========
        System.out.println("\n--- 替换 ---");
        System.out.println("replace(\"Hello\", \"Hi\"): " + str.replace("Hello", "Hi"));
        System.out.println("replaceAll正则替换数字: " + "abc123def".replaceAll("\\d+", "#")); // abc#def
        System.out.println("replaceFirst: " + "aabbcc".replaceFirst("b", "X")); // aaXbcc

        // ========== 6. 大小写与去空格 ==========
        System.out.println("\n--- 大小写 & 去空格 ---");
        String spaced = "  Hello World  ";
        System.out.println("trim(): '" + spaced.trim() + "'");
        System.out.println("toUpperCase(): " + "hello".toUpperCase());
        System.out.println("toLowerCase(): " + "HELLO".toLowerCase());

        // ========== 7. 转换 ==========
        System.out.println("\n--- 转换 ---");
        char[] chars = "Java".toCharArray();
        System.out.print("toCharArray(): ");
        for (char c1 : chars) System.out.print(c1 + " ");
        System.out.println();

        // String.valueOf 各种类型转字符串
        System.out.println("valueOf(123): " + String.valueOf(123));
        System.out.println("valueOf(true): " + String.valueOf(true));
        System.out.println("valueOf(3.14): " + String.valueOf(3.14));

        // ========== 8. 正则匹配 ==========
        System.out.println("\n--- 正则 ---");
        String email = "test@example.com";
        System.out.println("matches邮箱: " + email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"));
        System.out.println("matches手机号: " + "13812345678".matches("^1[3-9]\\d{9}$"));

        // ========== 9. String.format 格式化 ==========
        System.out.println("\n--- 格式化 ---");
        System.out.println(String.format("姓名: %s, 年龄: %d, 成绩: %.2f", "张三", 25, 95.5));
        System.out.println(String.format("补零: %04d", 42));  // 0042

        // ========== 10. StringBuilder（面试重点：可变字符串，线程不安全但快） ==========
        System.out.println("\n--- StringBuilder ---");
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World");        // 追加
        sb.insert(5, ",");          // 在索引5处插入
        sb.delete(5, 6);           // 删除索引[5,6)的字符      删除单个字符 sb.deleteCharAt(5);
        sb.reverse();              // 反转
        System.out.println("StringBuilder操作后: " + sb);

        // 性能对比：String拼接 vs StringBuilder
        // String 每次拼接都会创建新对象，StringBuilder 在同一个对象上操作
        long start = System.currentTimeMillis();
        String concat = "";
        for (int i = 0; i < 10000; i++) {
            concat += i; // 每次循环都创建新String对象
        }
        long time1 = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuilder sbFast = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sbFast.append(i); // 在同一个对象上追加
        }
        long time2 = System.currentTimeMillis() - start;
        System.out.println("String拼接耗时: " + time1 + "ms, StringBuilder耗时: " + time2 + "ms");
    }
}
