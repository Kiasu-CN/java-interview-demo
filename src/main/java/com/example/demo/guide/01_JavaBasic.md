# Java 基础面试完全指南

---

## 一、数据类型

### 1.1 基本数据类型 ⭐

| 类型 | 大小 | 范围 | 默认值 |
|------|------|------|--------|
| byte | 1字节 | -128 ~ 127 | 0 |
| short | 2字节 | -32768 ~ 32767 | 0 |
| int | 4字节 | -2^31 ~ 2^31-1（约±21亿） | 0 |
| long | 8字节 | -2^63 ~ 2^63-1 | 0L |
| float | 4字节 | IEEE 754 | 0.0f |
| double | 8字节 | IEEE 754 | 0.0d |
| char | 2字节 | 0 ~ 65535（Unicode） | '\u0000' |
| boolean | ~ | true / false | false |

**常见陷阱：**

```java
// 整数溢出（面试常考）
int max = Integer.MAX_VALUE; // 2147483647
System.out.println(max + 1);  // -2147483648！溢出变为负数

// 浮点精度问题（面试常考）
System.out.println(0.1 + 0.2);           // 0.30000000000000004（不是0.3）
System.out.println(0.1 + 0.2 == 0.3);    // false！
// 解决：用 BigDecimal
BigDecimal a = new BigDecimal("0.1");
BigDecimal b = new BigDecimal("0.2");
System.out.println(a.add(b));  // 0.3

// long 类型要加 L
long num = 10000000000L; // 不加L会报错，因为整数默认int

// 类型转换
// 自动转换：byte → short → int → long → float → double
// 强制转换：大 → 小，可能丢失精度
int i = 128;
byte b = (byte) i; // -128，溢出截断
```

### 1.2 包装类与自动装箱拆箱 ⭐

```java
// 自动装箱：基本类型 → 包装类
Integer a = 10;        // 编译为 Integer.valueOf(10)
// 自动拆箱：包装类 → 基本类型
int b = a;             // 编译为 a.intValue()

// Integer 缓存池（面试高频）— Byte, Short, Integer, Long 都有缓存 -128~127
Integer x = 127;
Integer y = 127;
System.out.println(x == y);   // true  ← 缓存池中同一个对象

Integer m = 128;
Integer n = 128;
System.out.println(m == n);   // false ← 超出缓存池，new 了不同对象
System.out.println(m.equals(n)); // true ← 内容相同

// 经典陷阱：拆箱时空指针
Integer nullInt = null;
int val = nullInt;  // NullPointerException！自动拆箱调用了 null.intValue()
```

**练习题：**
1. 手写一个方法判断整数是否溢出（不用 BigInteger）
2. 实现一个简单的 BigDecimal 计算：输入两个小数字符串，输出精确的加、减、乘、除结果

---

## 二、String ⭐⭐⭐

### 2.1 不可变性

```java
// String 底层是 private final byte[] value（JDK9+）, private final char[] value（JDK8）
// final 修饰引用，数组内容不可改，且 String 类没有提供修改 value 的方法
String s = "hello";
s = s + " world"; // 不是修改原对象！而是创建了一个新 String 对象
```

**面试题：为什么 String 设计为不可变？**
1. **字符串常量池**：相同内容可共享同一对象，节省内存
2. **安全性**：String 常作为参数（如网络连接、文件路径），不可变保证不被篡改
3. **线程安全**：不可变天然线程安全，无需同步
4. **hashCode 缓存**：不可变所以 hashCode 只需计算一次

### 2.2 == vs equals

```java
String s1 = "abc";              // 常量池
String s2 = "abc";              // 常量池中已存在，直接复用
String s3 = new String("abc");  // 堆上新建对象
String s4 = s3.intern();        // 返回常量池中的 "abc"

System.out.println(s1 == s2);           // true  → 同一个常量池对象
System.out.println(s1 == s3);           // false → 堆 vs 常量池
System.out.println(s1.equals(s3));      // true  → 内容相同
System.out.println(s1 == s4);           // true  → intern() 返回常量池引用
System.out.println(s3 == s4);           // false → 堆 vs 常量池

// 面试题：创建了几个对象？
String a = "a";
String b = "b";
String ab = a + b;        // 等价于 new StringBuilder().append("a").append("b").toString()
                          // 创建了 StringBuilder 对象 + toString() 创建的 String 对象 = 2个
String ab2 = "a" + "b";   // 编译期优化为 "ab"，0个新对象（常量池已有则复用）
String ab3 = "ab";
System.out.println(ab == ab3);   // false！ab 是堆上对象
System.out.println(ab2 == ab3);  // true！都在常量池
```

