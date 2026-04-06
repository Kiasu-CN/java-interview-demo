package com.example.demo.api;

import java.util.*;

/**
 * 集合框架常用 API 示例：List / Set / Map / Queue + Collections 工具类
 * 面试高频：ArrayList vs LinkedList、HashMap底层结构、ConcurrentModificationException
 */
public class CollectionsApiDemo {
    public static void main(String[] args) {
        // ===================== List =====================
        System.out.println("===== List (ArrayList) =====");
        List<String> list = new ArrayList<>();

        // 增
        list.add("张三");
        list.add("李四");
        list.add("王五");
        list.add(1, "赵六"); // 在索引1处插入
        System.out.println("add后: " + list);

        // 查
        System.out.println("get(0): " + list.get(0));
        System.out.println("indexOf(\"李四\"): " + list.indexOf("李四"));
        System.out.println("contains(\"王五\"): " + list.contains("王五"));
        System.out.println("size(): " + list.size());

        // 改
        list.set(0, "张三丰");
        System.out.println("set(0,\"张三丰\"): " + list);

        // 删
        list.remove("赵六");    // 按元素删除
        list.remove(0);         // 按索引删除
        System.out.println("remove后: " + list);

        // 截取
        list.addAll(Arrays.asList("A", "B", "C"));
        System.out.println("subList(1,3): " + list.subList(1, 3));

        // 遍历方式
        System.out.println("--- 三种遍历方式 ---");
        // 1. for-each
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 2. Iterator
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();
        // 3. forEach + lambda
        list.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // 转数组
        String[] arr = list.toArray(new String[0]);
        System.out.println("toArray: " + Arrays.toString(arr));

        // --- 面试陷阱：遍历时删除 ---
        System.out.println("\n--- ConcurrentModificationException ---");
        List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        // 错误做法（会抛ConcurrentModificationException）：
        // for (int n : nums) { if (n == 3) nums.remove(Integer.valueOf(n)); }
        // 正确做法1：用 Iterator
        Iterator<Integer> iter = nums.iterator();
        while (iter.hasNext()) {
            if (iter.next() == 3) iter.remove(); // 安全删除
        }
        System.out.println("Iterator安全删除3后: " + nums);
        // 正确做法2：removeIf (Java 8+)
        nums.removeIf(n -> n > 3);
        System.out.println("removeIf(>3)后: " + nums);

        // --- LinkedList 特有：双端队列操作 ---
        System.out.println("\n===== LinkedList (双端队列) =====");
        LinkedList<String> linked = new LinkedList<>();
        linked.addFirst("B");   // 头部插入
        linked.addFirst("A");
        linked.addLast("C");    // 尾部插入
        System.out.println("addFirst/Last: " + linked);
        System.out.println("getFirst: " + linked.getFirst());
        System.out.println("getLast: " + linked.getLast());
        linked.push("X");       // 栈操作：压入头部
        System.out.println("push: " + linked);
        System.out.println("pop: " + linked.pop()); // 栈操作：弹出头部

        // ===================== Set =====================
        System.out.println("\n===== Set (HashSet) =====");
        Set<String> set = new HashSet<>();
        set.add("苹果");
        set.add("香蕉");
        set.add("苹果");        // 重复元素，不会添加
        System.out.println("HashSet(无序去重): " + set);
        System.out.println("contains: " + set.contains("苹果"));

        // TreeSet：有序（红黑树）
        Set<Integer> treeSet = new TreeSet<>(Arrays.asList(5, 3, 8, 1, 9));
        System.out.println("TreeSet(自然排序): " + treeSet);

        // 交集、并集、差集
        Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        Set<Integer> union = new HashSet<>(s1);
        union.addAll(s2);       // 并集
        System.out.println("并集: " + union);
        Set<Integer> inter = new HashSet<>(s1);
        inter.retainAll(s2);    // 交集
        System.out.println("交集: " + inter);
        Set<Integer> diff = new HashSet<>(s1);
        diff.removeAll(s2);     // 差集
        System.out.println("差集(s1-s2): " + diff);

        // ===================== Map =====================
        System.out.println("\n===== Map (HashMap) =====");
        Map<String, Integer> map = new HashMap<>();
        map.put("数学", 95);
        map.put("英语", 88);
        map.put("语文", 92);
        System.out.println("put后: " + map);

        // 查
        System.out.println("get(\"数学\"): " + map.get("数学"));
        System.out.println("getOrDefault: " + map.getOrDefault("物理", 0));
        System.out.println("containsKey: " + map.containsKey("英语"));
        System.out.println("containsValue: " + map.containsValue(95));

        // 改/删
        map.put("数学", 100);            // key存在则更新
        map.remove("英语");
        System.out.println("更新+删除后: " + map);

        // 遍历（推荐 entrySet）
        System.out.println("--- 遍历方式 ---");
        // 1. entrySet（推荐）
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
        // 2. forEach + lambda
        map.forEach((k, v) -> System.out.println("  " + k + " -> " + v));

        // 常用高级方法
        map.merge("语文", 5, Integer::sum);  // value = 旧值 + 5
        System.out.println("merge后语文: " + map.get("语文"));

        map.computeIfAbsent("化学", k -> 80); // key不存在时计算放入
        System.out.println("computeIfAbsent: " + map);

        // ===================== Queue =====================
        System.out.println("\n===== Queue (PriorityQueue) =====");
        Queue<Integer> queue = new PriorityQueue<>(); // 默认小顶堆
        queue.offer(3);
        queue.offer(1);
        queue.offer(2);
        System.out.println("PriorityQueue(自动排序): " + queue);
        System.out.println("peek(队头): " + queue.peek());
        System.out.println("poll(出队): " + queue.poll());
        System.out.println("poll后: " + queue);

        // Deque 双端队列
        Deque<String> deque = new ArrayDeque<>();
        deque.offerFirst("B");
        deque.offerFirst("A");
        deque.offerLast("C");
        System.out.println("Deque: " + deque);
        System.out.println("pollFirst: " + deque.pollFirst());
        System.out.println("pollLast: " + deque.pollLast());

        // ===================== Collections 工具类 =====================
        System.out.println("\n===== Collections 工具类 =====");
        List<Integer> col = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

        Collections.sort(col);
        System.out.println("sort: " + col);

        Collections.reverse(col);
        System.out.println("reverse: " + col);

        Collections.shuffle(col);
        System.out.println("shuffle: " + col);

        System.out.println("max: " + Collections.max(col));
        System.out.println("min: " + Collections.min(col));
        System.out.println("frequency(1): " + Collections.frequency(col, 1));

        Collections.replaceAll(col, 1, 99);
        System.out.println("replaceAll(1->99): " + col);

        // 不可变集合
        List<String> unmodifiable = Collections.unmodifiableList(Arrays.asList("A", "B"));
        System.out.println("unmodifiableList: " + unmodifiable);
        try {
            unmodifiable.add("C");
        } catch (UnsupportedOperationException e) {
            System.out.println("不可变集合不支持add");
        }

        // 线程安全集合（面试题：如何让ArrayList线程安全？）
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        System.out.println("synchronizedList: " + syncList.getClass().getSimpleName());
    }
}
