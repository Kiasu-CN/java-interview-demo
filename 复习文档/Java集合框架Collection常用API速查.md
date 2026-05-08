# Java集合框架常用API速查（可反复温习）

目标：覆盖面试与实战最高频的 Java 集合 API（JDK 8+），每类都给出最常用方法和代码演示。

---

## 1. 集合框架总览（记忆版）

- `Collection`：单列集合根接口
- `List`：有序、可重复
- `Set`：不可重复
- `Queue/Deque`：队列/双端队列
- `Map`：键值对（不继承 `Collection`，但属于集合框架）

常见实现类：
- `ArrayList`、`LinkedList`
- `HashSet`、`LinkedHashSet`、`TreeSet`
- `ArrayDeque`、`LinkedList`
- `HashMap`、`LinkedHashMap`、`TreeMap`

---

## 2. `Collection<E>` 通用方法

常用方法：
- `add(E e)`：添加元素
- `addAll(Collection<? extends E> c)`：批量添加
- `remove(Object o)`：删除第一个匹配元素
- `removeAll(Collection<?> c)`：删除交集
- `retainAll(Collection<?> c)`：保留交集
- `contains(Object o)`：是否包含
- `containsAll(Collection<?> c)`：是否包含另一个集合全部元素
- `size()`：元素个数
- `isEmpty()`：是否为空
- `clear()`：清空
- `toArray()`：转数组
- `iterator()`：获取迭代器

```java
import java.util.*;

public class CollectionDemo {
    public static void main(String[] args) {
        Collection<String> c1 = new ArrayList<>();
        c1.add("Java");
        c1.add("Go");

        Collection<String> c2 = new ArrayList<>();
        c2.add("Go");
        c2.add("Python");

        c1.addAll(c2);                    // [Java, Go, Go, Python]
        c1.remove("Go");                 // 删除第一个Go
        boolean hasJava = c1.contains("Java");
        int n = c1.size();

        c1.retainAll(Arrays.asList("Java", "Python")); // [Java, Python]
        Object[] arr = c1.toArray();

        for (String s : c1) {
            System.out.println(s);
        }

        System.out.println(hasJava + ", size=" + n + ", arrLen=" + arr.length);
    }
}
```

---

## 3. `List<E>` 常用方法（`ArrayList`/`LinkedList`）

常用方法：
- `add(E e)` / `add(int index, E e)`
- `get(int index)`
- `set(int index, E e)`
- `remove(int index)` / `remove(Object o)`
- `indexOf(Object o)` / `lastIndexOf(Object o)`
- `subList(int from, int to)`：左闭右开
- `sort(Comparator<? super E> c)`

```java
import java.util.*;

public class ListDemo {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(1);
        list.add(2);
        list.add(1, 99);                  // [3, 99, 1, 2]

        int x = list.get(2);              // 1
        list.set(2, 100);                 // [3, 99, 100, 2]

        list.remove(Integer.valueOf(99)); // 按对象删除
        list.remove(0);                   // 按索引删除

        int idx = list.indexOf(100);
        list.sort(Integer::compareTo);    // [2, 100]

        List<Integer> part = list.subList(0, 1); // [2]
        System.out.println(x + ", idx=" + idx + ", part=" + part);
    }
}
```

---

## 4. `Set<E>` 常用方法（去重）

常用方法：
- `add(E e)`：返回 `false` 代表重复未加入
- `remove(Object o)`
- `contains(Object o)`
- `size()`、`isEmpty()`、`clear()`

`TreeSet` 额外常用（有序集合）：
- `first()` / `last()`
- `ceiling(E e)`：大于等于 e 的最小元素
- `floor(E e)`：小于等于 e 的最大元素
- `higher(E e)` / `lower(E e)`

```java
import java.util.*;

public class SetDemo {
    public static void main(String[] args) {
        Set<String> hs = new HashSet<>();
        hs.add("A");
        hs.add("A");                     // 重复无效
        hs.add("B");

        boolean ok = hs.contains("A");
        hs.remove("B");

        TreeSet<Integer> ts = new TreeSet<>();
        ts.add(10);
        ts.add(30);
        ts.add(20);

        int first = ts.first();
        Integer ceil = ts.ceiling(15);    // 20

        System.out.println(ok + ", first=" + first + ", ceil=" + ceil);
    }
}
```

---

## 5. `Queue<E>` 常用方法（FIFO）

建议记三组对照：
- 插入：`add(e)`（失败抛异常） / `offer(e)`（失败返回 `false`）
- 取头不删：`element()`（空抛异常） / `peek()`（空返回 `null`）
- 取头并删：`remove()`（空抛异常） / `poll()`（空返回 `null`）

```java
import java.util.*;

public class QueueDemo {
    public static void main(String[] args) {
        Queue<String> q = new LinkedList<>();
        q.offer("A");
        q.offer("B");

        String head = q.peek();           // A
        String out1 = q.poll();           // A
        String out2 = q.poll();           // B
        String out3 = q.poll();           // null

        System.out.println(head + ", " + out1 + ", " + out2 + ", " + out3);
    }
}
```

---

## 6. `Deque<E>` 常用方法（栈 + 双端队列）

### 6.1 作为栈（LIFO）

- `push(e)`：入栈（头插）
- `pop()`：出栈（空抛异常）
- `peek()`：看栈顶（空返回 `null`）
- `pollFirst()`：安全出栈（空返回 `null`）