### 2.3 String / StringBuilder / StringBuffer

| | String | StringBuilder | StringBuffer |
|--|--------|--------------|--------------|
| 可变性 | 不可变 | 可变 | 可变 |
| 线程安全 | 安全（不可变） | 不安全 | 安全（synchronized） |
| 性能 | 拼接最慢 | 最快 | 比 StringBuilder 慢 |
| 使用场景 | 少量操作 | 单线程拼接 | 多线程拼接 |

```java
// 性能对比
long start = System.currentTimeMillis();
String s = "";
for (int i = 0; i < 100000; i++) s += i;         // 极慢：每次创建新对象
System.out.println("String: " + (System.currentTimeMillis() - start) + "ms");

start = System.currentTimeMillis();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100000; i++) sb.append(i);    // 快：在同一个对象上操作
System.out.println("StringBuilder: " + (System.currentTimeMillis() - start) + "ms");
```

### 2.4 String 常用方法（必须熟练）

```java
String s = "Hello, World!";

// --- 查找 ---
s.length()                          // 13
s.charAt(0)                         // 'H'
s.indexOf("World")                  // 7
s.lastIndexOf("l")                  // 9
s.contains("World")                 // true
s.startsWith("Hello")               // true
s.endsWith("!")
s.isEmpty()                         // false
s.isBlank()                         // false（Java 11+，空字符串或全空格为true）

// --- 截取 ---
s.substring(7)                      // "World!"
s.substring(7, 12)                  // "World"（[7,12)）

// --- 分割 ---
"a,b,,c".split(",")                 // ["a", "b", "", "c"]
"a  b   c".split("\\s+")            // ["a", "b", "c"]（正则分割空白）

// --- 替换 ---
s.replace("World", "Java")          // "Hello, Java!"
"abc123".replaceAll("\\d", "*")     // "abc***"（正则）
"abc123".replaceFirst("\\d", "*")   // "abc*23"

// --- 大小写 & 去空格 ---
"  Hello  ".trim()                  // "Hello"
"Hello".toUpperCase()               // "HELLO"
"HELLO".toLowerCase()               // "hello"

// --- 比较 ---
"abc".equals("ABC")                 // false
"abc".equalsIgnoreCase("ABC")       // true
"abc".compareTo("abd")              // -1（字典序差值）

// --- 转换 ---
"Java".toCharArray()                // ['J','a','v','a']
String.valueOf(123)                 // "123"
String.join("-", "a", "b", "c")    // "a-b-c"

// --- 格式化 ---
String.format("姓名:%s,年龄:%d,分数:%.1f", "张三", 20, 95.5)
String.format("%04d", 42)           // "0042"
String.format("%.2f%%", 3.1415)     // "3.14%"
```

**练习题：**
1. 反转字符串（手写，不用 `StringBuilder.reverse()`）
2. 判断回文字符串（忽略大小写和非字母数字字符）
3. 统计一个字符串中每个字符出现的次数
4. 将字符串中的空格替换为 `%20`
5. 验证字符串是否是有效的 IPv4 地址
6. 实现字符串的左旋转操作（如 "abcde" 左旋 2 位 → "cdeab"）

---

## 三、数组与Arrays工具类

### 3.1 数组基础

```java
// 声明与初始化
int[] arr1 = new int[5];              // [0, 0, 0, 0, 0]
int[] arr2 = {1, 2, 3, 4, 5};
int[] arr3 = new int[]{1, 2, 3};

// 二维数组
int[][] matrix = new int[3][4];       // 3行4列，默认全0
int[][] matrix2 = {{1, 2}, {3, 4, 5}, {6}};  // 不规则数组

// 数组长度
arr2.length    // 注意：length 是属性，不是方法！

// 数组拷贝
int[] copy = Arrays.copyOf(arr2, arr2.length);
int[] range = Arrays.copyOfRange(arr2, 1, 4); // [1,4)

// System.arraycopy（最高效）
int[] src = {1, 2, 3, 4, 5};
int[] dest = new int[5];
System.arraycopy(src, 0, dest, 0, src.length);
```

