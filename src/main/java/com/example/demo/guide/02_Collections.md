# Java 集合框架面试完全指南

---

## 一、集合体系结构 ⭐（必须能默画）

```
                    Iterable（接口）
                        |
                    Collection（接口）
                   /      |       \
               List      Set      Queue
              / | \     / | \       |
          ArrayList  HashSet  PriorityQueue
          LinkedList TreeSet  ArrayDeque
          Vector    LinkedHashSet

    Map（接口，独立于 Collection）
      |
    HashMap → LinkedHashMap
    TreeMap
    Hashtable（过时）
    ConcurrentHashMap
```

**面试题：Collection 和 Collections 的区别？**
- `Collection`：集合的顶层接口
- `Collections`：工具类，提供 sort/reverse/shuffle 等静态方法

---

## 二、List ⭐

### 2.1 ArrayList

**底层原理：**
- 基于 `Object[]` 数组
- 默认初始容量 **10**（首次 add 时创建）
- 扩容为原来的 **1.5 倍**：`newCapacity = oldCapacity + (oldCapacity >> 1)`
- 随机访问 O(1)，中间插入/删除 O(n)（需要移动元素）
- 非线程安全

```java
// ArrayList 核心源码逻辑（面试能口述）
class ArrayList<E> {
    Object[] elementData;
    int size;

    boolean add(E e) {
        // 确保容量足够
        if (size == elementData.length)
            grow(); // Arrays.copyOf(elementData, newCapacity(1.5倍))
        elementData[size++] = e;
        return true;
    }

    E get(int index) {
        rangeCheck(index);
        return elementData[index]; // O(1)
    }

    E remove(int index) {
        E old = elementData[index];
        // 将 index+1 到末尾的元素全部左移一位
        System.arraycopy(elementData, index + 1, elementData, index, size - index - 1);
        elementData[--size] = null; // GC
        return old;
    }
}
```

**常用方法：**

```java
List<String> list = new ArrayList<>();

// 增
list.add("A");                    // 尾部添加
list.add(0, "B");                 // 指定位置插入
list.addAll(Arrays.asList("C","D")); // 批量添加

// 查
list.get(0);                      // 按索引取
list.indexOf("A");                // 查找索引，不存在返回 -1
list.lastIndexOf("A");
list.contains("A");               // 是否包含
list.size();                      // 元素个数
list.isEmpty();
list.subList(0, 2);               // 截取 [0,2)，返回视图（修改会影响原list）

// 改
list.set(0, "X");                 // 替换指定位置

// 删
list.remove(0);                   // 按索引删
list.remove("A");                 // 按元素删（只删第一个）
list.removeAll(otherList);        // 删除交集
list.removeIf(s -> s.startsWith("A")); // 条件删除
list.clear();                     // 清空

// 转换
String[] arr = list.toArray(new String[0]);
```

### 2.2 LinkedList

**底层原理：**
- 基于**双向链表**，每个节点有 prev 和 next 指针
- 随机访问 O(n)（从头遍历），头部插入/删除 O(1)
- 同时实现了 List 和 Deque 接口

```java
// 节点结构
class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;
}

// LinkedList 特有操作（双端队列）
LinkedList<String> list = new LinkedList<>();
list.addFirst("A");    // 头部添加 O(1)
list.addLast("B");     // 尾部添加 O(1)
list.getFirst();       // 获取头部
list.getLast();        // 获取尾部
list.removeFirst();    // 删除头部 O(1)
list.removeLast();     // 删除尾部 O(1)
list.push("X");        // 栈操作 = addFirst
list.pop();            // 栈操作 = removeFirst
```

### 2.3 ArrayList vs LinkedList（面试必问）

| 对比项 | ArrayList | LinkedList |
|--------|-----------|------------|
| 底层结构 | 动态数组 Object[] | 双向链表 |
| 随机访问 get(i) | **O(1)** 直接下标访问 | O(n) 需要遍历 |
| 头部插入 add(0, e) | O(n) 需移动元素 | **O(1)** 修改指针 |
| 尾部插入 add(e) | O(1)* 均摊 | O(1) |
| 中间插入 | O(n) | O(n)* 需先遍历到位置 |
| 内存占用 | 紧凑（可能浪费扩容空间） | 每节点额外存 prev/next |
| 缓存友好 | **是**（连续内存） | 否（链表节点分散） |
| 适用场景 | 读多写少 | 头尾频繁增删 |

