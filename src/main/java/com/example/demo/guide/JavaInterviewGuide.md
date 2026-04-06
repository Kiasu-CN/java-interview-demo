# Java 基础 + 集合 + IO 面试知识梳理

> 按 **必须掌握程度** 分级：⭐ 必考 | 🔶 高频 | 🔹 进阶

---

## 一、Java 基础

### 1. 数据类型与包装类 ⭐

**知识点：**
- 8种基本类型及其范围
- 自动装箱拆箱（IntegerCache 缓存池 -128~127）
- `Integer.valueOf()` vs `new Integer()`

**必须会写的代码：** 判断以下输出结果

```java
Integer a = 127, b = 127;
Integer c = 128, d = 128;
System.out.println(a == b);  // ?
System.out.println(c == d);  // ?

Integer e = Integer.valueOf(127);
Integer f = Integer.valueOf(127);
System.out.println(e == f);  // ?

Integer g = new Integer(127);
System.out.println(a == g);  // ?
System.out.println(a.equals(g)); // ?
```

**练习题：**
1. 手写一个方法，判断一个整数是否为回文数（如 121、1331）
2. 手写一个方法，统计一个字符串中每个字符出现的次数（用 Map）

---

### 2. String ⭐

**知识点：**
- String 不可变性（final char[] / final byte[]）
- `==` vs `equals()`（面试必问）
- String 常量池
- String / StringBuilder / StringBuffer 区别
- `substring()`、`split()`、`replace()` 常用方法

**必须会写的代码：**

```java
// 面试经典题：以下创建了几个对象？
String s1 = "a" + "b" + "c";          // 编译期优化 → 1个对象 "abc"
String s2 = "abc";
System.out.println(s1 == s2);          // true

String s3 = new String("abc");         // 堆 + 常量池 → 最多2个对象
String s4 = s3.intern();               // intern() 返回常量池引用
System.out.println(s2 == s4);          // true
```

**练习题：**
1. 反转字符串（不能用 `StringBuilder.reverse()`）
2. 判断两个字符串是否为字母异位词（anagram，如 "listen" 和 "silent"）
3. 找出字符串中最长的无重复字符子串长度

---

### 3. Object 核心方法 ⭐

**知识点：**
- `equals()` 和 `hashCode()` 的契约：equals 相等则 hashCode 必须相等，反之不要求
- 为什么重写 equals 必须重写 hashCode？（HashMap 中两个 equals 相等的对象如果 hashCode 不同，会被分到不同桶）
- `toString()`、`clone()`（深浅拷贝）

**必须会写的代码：** 手动重写 equals 和 hashCode

```java
class Student {
    String name;
    int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;               // 同一引用
        if (o == null || getClass() != o.getClass()) return false; // null 或类型不同
        Student student = (Student) o;
        return age == student.age && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

**练习题：**
1. 定义一个 `Person` 类（name, age, idCard），完整重写 equals/hashCode/toString
2. 将 Person 放入 HashSet 和 HashMap，验证去重逻辑

---

### 4. 异常处理 🔶

**知识点：**
```
Throwable
├── Error（不处理）：StackOverflowError, OutOfMemoryError
└── Exception
    ├── 受检异常（checked）：IOException, SQLException（必须 try-catch 或 throws）
    └── 非受检异常（unchecked/runtime）：NullPointerException, ArrayIndexOutOfBoundsException
```
- try-catch-finally 执行顺序
- finally 中 return 会覆盖 try 中的 return（面试坑题）

**必须会写的代码：**

```java
// 经典面试题：输出什么？
public static int test() {
    try {
        return 1;
    } finally {
        return 2;  // finally 的 return 会覆盖 try 的 return
    }
}
// 答案：返回 2（但实际开发中绝对不要这么写）

// try-with-resources（Java 7+，自动关闭资源）
try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
    return br.readLine();
} // 自动调用 br.close()
```

**练习题：**
1. 手写一个方法，安全地读取文件内容并在 finally 中关闭流
2. 自定义一个异常类 `InsufficientBalanceException`，模拟银行取款余额不足场景

---

### 5. 泛型 🔶

**知识点：**
- 泛型擦除：编译后泛型信息被擦除，运行时 `List<String>` 和 `List<Integer>` 都是 `List`
- `<T>`、`<? extends T>`（上界通配符，只读）、`<? super T>`（下界通配符，只写）
- PECS 原则：Producer Extends, Consumer Super

**必须理解：**
```java
List<?> list = new ArrayList<String>();
// list.add("hello"); // 编译错误！? 不知道具体类型，不能 add（null 除外）
Object obj = list.get(0); // 可以 get，返回 Object

List<? extends Number> nums = new ArrayList<Integer>();
// nums.add(1);           // 编译错误！
Number n = nums.get(0);  // OK