### 3.2 Arrays 工具类

```java
int[] arr = {5, 3, 8, 1, 9, 2, 7};

Arrays.sort(arr);                           // [1, 2, 3, 5, 7, 8, 9]
Arrays.sort(arr, 0, 4);                     // 只排 [0,4)
Arrays.toString(arr);                       // "[5, 3, 8, 1, 9, 2, 7]"
Arrays.deepToString(new int[][]{{1,2},{3}}); // "[[1, 2], [3]]"

// 二分查找（必须先排序！）
int idx = Arrays.binarySearch(sortedArr, 5);  // 找到返回索引
int idx2 = Arrays.binarySearch(sortedArr, 4); // 未找到返回 -(插入点+1)

Arrays.fill(arr, 0);                        // 全部填0
Arrays.fill(arr, 1, 3, 9);                  // [1,3) 填9
Arrays.equals(arr1, arr2);                  // 比较内容

// asList 陷阱（面试常考）
String[] sa = {"a", "b", "c"};
List<String> list = Arrays.asList(sa);      // 返回的是 Arrays 内部类！
// list.add("d");  → UnsupportedOperationException!
// list.remove(0); → UnsupportedOperationException!
// list.set(0, "x"); → OK（会修改原数组！）

// 正确做法
List<String> realList = new ArrayList<>(Arrays.asList(sa));

// stream
int sum = Arrays.stream(arr).sum();
double avg = Arrays.stream(arr).average().orElse(0);
int[] filtered = Arrays.stream(arr).filter(x -> x > 3).toArray();
```

**练习题：**
1. 手写数组反转（双指针）
2. 手写数组去重（保持顺序 / 不保持顺序）
3. 合并两个有序数组
4. 找出数组中第二大的数

---

## 四、面向对象 ⭐

### 4.1 四大特性

| 特性 | 说明 | 体现 |
|------|------|------|
| 封装 | 隐藏实现细节，暴露接口 | private + getter/setter |
| 继承 | 子类复用父类属性和方法 | extends（单继承） |
| 多态 | 同一方法不同表现 | 父类引用指向子类对象 |
| 抽象 | 定义规范，不关心实现 | abstract class / interface |

### 4.2 多态（面试高频）

```java
class Animal {
    void speak() { System.out.println("动物叫"); }
}
class Dog extends Animal {
    @Override void speak() { System.out.println("汪汪"); }
}
class Cat extends Animal {
    @Override void speak() { System.out.println("喵喵"); }
}

// 多态：编译看左边，运行看右边
Animal a = new Dog();
a.speak(); // "汪汪" — 运行时调用 Dog 的方法

// instanceof 判断
if (a instanceof Dog) {
    Dog d = (Dog) a; // 向下转型
    d.speak();
}
```

**多态的三个前提：** 继承/实现、方法重写、父类引用指向子类对象

### 4.3 重写 vs 重载

| | 重写(Override) | 重载(Overload) |
|--|----------------|----------------|
| 发生在 | 子类与父类之间 | 同一个类中 |
| 方法名 | 相同 | 相同 |
| 参数列表 | 相同 | 不同（类型/个数/顺序） |
| 返回值 | 相同或是其子类 | 无要求 |
| 访问权限 | 不能更严格 | 无要求 |
| 异常 | 不能抛出更多受检异常 | 无要求 |
| 绑定 | 运行时（动态绑定） | 编译时（静态绑定） |

### 4.4 接口 vs 抽象类

| | 接口(interface) | 抽象类(abstract class) |
|--|-----------------|----------------------|
| 实例化 | 不能 | 不能 |
| 构造方法 | 无 | 有 |
| 成员变量 | 只能 public static final | 任意 |
| 方法 | 默认 public abstract（JDK8+可有default/static方法） | 任意 |
| 多继承 | 类可实现多个接口 | 只能单继承 |
| 设计理念 | "能做什么"（行为契约） | "是什么"（模板） |