> **面试加分点：** 实际开发中 99% 场景用 ArrayList，因为 CPU 缓存行的存在，连续内存的数组访问远快于链表。

---

## 三、Map ⭐⭐⭐（重中之重）

### 3.1 HashMap（面试最高频）

**核心参数：**

| 参数 | 默认值 | 说明 |
|------|--------|------|
| initialCapacity | 16 | 初始容量（必须是 2 的幂） |
| loadFactor | 0.75 | 负载因子 |
| treeifyThreshold | 8 | 链表转红黑树阈值 |
| untreeifyThreshold | 6 | 红黑树退化链表阈值 |
| minTreeifyCapacity | 64 | 转红黑树的最小数组长度 |

**底层数据结构（JDK 8+）：**

```
数组（桶/bucket）
  ┌───┐
0 │   │ → null
  ├───┤
1 │   │ → Node → Node → Node ... （链表，长度≥8且数组≥64时转红黑树）
  ├───┤
2 │   │ → null
  ├───┤
3 │   │ → TreeNode（红黑树节点）
  ├───┤
  ...
  └───┘
```

**put 流程（必须能完整口述）：**

```
1. 计算 hash = (h = key.hashCode()) ^ (h >>> 16)   ← 扰动函数，减少碰撞
2. 定位桶 index = (n - 1) & hash                    ← 等价于 hash % n，位运算更快
3. 桶为空 → 直接创建 Node 放入
4. 桶不为空：
   a. key 相同（== 或 equals）→ 覆盖 value
   b. 是 TreeNode → 红黑树插入
   c. 是链表 → 尾插法插入，插入后检查长度 ≥ 8 → 可能转红黑树
5. 检查 size > capacity * loadFactor → resize() 扩容为 2 倍
```

**面试题：为什么容量是 2 的幂次？**
```
因为 hash % n 等价于 hash & (n - 1)（当 n 是 2 的幂时）
位运算 & 比取模 % 快得多
例：n = 16, n-1 = 15 = 0b1111
    hash & 0b1111 → 只保留低4位 → 范围 [0, 15]
```

**面试题：HashMap 为什么线程不安全？**
```
1. JDK 7：并发扩容时头插法导致链表成环 → 死循环
2. JDK 8：改为尾插法解决了成环，但仍有数据覆盖问题
   - 线程A和线程B同时 put 到同一个桶，可能互相覆盖
   - size 计数不准确
```

**resize（扩容）过程：**
```
1. 新数组容量 = 旧容量 × 2
2. 遍历旧数组每个桶：
   - 链表节点：重新计算 hash & (newCap - 1)
     巧妙之处：节点新位置 = 原位置 或 原位置 + 旧容量（由高位bit决定）
   - 红黑树：拆分后如果节点 ≤ 6 → 退化为链表
```

**常用方法：**

```java
Map<String, Integer> map = new HashMap<>();

// 增/改
map.put("数学", 95);                          // 添加或覆盖
map.putIfAbsent("语文", 90);                  // 不存在才添加
map.putAll(otherMap);                          // 批量放入

// 查
map.get("数学");                               // 返回 value 或 null
map.getOrDefault("物理", 0);                   // 不存在返回默认值
map.containsKey("数学");                       // 是否包含 key
map.containsValue(95);                         // 是否包含 value
map.size();
map.isEmpty();

// 删
map.remove("数学");                            // 按 key 删除
map.remove("数学", 95);                        // key 和 value 都匹配才删
map.clear();

// 高级操作
map.merge("语文", 5, Integer::sum);            // value = 旧值+5，不存在则放入5
map.computeIfAbsent("化学", k -> 80);          // key 不存在时计算并放入
map.computeIfPresent("数学", (k, v) -> v + 5); // key 存在时重新计算

// 遍历（三种方式）
// 方式1：entrySet（推荐，一次拿到 key 和 value）
for (Map.Entry<String, Integer> e : map.entrySet()) {
    System.out.println(e.getKey() + " = " + e.getValue());
}
// 方式2：forEach + lambda
map.forEach((k, v) -> System.out.println(k + " -> " + v));
// 方式3：keySet（需要两次查找，不推荐）
for (String key : map.keySet()) {
    System.out.println(key + " = " + map.get(key));
}
```