List<? super Integer> ints = new ArrayList<Number>();
ints.add(1);              // OK！可以 add Integer 及其子类
Object o = ints.get(0);   // 只能拿 Object
```

---

### 6. 反射 🔹

**知识点：**
- Class 对象的三种获取方式
- 通过反射创建实例、调用方法、访问字段
- 应用场景：框架（Spring IOC）、注解处理

**必须会写的代码：**

```java
// 获取 Class 对象
Class<?> clazz = Class.forName("com.example.Student");
Class<?> clazz2 = Student.class;
Class<?> clazz3 = new Student().getClass();

// 反射创建对象
Student s = (Student) clazz.getDeclaredConstructor().newInstance();

// 反射调用方法
Method method = clazz.getDeclaredMethod("setName", String.class);
method.invoke(s, "张三");

// 反射访问私有字段
Field field = clazz.getDeclaredField("age");
field.setAccessible(true); // 突破 private 限制
field.set(s, 20);
```

---

## 二、Java 集合框架

### 1. 体系结构 ⭐（必须能画出这个图）

```
Collection（接口）
├── List（有序可重复）
│   ├── ArrayList    — 数组，查询快 O(1)，增删慢 O(n)
│   ├── LinkedList   — 双向链表，增删快 O(1)，查询慢 O(n)
│   └── Vector       — 线程安全的 ArrayList（已过时）
├── Set（无序不可重复）
│   ├── HashSet      — HashMap 的 key，允许 null
│   ├── LinkedHashSet— 保持插入顺序
│   └── TreeSet      — 红黑树排序
└── Queue（队列）
    ├── LinkedList   — 同时实现 List 和 Deque
    ├── PriorityQueue— 小顶堆，按优先级出队
    └── ArrayDeque   — 双端队列，栈推荐用这个

Map（接口，独立于 Collection）
├── HashMap         — 数组+链表+红黑树，允许 null key/value
├── LinkedHashMap   — 保持插入/访问顺序
├── TreeMap         — 红黑树，按 key 排序
├── Hashtable       — 线程安全（已过时）
└── ConcurrentHashMap— 线程安全，分段锁/CAS（面试重点）
```

---

### 2. ArrayList ⭐

**知识点：**
- 底层是 `Object[]` 数组，默认初始容量 10
- 扩容：每次扩为原来的 **1.5 倍**（`oldCapacity + (oldCapacity >> 1)`）
- 随机访问 O(1)，中间插入删除 O(n)
- 非线程安全

**面试必问：** ArrayList vs LinkedList？
| | ArrayList | LinkedList |
|--|-----------|------------|
| 底层 | 数组 | 双向链表 |
| 随机访问 | O(1) | O(n) |
| 头部插入 | O(n) | O(1) |
| 内存占用 | 紧凑 | 每个节点额外存 prev/next |
| 缓存友好 | 是 | 否 |

**练习题：**
1. 手写一个简易 `MyArrayList`，实现 add、get、remove、扩容
2. 用 ArrayList 实现 LRU 缓存（提示：remove 后 add 到末尾）

---

### 3. HashMap ⭐⭐⭐（面试重中之重）

**知识点：**
- **底层数据结构：** 数组 + 链表 + 红黑树（JDK 8+）
- **默认容量：** 16，负载因子 0.75
- **链表转红黑树：** 链表长度 ≥ 8 且数组长度 ≥ 64
- **红黑树退化链表：** 节点数 ≤ 6
- **put 流程（必须能口述）：**
  1. 计算 key 的 hash（高16位异或低16位，减少碰撞）
  2. `(n-1) & hash` 定位数组下标
  3. 该位置为空 → 直接放入
  4. 该位置有值 → 判断 key 是否相同（equals）→ 相同则覆盖 value
  5. 不相同 → 尾插法加入链表 / 红黑树插入
  6. 链表长度 ≥ 8 → 转红黑树
  7. size > capacity * loadFactor → 扩容为 2 倍，rehash
- **为什么容量是 2 的幂次？** 因为 `(n-1) & hash` 等价于取模，位运算更快
- **为什么线程不安全？** JDK7 头插法导致死循环、数据覆盖

**必须会写的代码：**

```java
// 手写 hash 方法（理解扰动函数）
static int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

// 手写定位桶下标
int index = (table.length - 1) & hash(key);
```

**练习题：**
1. 手写简易 `MyHashMap`（只需 put/get，数组+链表）
2. 给一个整数数组和一个目标值，返回两个数的下标（使它们的和等于目标值）— 用 HashMap O(n)

```java
// 两数之和（LeetCode 1，面试最高频题）
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

---

### 4. ConcurrentHashMap ⭐

**知识点：**
- **JDK 7：** Segment 分段锁，每段独立 ReentrantLock
- **JDK 8：** 取消 Segment，用 `synchronized + CAS`，锁粒度细化到单个桶（Node）
- put 流程：CAS 尝试写入 → 失败则 synchronized 锁住头节点

---

### 5. Fail-Fast vs Fail-Safe 🔶