```java
// 接口的 default 方法（JDK 8+）
interface Flyable {
    void fly();
    default void land() {           // default 方法可以有实现
        System.out.println("降落");
    }
    static void checkWeather() {    // static 方法
        System.out.println("检查天气");
    }
}
```

### 4.5 访问修饰符

| 修饰符 | 同类 | 同包 | 子类 | 不同包 |
|--------|------|------|------|--------|
| private | ✅ | ❌ | ❌ | ❌ |
| (default) | ✅ | ✅ | ❌ | ❌ |
| protected | ✅ | ✅ | ✅ | ❌ |
| public | ✅ | ✅ | ✅ | ✅ |

### 4.6 其他关键字

```java
// static：属于类，不属于实例
class Counter {
    static int count = 0;       // 所有实例共享
    static { count = 10; }      // 静态代码块，类加载时执行一次
    static void reset() { count = 0; } // 静态方法，不能访问 this
}

// final：不可变
final class Immutable {}              // 不可继承
final int MAX = 100;                  // 不可修改
final void method() {}                // 不可重写

// this vs super
// this：当前对象引用   super：父类引用
// this()：调用本类其他构造器   super()：调用父类构造器（必须是构造器第一行）
```

**练习题：**
1. 设计一个 `Shape` 接口，`Circle` 和 `Rectangle` 实现类，计算面积和周长
2. 设计一个简单的动物继承体系，演示多态
3. 用接口 + default 方法实现一个日志框架（可以输出到控制台或文件）

---

## 五、Object 核心方法 ⭐

### 5.1 equals() 和 hashCode()

**契约（必须记住）：**
1. equals 相等 → hashCode 必须相等
2. hashCode 相等 → equals 不一定相等（哈希碰撞）
3. 重写 equals 必须重写 hashCode（否则 HashMap 中出错）

```java
// 为什么？HashMap 用 hashCode 定位桶，用 equals 判断是否同一个 key
// 如果两个对象 equals 但 hashCode 不同 → 分到不同桶 → 找不到 → 逻辑错误！
```

**正确写法：**

```java
class Person {
    String name;
    int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
```

### 5.2 clone() — 深拷贝 vs 浅拷贝

```java
class Address {
    String city;
    Address(String city) { this.city = city; }
}

class User implements Cloneable {
    String name;
    Address address; // 引用类型

    @Override
    protected User clone() throws CloneNotSupportedException {
        User cloned = (User) super.clone();  // 浅拷贝：address 仍指向同一对象
        cloned.address = new Address(this.address.city); // 深拷贝：手动复制引用类型
        return cloned;
    }
}
```

**练习题：**
1. 定义 `Student` 类（name, age, scores数组），实现深拷贝
2. 将 Student 放入 `HashSet`，验证不重写 hashCode 时的 bug

---

## 六、异常处理 ⭐

### 6.1 异常体系

```
Throwable
├── Error（不应捕获）
│   ├── StackOverflowError     — 递归太深
│   ├── OutOfMemoryError       — 内存不足
│   └── NoClassDefFoundError   — 类找不到
└── Exception
    ├── 受检异常(Checked) — 必须处理（try-catch 或 throws）
    │   ├── IOException
    │   ├── FileNotFoundException
    │   ├── SQLException
    │   ├── ClassNotFoundException
    │   └── ParseException
    └── 非受检异常(Unchecked/RuntimeException) — 可不处理
        ├── NullPointerException          — 空指针
        ├── ArrayIndexOutOfBoundsException — 数组越界
        ├── ClassCastException            — 类型转换错误
        ├── NumberFormatException         — 数字格式错误
        ├── IllegalArgumentException      — 非法参数
        ├── IllegalStateException         — 非法状态
        ├── ConcurrentModificationException — 并发修改
        └── ArithmeticException           — 算术错误（如 /0）
```

### 6.2 try-catch-finally 执行顺序