### 3.2 LinkedHashMap

```java
// 继承 HashMap，内部维护了一个双向链表记录插入顺序（或访问顺序）
// 默认按插入顺序迭代
Map<String, Integer> lhm = new LinkedHashMap<>();
lhm.put("A", 1);
lhm.put("B", 2);
lhm.put("C", 3);
System.out.println(lhm); // {A=1, B=2, C=3} — 保持插入顺序

// 按访问顺序（可用来实现 LRU 缓存）
Map<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
accessOrder.put("A", 1);
accessOrder.put("B", 2);
accessOrder.get("A"); // 访问 A，A 移到末尾
System.out.println(accessOrder); // {B=2, A=1}

// 实现 LRU 缓存（经典面试题）
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int capacity;
    LRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity; // 超过容量时自动删除最久未访问的
    }
}
```

### 3.3 TreeMap

```java
// 底层红黑树，key 按自然顺序或指定 Comparator 排序
// 增删查 O(log n)
TreeMap<String, Integer> tm = new TreeMap<>();
tm.put("banana", 2);
tm.put("apple", 1);
tm.put("cherry", 3);
System.out.println(tm); // {apple=1, banana=2, cherry=3} — 按key排序

tm.firstKey();           // "apple"
tm.lastKey();            // "cherry"
tm.lowerKey("banana");   // "apple"（严格小于）
tm.higherKey("banana");  // "cherry"（严格大于）
tm.subMap("apple", "cherry"); // {apple=1, banana=2} — 范围视图
tm.headMap("banana");    // {apple=1} — 小于 banana
tm.tailMap("banana");    // {banana=2, cherry=3} — 大于等于
```

### 3.4 ConcurrentHashMap ⭐

```
JDK 7: Segment 分段锁
├── Segment[16]（每个 Segment 是一个 ReentrantLock）
│   └── HashEntry[]（每个 Segment 独立的 HashMap）
└── 最多支持 16 个线程并发写

JDK 8: synchronized + CAS
├── Node[] 数组（与 HashMap 结构相同）
├── 锁粒度：单个桶的头节点（比 JDK 7 更细）
├── put 流程：
│   1. 计算 hash 定位桶
│   2. 桶为空 → CAS 写入
│   3. 桶不为空 → synchronized(头节点) 加锁后操作
│   4. 也是 链表 → 红黑树
└── size()：baseCount + CounterCell[] 累加（类似 LongAdder）
```

### 3.5 HashMap vs Hashtable vs ConcurrentHashMap

| | HashMap | Hashtable | ConcurrentHashMap |
|--|---------|-----------|-------------------|
| 线程安全 | ❌ | ✅（全表锁） | ✅（桶级锁） |
| null key | ✅ 允许1个 | ❌ NPE | ❌ NPE |
| null value | ✅ 允许 | ❌ NPE | ❌ NPE |
| 性能 | 最好 | 最差 | 高并发好 |
| 推荐 | 单线程 | 不推荐（已过时） | 多线程首选 |

---

## 四、Set ⭐

### 4.1 HashSet

```java
// 底层就是 HashMap，value 统一存一个 PRESENT 常量
// add(e) → map.put(e, PRESENT)
// 所以 HashSet 的特性完全取决于 HashMap 的 key 特性

Set<String> set = new HashSet<>();
set.add("apple");
set.add("banana");
set.add("apple");  // 重复，不会添加
set.add(null);     // 允许一个 null
System.out.println(set); // [null, apple, banana]（无序）

set.contains("apple"); // true
set.remove("apple");
set.size();

// 交集、并集、差集
Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

Set<Integer> union = new HashSet<>(s1); union.addAll(s2);       // 并集 [1,2,3,4,5,6]
Set<Integer> inter = new HashSet<>(s1); inter.retainAll(s2);    // 交集 [3,4]
Set<Integer> diff = new HashSet<>(s1);  diff.removeAll(s2);     // 差集 [1,2]
```

