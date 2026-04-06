package com.example.demo.api;

import java.util.*;
import java.util.stream.*;

/**
 * Stream 流式 API 示例（Java 8+）
 * 面试高频：中间操作 vs 终端操作、惰性求值、collect收集器
 */
public class StreamApiDemo {

    // 示例数据类
    static class Student {
        String name;
        int age;
        double score;
        String className;

        Student(String name, int age, double score, String className) {
            this.name = name;
            this.age = age;
            this.score = score;
            this.className = className;
        }

        @Override
        public String toString() {
            return name + "(年龄:" + age + ",成绩:" + score + ",班级:" + className + ")";
        }
    }

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
            new Student("张三", 20, 85.5, "A班"),
            new Student("李四", 22, 92.0, "B班"),
            new Student("王五", 21, 78.0, "A班"),
            new Student("赵六", 23, 95.5, "B班"),
            new Student("钱七", 20, 60.0, "C班"),
            new Student("孙八", 22, 88.0, "A班"),
            new Student("周九", 21, 45.0, "C班")
        );

        // ========== 1. filter — 过滤 ==========
        System.out.println("===== filter（成绩 >= 80） =====");
        students.stream()
                .filter(s -> s.score >= 80)
                .forEach(s -> System.out.println("  " + s));

        // ========== 2. map — 映射转换 ==========
        System.out.println("\n===== map（提取所有姓名） =====");
        List<String> names = students.stream()
                .map(s -> s.name)
                .collect(Collectors.toList());
        System.out.println("  " + names);

        // map 将成绩提高10%
        System.out.println("\n===== map（成绩提高10%） =====");
        List<Double> bonusScores = students.stream()
                .map(s -> s.score * 1.1)
                .collect(Collectors.toList());
        System.out.println("  " + bonusScores);

        // ========== 3. sorted — 排序 ==========
        System.out.println("\n===== sorted（按成绩降序） =====");
        students.stream()
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .forEach(s -> System.out.println("  " + s));

        // Comparator.comparing 链式排序
        System.out.println("\n===== sorted（先按班级，再按成绩降序） =====");
        students.stream()
                .sorted(Comparator.comparing((Student s) -> s.className)
                        .thenComparing((Student s) -> s.score, Comparator.reverseOrder()))
                .forEach(s -> System.out.println("  " + s));

        // ========== 4. distinct — 去重 ==========
        System.out.println("\n===== distinct（不重复的年龄） =====");
        List<Integer> ages = students.stream()
                .map(s -> s.age)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("  " + ages);

        // ========== 5. limit / skip — 截取 ==========
        System.out.println("\n===== limit（取前3名最高分） =====");
        students.stream()
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .limit(3)
                .forEach(s -> System.out.println("  " + s));

        System.out.println("\n===== skip（跳过前2个） =====");
        students.stream()
                .skip(2)
                .forEach(s -> System.out.println("  " + s));

        // ========== 6. reduce — 归约 ==========
        System.out.println("\n===== reduce（求总成绩） =====");
        double totalScore = students.stream()
                .map(s -> s.score)
                .reduce(0.0, Double::sum);
        System.out.println("  总成绩: " + totalScore);

        // ========== 7. count / min / max ==========
        System.out.println("\n===== count/min/max =====");
        long count = students.stream().filter(s -> s.score >= 60).count();
        System.out.println("  及格人数: " + count);

        Optional<Student> top = students.stream()
                .max(Comparator.comparing(s -> s.score));
        System.out.println("  最高分: " + top.orElse(null));

        // ========== 8. anyMatch / allMatch / noneMatch ==========
        System.out.println("\n===== 匹配判断 =====");
        boolean anyFailed = students.stream().anyMatch(s -> s.score < 60);
        System.out.println("  是否有人不及格(anyMatch): " + anyFailed);

        boolean allAbove40 = students.stream().allMatch(s -> s.score > 40);
        System.out.println("  是否全部>40分(allMatch): " + allAbove40);

        boolean none100 = students.stream().noneMatch(s -> s.score == 100);
        System.out.println("  是否没人满分(noneMatch): " + none100);

        // ========== 9. findFirst / findAny ==========
        System.out.println("\n===== 查找 =====");
        Optional<Student> firstA = students.stream()
                .filter(s -> s.className.equals("A班"))
                .findFirst();
        System.out.println("  A班第一个学生: " + firstA.orElse(null));

        // ========== 10. collect 收集器（面试重点） ==========
        System.out.println("\n===== collect 收集器 =====");

        // toList
        List<String> nameList = students.stream().map(s -> s.name).collect(Collectors.toList());
        System.out.println("  toList: " + nameList);

        // toSet
        Set<String> classSet = students.stream().map(s -> s.className).collect(Collectors.toSet());
        System.out.println("  toSet(班级): " + classSet);

        // toMap
        Map<String, Double> nameScoreMap = students.stream()
                .collect(Collectors.toMap(s -> s.name, s -> s.score));
        System.out.println("  toMap(姓名->成绩): " + nameScoreMap);

        // joining — 拼接字符串
        String joined = students.stream()
                .map(s -> s.name)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("  joining: " + joined);

        // groupingBy — 分组（面试高频）
        Map<String, List<Student>> byClass = students.stream()
                .collect(Collectors.groupingBy(s -> s.className));
        System.out.println("  groupingBy(按班级):");
        byClass.forEach((cls, stuList) -> {
            System.out.println("    " + cls + ": " + stuList.stream().map(s -> s.name).collect(Collectors.toList()));
        });

        // groupingBy + 计数
        Map<String, Long> countByClass = students.stream()
                .collect(Collectors.groupingBy(s -> s.className, Collectors.counting()));
        System.out.println("  groupingBy(计数): " + countByClass);

        // groupingBy + 平均分
        Map<String, Double> avgByClass = students.stream()
                .collect(Collectors.groupingBy(s -> s.className, Collectors.averagingDouble(s -> s.score)));
        System.out.println("  groupingBy(平均分): " + avgByClass);

        // partitioningBy — 分区（按条件分两组）
        Map<Boolean, List<Student>> partitioned = students.stream()
                .collect(Collectors.partitioningBy(s -> s.score >= 60));
        System.out.println("  partitioningBy(是否及格):");
        System.out.println("    及格: " + partitioned.get(true).stream().map(s -> s.name).collect(Collectors.toList()));
        System.out.println("    不及格: " + partitioned.get(false).stream().map(s -> s.name).collect(Collectors.toList()));

        // summarizingDouble — 统计汇总
        DoubleSummaryStatistics stats = students.stream()
                .collect(Collectors.summarizingDouble(s -> s.score));
        System.out.println("  summarizingDouble:");
        System.out.println("    人数=" + stats.getCount() + " 平均=" + stats.getAverage()
                + " 最高=" + stats.getMax() + " 最低=" + stats.getMin() + " 总分=" + stats.getSum());

        // ========== 11. flatMap — 扁平化 ==========
        System.out.println("\n===== flatMap（一对多展开） =====");
        List<String> words = Arrays.asList("Hello World", "Java Stream");
        List<String> flatWords = words.stream()
                .flatMap(w -> Arrays.stream(w.split(" ")))
                .collect(Collectors.toList());
        System.out.println("  flatMap拆词: " + flatWords);

        // ========== 12. IntStream 数值流 ==========
        System.out.println("\n===== IntStream 数值流 =====");
        int sum = IntStream.rangeClosed(1, 100).sum(); // 1+2+...+100
        System.out.println("  1到100求和: " + sum);

        // 统计字符出现次数
        String text = "aabbbcccc";
        Map<Character, Long> charCount = text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println("  字符统计: " + charCount);
    }
}