```java
// 经典面试题1：
public static int test1() {
    int i = 0;
    try {
        i = 1;
        return i;
    } finally {
        i = 2;  // finally 修改了 i，但 return 的值已经缓存为 1
    }
}
// 返回 1（不是2！）finally 中对变量的修改不影响已经缓存的返回值

// 经典面试题2：
public static int test2() {
    try {
        return 1;
    } finally {
        return 2;  // finally 中的 return 会覆盖 try 中的！
    }
}
// 返回 2（实际开发中千万不要在 finally 中 return）

// 执行顺序：try → catch → finally → return
// finally 始终执行（除非 JVM 退出 System.exit() 或线程被杀）
```

### 6.3 try-with-resources（Java 7+）

```java
// 自动关闭实现了 AutoCloseable 的资源
try (FileInputStream fis = new FileInputStream("a.txt");
     BufferedInputStream bis = new BufferedInputStream(fis)) {
    // 使用资源
} catch (IOException e) {
    e.printStackTrace();
} // 自动调用 bis.close() → fis.close()，无需 finally

// 等价于手写：
FileInputStream fis = null;
try {
    fis = new FileInputStream("a.txt");
    // ...
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (fis != null) {
        try { fis.close(); } catch (IOException e) { e.printStackTrace(); }
    }
}
```

### 6.4 自定义异常

```java
// 自定义受检异常
class InsufficientBalanceException extends Exception {
    private double amount;
    public InsufficientBalanceException(double amount) {
        super("余额不足，差额: " + amount);
        this.amount = amount;
    }
    public double getAmount() { return amount; }
}

// 自定义非受检异常
class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException(String message) {
        super(message);
    }
}

// 使用
void withdraw(double balance, double amount) throws InsufficientBalanceException {
    if (amount > balance) {
        throw new InsufficientBalanceException(amount - balance);
    }
    balance -= amount;
}
```

**练习题：**
1. 自定义 `ScoreOutOfRangeException`，编写一个方法验证成绩（0~100），超出范围抛异常
2. 实现一个简单的计算器，处理除零异常和输入非数字异常
3. 手写资源复制方法（用 try-with-resources）

---

## 七、泛型 🔶

### 7.1 基本用法

```java
// 泛型类
class Box<T> {
    private T value;
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}
Box<String> strBox = new Box<>();
strBox.set("hello");
// strBox.set(123); // 编译错误！类型安全

// 泛型方法
public static <T> T getFirst(List<T> list) {
    return list.isEmpty() ? null : list.get(0);
}

// 多个类型参数
class Pair<K, V> {
    K key; V value;
    Pair(K key, V value) { this.key = key; this.value = value; }
}
```

### 7.2 通配符与 PECS 原则

```java
// ? 无界通配符 — 不知道具体类型
List<?> list = new ArrayList<String>();
// list.add("x"); // 编译错误！不能 add（类型不确定）
Object o = list.get(0); // 只能拿 Object

// ? extends T 上界通配符 — 适合读（Producer Extends）
List<? extends Number> nums = new ArrayList<Integer>();
// nums.add(1);  // 编译错误！不知道具体是 Integer 还是 Double
Number n = nums.get(0); // OK，一定是 Number 或其子类

// ? super T 下界通配符 — 适合写（Consumer Super）
List<? super Integer> ints = new ArrayList<Number>();
ints.add(1);      // OK！Integer 是 Number 的子类，可以安全 add
ints.add(2);
Object o = ints.get(0); // 只能拿 Object

// PECS：Producer Extends, Consumer Super
// 如果要从集合读取数据用 extends
// 如果要往集合写入数据用 super
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T t : src) dest.add(t);
}
```

### 7.3 类型擦除

```java
// 泛型只在编译期存在，运行时被擦除
List<String> strList = new ArrayList<>();
List<Integer> intList = new ArrayList<>();
System.out.println(strList.getClass() == intList.getClass()); // true！

// 擦除后都是 List（raw type）
// 不能 new T()、不能 new T[]、不能 instanceof T
// 不能创建泛型数组：new List<String>[10] ← 编译错误
```

**练习题：**
1. 手写一个泛型工具类 `PairUtil`，包含 swap、getMax、getMin 方法
2. 手写一个简单的泛型栈 `MyStack<T>`

---

## 八、枚举 🔶