### 4.2 TreeSet

```java
// 底层是 TreeMap（红黑树），元素自然排序或指定 Comparator
// 元素必须实现 Comparable 或构造时传入 Comparator
Set<Integer> treeSet = new TreeSet<>(Arrays.asList(5, 1, 3, 2, 4));
System.out.println(treeSet); // [1, 2, 3, 4, 5] — 自然排序

// 自定义排序
Set<String> byLength = new TreeSet<>(Comparator.comparing(String::length));
byLength.add("bb");
byLength.add("aaa");
byLength.add("c");
System.out.println(byLength); // [c, bb, aaa] — 按长度排序

// NavigableSet 操作
TreeSet<Integer> ns = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));
ns.lower(5);     // 3（严格小于）
ns.floor(5);     // 5（小于等于）
ns.higher(5);    // 7（严格大于）
ns.ceiling(5);   // 5（大于等于）
ns.first();      // 1
ns.last();       // 9
ns.subSet(3, 7); // [3, 5] — [3, 7)
ns.headSet(5);   // [1, 3] — 小于5
ns.tailSet(5);   // [5, 7, 9] — 大于等于5
```

### 4.3 HashSet vs TreeSet

| | HashSet | TreeSet |
|--|---------|---------|
| 底层 | HashMap | TreeMap（红黑树） |
| 顺序 | 无序 | 有序（自然/自定义排序） |
| null | 允许 | 不允许（排序时会NPE） |
| 性能 | add/remove/contains **O(1)** | O(log n) |
| 适用 | 去重、快速查找 | 有序去重、范围查询 |

---

## 五、Queue / Deque ⭐

```java
// Queue — 先进先出
Queue<String> queue = new LinkedList<>();
queue.offer("A");      // 入队（推荐，满时返回false）
queue.add("B");        // 入队（满时抛异常）
queue.peek();          // 查看队头（空返回null）
queue.element();       // 查看队头（空抛异常）
queue.poll();          // 出队（空返回null）
queue.remove();        // 出队（空抛异常）

// PriorityQueue — 优先队列（默认小顶堆）
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(3); pq.offer(1); pq.offer(2);
System.out.println(pq.peek()); // 1（最小值在队头）
while (!pq.isEmpty()) {
    System.out.print(pq.poll() + " "); // 1 2 3（升序出队）
}

// 大顶堆
Queue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
Queue<Integer> maxHeap2 = new PriorityQueue<>((a, b) -> b - a);

// Deque — 双端队列（推荐 ArrayDeque，比 LinkedList 快）
Deque<String> deque = new ArrayDeque<>();
deque.offerFirst("B");    // 头部入队
deque.offerLast("C");     // 尾部入队
deque.peekFirst();        // 查看头部
deque.peekLast();         // 查看尾部
deque.pollFirst();        // 头部出队
deque.pollLast();         // 尾部出队

// 用 Deque 实现栈（比 Stack 类快）
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);            // 入栈（= addFirst）
stack.push(2);
stack.pop();              // 出栈（= removeFirst），返回 2
stack.peek();             // 栈顶（= peekFirst），返回 1
```

---

## 六、Comparable vs Comparator ⭐

```java
// Comparable — 在类内部定义自然排序规则
class Student implements Comparable<Student> {
    String name;
    int score;
    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.score, o.score); // 按分数升序
    }
}

// Comparator — 在类外部定义排序规则（更灵活）
// 方式1：匿名类
Comparator<Student> byName = new Comparator<Student>() {
    @Override
    public int compare(Student a, Student b) {
        return a.name.compareTo(b.name);
    }
};

// 方式2：Lambda
Comparator<Student> byScore = (a, b) -> a.score - b.score;

// 方式3：Comparator.comparing（最推荐）
Comparator<Student> byScoreDesc = Comparator.comparingInt((Student s) -> s.score).reversed();
Comparator<Student> byNameThenScore = Comparator.comparing((Student s) -> s.name)
                                                 .thenComparingInt(s -> s.score);

// 使用
List<Student> students = new ArrayList<>(...);
students.sort(byScoreDesc);                              // List.sort
Collections.sort(students, byNameThenScore);             // Collections.sort
students.stream().sorted(byScoreDesc).collect(Collectors.toList()); // Stream.sort

// 处理 null
Comparator<String> nullsLast = Comparator.nullsLast(Comparator.naturalOrder());
Comparator<String> nullsFirst = Comparator.nullsFirst(String::compareTo);
```