**知识点：**
- **Fail-Fast：** ArrayList/HashMap 的迭代器，遍历时结构性修改（add/remove）抛 `ConcurrentModificationException`，通过 modCount 检测
- **Fail-Safe：** CopyOnWriteArrayList/ConcurrentHashMap，遍历的是副本或弱一致性，不抛异常

**必须会写的代码（安全删除）：**

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// 方式1：Iterator.remove()
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() == 3) it.remove();
}

// 方式2：removeIf（推荐）
list.removeIf(n -> n % 2 == 0);

// 方式3：CopyOnWriteArrayList（适用于读多写少）
List<String> safe = new CopyOnWriteArrayList<>(Arrays.asList("a", "b", "c"));
for (String s : safe) {
    safe.add("d"); // 不会报错（但遍历不到新加的元素）
}
```

---

## 三、IO

### 1. 体系结构 ⭐

```
字节流（处理任意数据）              字符流（处理文本）
├── InputStream                    ├── Reader
│   ├── FileInputStream            │   ├── FileReader
│   ├── BufferedInputStream        │   ├── BufferedReader ← 有 readLine()
│   ├── ObjectInputStream          │   └── InputStreamReader ← 桥梁：字节→字符
│   └── ByteArrayInputStream       │
└── OutputStream                   └── Writer
    ├── FileOutputStream               ├── FileWriter
    ├── BufferedOutputStream           ├── BufferedWriter ← 有 newLine()
    ├── ObjectOutputStream             └── OutputStreamWriter ← 桥梁：字符→字节
    └── ByteArrayOutputStream
```

**核心记忆：**
- 字节流以 `Stream` 结尾，字符流以 `Reader/Writer` 结尾
- 装饰器模式：`new BufferedReader(new FileReader("file.txt"))`
- `InputStreamReader` 是字节流 → 字符流的桥梁

---

### 2. BIO vs NIO vs AIO ⭐

| | BIO | NIO | AIO |
|--|-----|-----|-----|
| 全称 | Blocking IO | Non-blocking IO | Asynchronous IO |
| 模型 | 同步阻塞 | 同步非阻塞 | 异步非阻塞 |
| 核心 | Stream | Channel + Buffer + Selector | CompletionHandler |
| 适用 | 连接数少且固定 | 连接数多且短 | 连接数多且长 |

**NIO 三大核心：**
- **Buffer：** 读写数据的容器（ByteBuffer、CharBuffer...）
- **Channel：** 双向通道，可读可写
- **Selector：** 多路复用器，一个线程管理多个 Channel

---

### 3. 文件操作 ⭐（必须会写的代码）

**经典写法：逐行读取文件**

```java
// Java 8+ 最佳实践
try (BufferedReader br = new BufferedReader(new FileReader("input.txt"));
     BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        bw.write(line);
        bw.newLine();
    }
} // 自动关闭资源

// Java 8 NIO 简洁写法
List<String> lines = Files.readAllLines(Paths.get("input.txt"));
Files.write(Paths.get("output.txt"), lines);

// 流式读取（适合大文件）
try (Stream<String> stream = Files.lines(Paths.get("big.txt"))) {
    stream.filter(line -> line.contains("ERROR"))
          .forEach(System.out::println);
}
```

**练习题：**
1. 复制一个文件的所有内容到另一个文件（用字节流）
2. 统计一个文本文件中每个单词出现的频率，按频率降序输出
3. 读取 CSV 文件（姓名,年龄,成绩），按成绩排序后写入新文件

---

### 4. 序列化 🔶

**知识点：**
- 实现 `Serializable` 接口（标记接口，无方法）
- `serialVersionUID`：版本控制，反序列化时版本不一致会报错
- `transient`：修饰的字段不参与序列化
- `Externalizable`：自定义序列化逻辑

**必须会写的代码：**

```java
class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    transient String password; // 不序列化

    // 序列化
    public static void serialize(User user, String file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(user);
        }
    }

    // 反序列化
    public static User deserialize(String file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (User) ois.readObject();
        }
    }
}
```

---

## 四、综合练习题

### 简单（必须能 5 分钟写出来）

1. **反转字符串** — `char[]` 双指针交换
2. **判断回文串** — 忽略大小写和非字母数字字符
3. **FizzBuzz** — 1~100，3的倍数打印Fizz，5的倍数打印Buzz，同时是打印FizzBuzz
4. **用 HashMap 统计字符频率**
5. **实现一个简易栈**（用 ArrayList）

### 中等（面试高频）

6. **两数之和**（HashMap，O(n)）
7. **有效的括号**（栈：`({[]})` → true，`([)]` → false）
8. **合并两个有序链表**
9. **LRU 缓存**（LinkedHashMap 或手写双向链表+HashMap）
10. **读取文件并统计词频 Top 10**（Stream + Collectors）

### 较难（大厂面试）

11. **手写简易 HashMap**（put/get/remove，数组+链表，含扩容）
12. **手写线程安全的单例模式**（DCL + volatile、静态内部类、枚举）
13. **生产者-消费者模式**（BlockingQueue / wait-notify）