```java
// 基本枚举
enum Season {
    SPRING("春"), SUMMER("夏"), AUTUMN("秋"), WINTER("冬");

    private String name;
    Season(String name) { this.name = name; }
    public String getName() { return name; }
}

// 枚举实现接口
enum Color implements Printable {
    RED { @Override public void print() { System.out.println("红色"); } },
    GREEN { @Override public void print() { System.out.println("绿色"); } };
}

// 枚举常用方法
Season.values()            // [SPRING, SUMMER, AUTUMN, WINTER]
Season.valueOf("SPRING")   // Season.SPRING
Season.SPRING.ordinal()    // 0（序号）
Season.SPRING.name()       // "SPRING"

// 面试：枚举实现单例（最佳实践，防反射、防序列化破坏）
enum Singleton {
    INSTANCE;
    public void doSomething() { }
}
```

---

## 九、Lambda 与函数式接口（Java 8+）⭐

### 9.1 函数式接口

```java
// 只有一个抽象方法的接口
@FunctionalInterface
interface MyFunction {
    int apply(int a, int b);
}
// 使用
MyFunction add = (a, b) -> a + b;
System.out.println(add.apply(3, 5)); // 8

// Java 内置四大函数式接口（必须记住）
// 1. Function<T, R> — 接收T返回R
Function<String, Integer> strLen = s -> s.length();
strLen.apply("hello"); // 5

// 2. Consumer<T> — 接收T无返回（消费）
Consumer<String> print = s -> System.out.println(s);
print.accept("hello"); // 打印 hello

// 3. Supplier<T> — 无输入返回T（提供）
Supplier<Double> random = () -> Math.random();
random.get(); // 随机数

// 4. Predicate<T> — 接收T返回boolean（判断）
Predicate<Integer> isEven = n -> n % 2 == 0;
isEven.test(4); // true

// 其他常用变体
BiFunction<T, U, R>   // 接收两个参数返回一个
UnaryOperator<T>       // 一元操作（T → T）
BinaryOperator<T>      // 二元操作（T, T → T）
```

### 9.2 方法引用

```java
// 四种形式
// 1. 静态方法引用：类名::静态方法
Function<String, Integer> parser = Integer::parseInt;

// 2. 实例方法引用：对象::实例方法
Consumer<String> printer = System.out::println;

// 3. 类型方法引用：类名::实例方法（第一个参数是调用者）
Function<String, Integer> length = String::length; // 等价于 s -> s.length()

// 4. 构造方法引用：类名::new
Supplier<List<String>> listSupplier = ArrayList::new;
Function<Integer, int[]> arrayFactory = int[]::new;
```

### 9.3 Optional（空指针安全）

```java
// 创建
Optional<String> o1 = Optional.of("hello");       // 不能为 null
Optional<String> o2 = Optional.ofNullable(null);   // 可以为 null
Optional<String> o3 = Optional.empty();            // 空值

// 使用
String name = Optional.ofNullable(getName())
    .orElse("默认值");                              // null 时返回默认值
String name2 = Optional.ofNullable(getName())
    .orElseThrow(() -> new IllegalArgumentException("名字不能为空"));
Optional.ofNullable(getName())
    .ifPresent(n -> System.out.println(n));         // 非空时执行

// 链式操作
String city = Optional.ofNullable(user)
    .map(u -> u.getAddress())     // 映射，类似 Stream 的 map
    .map(a -> a.getCity())
    .orElse("未知城市");
```

**练习题：**
1. 用 Lambda + Predicate 过滤一个 List 中满足条件的元素
2. 用 Function 链式转换：字符串 → 大写 → 截取前3位 → 转整数
3. 用 Optional 安全地处理可能为 null 的嵌套对象链

---

## 十、日期时间 API（Java 8+）🔶