---

## 七、迭代器与并发修改 ⭐

### 7.1 Iterator

```java
// Iterator 三个核心方法
Iterator<String> it = list.iterator();
while (it.hasNext()) {      // 是否有下一个
    String s = it.next();   // 获取下一个元素
}

// ListIterator（双向遍历，只适用于 List）
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) lit.next();
while (lit.hasPrevious()) lit.previous();
lit.add("X");   // 在当前位置插入
lit.set("Y");   // 替换最近一次 next/previous 返回的元素
```

### 7.2 ConcurrentModificationException（面试常考）

```java
// ❌ 错误：遍历时用集合的 remove/add → ConcurrentModificationException
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
for (Integer n : list) {           // for-each 底层是 Iterator
    if (n == 3) list.remove(n);    // 修改 modCount，Iterator 检测到不一致 → 抛异常
}

// ✅ 方式1：Iterator.remove()
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() == 3) it.remove();  // Iterator 自己的 remove，会同步 expectedModCount
}

// ✅ 方式2：removeIf（最简洁）
list.removeIf(n -> n == 3);

// ✅ 方式3：倒序遍历删除（避免索引偏移）
for (int i = list.size() - 1; i >= 0; i--) {
    if (list.get(i) == 3) list.remove(i);
}

// ✅ 方式4：CopyOnWriteArrayList（适用于读多写少）
List<String> safeList = new CopyOnWriteArrayList<>(Arrays.asList("a", "b", "c"));
for (String s : safeList) {
    safeList.add("d");  // 不会报错（遍历的是快照副本，新加的遍历不到）
}
```

---

## 八、Collections 工具类

```java
List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

// 排序
Collections.sort(list);                                    // 自然排序
Collections.sort(list, (a, b) -> b - a);                   // 降序
Collections.sort(list, Comparator.reverseOrder());         // 降序

// 查找
Collections.binarySearch(list, 4);  // 二分查找（必须先排序）
Collections.max(list);
Collections.min(list);
Collections.frequency(list, 1);     // 元素出现次数

// 打乱与反转
Collections.shuffle(list);          // 随机打乱
Collections.reverse(list);          // 反转
Collections.rotate(list, 2);        // 右旋2位（最后2个移到前面）
Collections.swap(list, 0, 1);       // 交换两个位置

// 填充与替换
Collections.fill(list, 0);          // 全部填0
Collections.replaceAll(list, 0, -1); // 0替换为-1

// 不可变集合（面试题：如何创建不可变List？）
List<String> unmodifiable = Collections.unmodifiableList(Arrays.asList("A", "B"));
// unmodifiable.add("C"); → UnsupportedOperationException

// Java 9+ 更简洁
List<String> of = List.of("A", "B", "C");       // 不可变
Set<String> setOf = Set.of("A", "B");
Map<String, Integer> mapOf = Map.of("A", 1, "B", 2);

// 线程安全集合
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<>());
// 注意：synchronizedList 只是给每个方法加了 synchronized，复合操作仍需手动同步
```

---

## 九、Stream API ⭐⭐（Java 8+ 面试高频）

### 9.1 创建 Stream

```java
// 从集合
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> s1 = list.stream();           // 顺序流
Stream<String> s2 = list.parallelStream();   // 并行流

// 从数组
Stream<Integer> s3 = Arrays.stream(new Integer[]{1, 2, 3});
IntStream s4 = Arrays.stream(new int[]{1, 2, 3});

// 静态方法
Stream<Integer> s5 = Stream.of(1, 2, 3);
Stream<Integer> s6 = Stream.iterate(0, n -> n + 1);   // 0, 1, 2, ...
Stream<Double> s7 = Stream.generate(Math::random);     // 无限流

// 基本类型流
IntStream.range(1, 5);      // 1, 2, 3, 4
IntStream.rangeClosed(1, 5); // 1, 2, 3, 4, 5
```