```java
import java.util.*;

public class StackByDequeDemo {
    public static void main(String[] args) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);

        int top = stack.peek();           // 2
        int a = stack.pop();              // 2
        Integer b = stack.pollFirst();    // 1
        Integer c = stack.pollFirst();    // null

        System.out.println(top + ", " + a + ", " + b + ", " + c);
    }
}
```

### 6.2 作为双端队列

- `offerFirst(e)` / `offerLast(e)`
- `peekFirst()` / `peekLast()`
- `pollFirst()` / `pollLast()`

```java
import java.util.*;

public class DequeDemo {
    public static void main(String[] args) {
        Deque<Integer> dq = new LinkedList<>();
        dq.offerFirst(10);                // [10]
        dq.offerLast(20);                 // [10, 20]

        Integer l = dq.peekFirst();       // 10
        Integer r = dq.peekLast();        // 20

        dq.pollFirst();
        dq.pollLast();

        System.out.println(l + ", " + r + ", empty=" + dq.isEmpty());
    }
}
```

---

## 7. `Map<K,V>` 常用方法（重点）

常用方法：
- `put(K,V)`：新增/覆盖
- `putIfAbsent(K,V)`：不存在才放
- `get(K)` / `getOrDefault(K, defaultV)`
- `containsKey(K)` / `containsValue(V)`
- `remove(K)` / `remove(K,V)`
- `replace(K,V)` / `replace(K, oldV, newV)`
- `keySet()` / `values()` / `entrySet()`
- `computeIfAbsent(K, mappingFunction)`：高频
- `merge(K, V, remappingFunction)`：计数高频

```java
import java.util.*;

public class MapDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("A", 2);                 // 覆盖
        map.putIfAbsent("A", 99);        // 不生效
        map.putIfAbsent("B", 3);

        int a = map.get("A");
        int c = map.getOrDefault("C", 0);

        map.computeIfAbsent("D", k -> 10);
        map.merge("B", 1, Integer::sum); // B: 3 -> 4

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println(e.getKey() + "=" + e.getValue());
        }

        System.out.println("A=" + a + ", C=" + c);
    }
}
```

---

## 8. 遍历方式（面试常问）

### 8.1 `Collection/List/Set` 遍历

```java
import java.util.*;

public class IterateCollectionDemo {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("A", "B", "C");

        // 1) 增强for
        for (String s : list) {
            System.out.println(s);
        }

        // 2) 迭代器
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        // 3) forEach + lambda
        list.forEach(System.out::println);
    }
}
```

### 8.2 `Map` 遍历

```java
import java.util.*;

public class IterateMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);

        // 推荐：entrySet
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue());
        }

        // keySet
        for (String k : map.keySet()) {
            System.out.println(k + ":" + map.get(k));
        }

        // lambda
        map.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
```

---

## 9. `Collections` 工具类常用方法

常用方法：
- `Collections.sort(list)`
- `Collections.reverse(list)`
- `Collections.shuffle(list)`
- `Collections.max(list)` / `Collections.min(list)`
- `Collections.binarySearch(list, key)`（前提：已排序）
- `Collections.frequency(c, o)`
- `Collections.swap(list, i, j)`
- `Collections.fill(list, obj)`

```java
import java.util.*;

public class CollectionsUtilDemo {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 2, 2));

        Collections.sort(list);           // [1,2,2,3]
        Collections.reverse(list);        // [3,2,2,1]
        Collections.shuffle(list);        // 随机顺序

        int max = Collections.max(list);
        int freq = Collections.frequency(list, 2);

        Collections.sort(list);
        int pos = Collections.binarySearch(list, 2);

        System.out.println("max=" + max + ", freq=" + freq + ", pos=" + pos);
    }
}
```

---

## 10. 高频模板（建议背）

### 10.1 词频统计

```java
Map<String, Integer> cnt = new HashMap<>();
for (String s : arr) {
    cnt.put(s, cnt.getOrDefault(s, 0) + 1);
}
```

### 10.2 分组

```java
Map<String, List<String>> group = new HashMap<>();
for (String item : items) {
    String key = item.substring(0, 1);
    group.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
}
```

### 10.3 去重并保持插入顺序

```java
List<Integer> unique = new ArrayList<>(new LinkedHashSet<>(nums));
```

### 10.4 栈（推荐）

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);
int x = stack.pop();
```

---

## 11. 易错点速记

- `Map` 不属于 `Collection`，但属于集合框架。
- `remove(int)` 与 `remove(Object)` 在 `List` 中重载不同，注意装箱：`remove(Integer.valueOf(1))`。
- `subList(from, to)` 是左闭右开，且通常是原列表视图。
- 需要有序去重：用 `LinkedHashSet`，不是 `HashSet`。
- 栈优先用 `Deque`（`ArrayDeque`），不推荐老的 `Stack`。
- `Queue`/`Deque` 记住“异常版 vs 安全版”：`remove/pop/element` 与 `poll/peek`。

---

## 12. 30秒口述模板（面试）

Java 集合框架我通常分成 `Collection` 和 `Map` 两大体系。`Collection` 下有 `List`、`Set`、`Queue/Deque`，分别解决有序可重复、去重、队列/栈问题。实战里 `List` 常用 `ArrayList`，去重常用 `HashSet`/`LinkedHashSet`，栈和队列用 `Deque`，键值存储用 `HashMap`。高频 API 我重点掌握 `add/remove/contains/getOrDefault/computeIfAbsent/merge`，并熟悉异常版与安全版方法差异。