```java
import java.time.*;
import java.time.format.DateTimeFormatter;

// --- 创建 ---
LocalDate date = LocalDate.now();               // 2025-04-06
LocalDate specific = LocalDate.of(2025, 1, 1);
LocalTime time = LocalTime.of(14, 30, 0);       // 14:30:00
LocalDateTime datetime = LocalDateTime.now();

// --- 操作 ---
date.plusDays(7)             // 7天后
date.minusMonths(1)          // 1个月前
date.getDayOfWeek()          // DayOfWeek.SUNDAY
date.isLeapYear()            // 是否闰年
date.isBefore(otherDate)     // 比较

// --- 格式化 ---
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
String formatted = datetime.format(fmt);
LocalDateTime parsed = LocalDateTime.parse("2025-01-01 12:00:00", fmt);

// --- 时间间隔 ---
Period period = Period.between(date1, date2);  // 天数、月数、年数
Duration duration = Duration.between(time1, time2); // 秒、纳秒

// --- 时间戳 ---
Instant now = Instant.now();                    // UTC时间戳
long timestamp = now.toEpochMilli();            // 毫秒时间戳
```

---

## 十一、正则表达式 🔶

```java
// 常用语法
// \d 数字  \D 非数字  \w 字母数字下划线  \s 空白
// ^ 开头  $ 结尾  * 0或多  + 1或多  ? 0或1
// [abc]  [a-z]  [^abc]非  | 或  () 分组

// matches — 完全匹配
"13812345678".matches("^1[3-9]\\d{9}$");               // 手机号
"test@qq.com".matches("^\\w+@\\w+\\.\\w+$");           // 邮箱
"2025-01-01".matches("^\\d{4}-\\d{2}-\\d{2}$");       // 日期

// Pattern + Matcher（更灵活）
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher("abc123def456");
while (m.find()) {
    System.out.println(m.group()); // "123" "456"
}

// replaceAll — 替换
"abc123".replaceAll("\\d+", "#"); // "abc#"

// split — 分割
"a,b;c,d".split("[,;]"); // ["a","b","c","d"]
```

---

## 十二、String/StringBuilder 综合练习

### 练习1：反转字符串

```java
// 要求：不能使用 StringBuilder.reverse()
public static String reverse(String s) {
    char[] chars = s.toCharArray();
    int left = 0, right = chars.length - 1;
    while (left < right) {
        char temp = chars[left];
        chars[left++] = chars[right];
        chars[right--] = temp;
    }
    return new String(chars);
}
```

### 练习2：判断回文

```java
public static boolean isPalindrome(String s) {
    s = s.toLowerCase().replaceAll("[^a-z0-9]", "");
    int left = 0, right = s.length() - 1;
    while (left < right) {
        if (s.charAt(left++) != s.charAt(right--)) return false;
    }
    return true;
}
```

### 练习3：统计字符频率

```java
public static Map<Character, Integer> charFrequency(String s) {
    Map<Character, Integer> map = new HashMap<>();
    for (char c : s.toCharArray()) {
        map.put(c, map.getOrDefault(c, 0) + 1);
    }
    return map;
}
```

### 练习4：第一个不重复字符

```java
public static int firstUniqChar(String s) {
    int[] count = new int[26];
    for (char c : s.toCharArray()) count[c - 'a']++;
    for (int i = 0; i < s.length(); i++) {
        if (count[s.charAt(i) - 'a'] == 1) return i;
    }
    return -1;
}
```

### 练习5：最长公共前缀

```java
public static String longestCommonPrefix(String[] strs) {
    if (strs == null || strs.length == 0) return "";
    String prefix = strs[0];
    for (int i = 1; i < strs.length; i++) {
        while (!strs[i].startsWith(prefix)) {
            prefix = prefix.substring(0, prefix.length() - 1);
            if (prefix.isEmpty()) return "";
        }
    }
    return prefix;
}
```

### 练习6：验证括号匹配

```java
public static boolean isValid(String s) {
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

---

## 附录：面试必背代码清单

- [ ] Integer 缓存池判断（127 vs 128）
- [ ] String == vs equals
- [ ] 重写 equals + hashCode
- [ ] 深拷贝 vs 浅拷贝
- [ ] 自定义异常 + throw/throws
- [ ] try-with-resources
- [ ] 泛型通配符（PECS）
- [ ] 枚举实现单例
- [ ] Lambda + 函数式接口
- [ ] Optional 链式调用
- [ ] 反转字符串
- [ ] 回文判断
- [ ] 字符频率统计
- [ ] 括号匹配