### 9.2 中间操作（惰性，返回新 Stream）

```java
// filter — 过滤
stream.filter(s -> s.length() > 3)

// map — 映射
stream.map(String::toUpperCase)
stream.mapToInt(String::length)       // → IntStream
stream.mapToDouble(s -> s.getPrice()) // → DoubleStream

// flatMap — 一对多展开
Stream<List<Integer>> s = Stream.of(Arrays.asList(1,2), Arrays.asList(3,4));
s.flatMap(Collection::stream)         // [1, 2, 3, 4]

// sorted — 排序
stream.sorted()                                        // 自然排序
stream.sorted(Comparator.comparing(Student::getScore)) // 按字段排序

// distinct — 去重（依赖 equals）
stream.distinct()

// limit / skip — 截取
stream.limit(5)     // 取前5个
stream.skip(3)      // 跳过前3个

// peek — 调试（查看元素但不修改）
stream.peek(System.out::println)
```

### 9.3 终端操作（触发执行，返回结果）

```java
// forEach — 遍历
stream.forEach(System.out::println);

// collect — 收集（最重要）
stream.collect(Collectors.toList());                    // → List
stream.collect(Collectors.toSet());                     // → Set
stream.collect(Collectors.toMap(s -> s.name, s -> s));  // → Map
stream.collect(Collectors.toCollection(LinkedList::new));// → 指定集合类型

// joining — 拼接
stream.map(Student::getName).collect(Collectors.joining(", ", "[", "]"));

// groupingBy — 分组（面试高频）
stream.collect(Collectors.groupingBy(Student::getClassName));
// → Map<String, List<Student>>

// groupingBy + 下游收集器
stream.collect(Collectors.groupingBy(Student::getClassName,
    Collectors.counting()));                             // 按班级计数
stream.collect(Collectors.groupingBy(Student::getClassName,
    Collectors.averagingDouble(Student::getScore)));     // 按班级平均分
stream.collect(Collectors.groupingBy(Student::getClassName,
    Collectors.mapping(Student::getName, Collectors.toList()))); // 按班级提取姓名

// partitioningBy — 分区（按条件分两组）
stream.collect(Collectors.partitioningBy(s -> s.score >= 60));
// → Map<Boolean, List<Student>>

// summarizing — 统计汇总
DoubleSummaryStatistics stats = stream.collect(
    Collectors.summarizingDouble(Student::getScore));
stats.getCount(); stats.getAverage(); stats.getMax(); stats.getMin(); stats.getSum();

// reducing — 归约
stream.collect(Collectors.reducing(0, Student::getScore, Integer::sum));

// count / min / max
long count = stream.filter(s -> s.score >= 60).count();
Optional<Student> top = stream.max(Comparator.comparing(Student::getScore));

// reduce — 归约
int sum = IntStream.of(1, 2, 3, 4, 5).reduce(0, Integer::sum);
Optional<Integer> max = Stream.of(3, 1, 4, 1, 5).reduce(Integer::max);

// findFirst / findAny
Optional<String> first = stream.filter(s -> s.length() > 3).findFirst();
Optional<String> any = stream.parallel().filter(s -> s.length() > 3).findAny();

// anyMatch / allMatch / noneMatch
boolean hasFail = stream.anyMatch(s -> s.score < 60);
boolean allPass = stream.allMatch(s -> s.score >= 60);
boolean none100 = stream.noneMatch(s -> s.score == 100);

// toArray
String[] arr = stream.toArray(String[]::new);
```

### 9.4 数值流

```java
// IntStream / LongStream / DoubleStream
IntStream.rangeClosed(1, 100).sum();              // 5050
IntStream.rangeClosed(1, 100).average();          // 50.5
IntStream.of(1, 2, 3, 4, 5).summaryStatistics(); // 统计摘要

// boxed — 基本类型流转对象流
Stream<Integer> boxed = IntStream.of(1, 2, 3).boxed();

// mapToObj — 转为对象流
IntStream.rangeClosed(1, 5).mapToObj(i -> "No." + i);
```

---

## 十、综合练习题

### 简单（5分钟内完成）

**1. 翻转 ArrayList**

```java
public static <T> void reverseList(List<T> list) {
    // 请实现：不使用 Collections.reverse
}
```

**2. 统计单词频率**

```java
// 输入: "hello world hello java world"
// 输出: {hello=2, world=2, java=1}
public static Map<String, Integer> wordCount(String text) {
    // 请实现
}
```

**3. 合并两个有序列表**

```java
public static List<Integer> mergeSorted(List<Integer> a, List<Integer> b) {
    // 请实现：类似归并排序的合并步骤
}
```

### 中等（面试高频）

**4. 两数之和（LeetCode #1）**

```java
// 给定数组和目标值，返回两个数的下标（和为目标值）
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return new int[0];
}
```

**5. 有效的括号（LeetCode #20）**

```java
public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
        if (c == '(') stack.push(')');
        else if (c == '[') stack.push(']');
        else if (c == '{') stack.push('}');
        else if (stack.isEmpty() || stack.pop() != c) return false;
    }
    return stack.isEmpty();
}
```

**6. LRU 缓存（LeetCode #146）**

```java
// 要求：get 和 put 都在 O(1) 时间复杂度
class LRUCache extends LinkedHashMap<Integer, Integer> {
    private int capacity;
    LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    public int get(int key) {
        return super.getOrDefault(key, -1);
    }
    public void put(int key, int value) {
        super.put(key, value);
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}
```

**7. Top K 高频元素（LeetCode #347）**

```java
public int[] topKFrequent(int[] nums, int k) {
    // 1. 统计频率
    Map<Integer, Integer> freq = new HashMap<>();
    for (int n : nums) freq.merge(n, 1, Integer::sum);
    // 2. 小顶堆，保留前K个
    PriorityQueue<Integer> pq = new PriorityQueue<>(
        Comparator.comparingInt(freq::get));
    for (int n : freq.keySet()) {
        pq.offer(n);
        if (pq.size() > k) pq.poll();
    }
    // 3. 收集结果
    return pq.stream().mapToInt(i -> i).toArray();
}
```

### 较难（大厂面试）

**8. 手写简易 HashMap**

```java
// 要求：实现 put(K, V)、get(K)、remove(K)、size()
// 底层：数组 + 链表，支持自动扩容
// 提示：参考 HashMap 的 hash 扰动和链表操作
```

**9. 手写 LRU 缓存（不使用 LinkedHashMap）**

```java
// 要求：手写双向链表 + HashMap
// 提示：需要 Node(key, val, prev, next) + dummy head/tail
```

**10. 用 Stream 处理数据**

```java
// 给定 List<Student>，完成以下操作：
// 1. 找出成绩大于80的所有学生姓名
// 2. 按班级分组，计算每班平均分
// 3. 找出每个班级成绩最高的学生
// 4. 按成绩降序，取前3名
// 5. 统计总人数、平均分、最高分、最低分
```

---

## 附录：面试必背清单

- [ ] 画出集合体系结构图
- [ ] ArrayList 扩容机制（1.5倍、Arrays.copyOf）
- [ ] ArrayList vs LinkedList 对比
- [ ] HashMap put 完整流程（hash → 定位桶 → 链表/红黑树 → 扩容）
- [ ] HashMap 为什么容量是 2 的幂
- [ ] HashMap 为什么线程不安全
- [ ] ConcurrentHashMap JDK7 vs JDK8 实现
- [ ] HashSet 底层（HashMap 的 key）
- [ ] Comparable vs Comparator
- [ ] ConcurrentModificationException 原因和解决方案
- [ ] Stream 中间操作 vs 终端操作
- [ ] Collectors 常用收集器（toList/toMap/groupingBy/partitioningBy）
- [ ] 手写两数之和
- [ ] 手写有效的括号
- [ ] 手写 LRU 缓存
