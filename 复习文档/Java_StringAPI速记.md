# Java `String` API速记（面试/算法版）

目标：掌握字符串题与业务代码里最高频的 `String` API，并能快速选择正确方法。

---

## 1. 高频方法总览（建议优先背）

- 长度/判空：`length()`、`isEmpty()`
- 取字符：`charAt(int index)`
- 大小写：`toLowerCase()`、`toUpperCase()`
- 比较：`equals()`、`equalsIgnoreCase()`
- 查找：`contains()`、`indexOf()`、`lastIndexOf()`
- 截取：`substring(begin)`、`substring(begin, end)`
- 替换：`replace(old, new)`、`replaceAll(regex, new)`
- 分割：`split(regex)`
- 去空白：`trim()`、`strip()`（JDK11+）
- 前后缀：`startsWith()`、`endsWith()`
- 转换：`toCharArray()`、`String.valueOf(...)`

---

## 2. 方法 + 代码演示

### 2.1 `length()` / `isEmpty()`

```java
String s = "abc";
System.out.println(s.length());   // 3
System.out.println(s.isEmpty());  // false
```

### 2.2 `charAt(index)`

```java
String s = "Java";
char c = s.charAt(1);
System.out.println(c); // a
```

### 2.3 `toLowerCase()` / `toUpperCase()`

```java
String s = "AbC";
System.out.println(s.toLowerCase()); // abc
System.out.println(s.toUpperCase()); // ABC
```

### 2.4 `equals()` / `equalsIgnoreCase()`

```java
String a = "Java";
String b = "java";
System.out.println(a.equals(b));           // false
System.out.println(a.equalsIgnoreCase(b)); // true
```

### 2.5 `contains()` / `indexOf()`

```java
String s = "hello world";
System.out.println(s.contains("world")); // true
System.out.println(s.indexOf("lo"));     // 3
System.out.println(s.indexOf("xx"));     // -1
```

### 2.6 `substring()`

```java
String s = "abcdef";
System.out.println(s.substring(2));    // cdef
System.out.println(s.substring(2, 5)); // cde  左闭右开
```

### 2.7 `replace()` / `replaceAll()`

```java
String s = "a-b-c";
System.out.println(s.replace("-", ":"));      // a:b:c
System.out.println(s.replaceAll("-", ":"));   // a:b:c
```

### 2.8 `split(regex)`

```java
String s = "java,go,python";
String[] arr = s.split(",");
System.out.println(arr[0]); // java
```

### 2.9 `trim()`

```java
String s = "  hello  ";
System.out.println("[" + s.trim() + "]"); // [hello]
```

### 2.10 `startsWith()` / `endsWith()`

```java
String file = "resume.pdf";
System.out.println(file.startsWith("res")); // true
System.out.println(file.endsWith(".pdf"));  // true
```

### 2.11 `toCharArray()` / `String.valueOf()`

```java
String s = "abc";
char[] cs = s.toCharArray();
System.out.println(cs[1]); // b

int x = 123;
String sx = String.valueOf(x);
System.out.println(sx);    // "123"
```

---

## 3. 算法题典型组合

### 3.1 回文串

- `charAt`
- `Character.isLetterOrDigit`
- `Character.toLowerCase`

### 3.2 最长无重复子串

- `charAt`
- `HashMap<Character, Integer>`

### 3.3 字符串清洗

- `trim`
- `toLowerCase`
- `replace` / `replaceAll`

---

## 4. 易错点

- `substring(begin, end)` 是左闭右开。
- `replaceAll` 的第一个参数是正则，普通替换优先用 `replace`。
- `String` 不可变，每次修改都会产生新字符串。
- 比较字符串内容要用 `equals`，不要用 `==`。

---

## 5. 30秒记忆卡

- 取长度：`length()`
- 取字符：`charAt(i)`
- 判断包含：`contains(str)`
- 截取：`substring(l, r)`（左闭右开）
- 忽略大小写比较：`equalsIgnoreCase`
- 普通替换：`replace`
- 正则替换：`replaceAll`

---

## 6. 微练习（每天3分钟）

1. 把 `"  AbC  "` 变成 `"abc"`。
2. 统计字符串里字符 `'a'` 出现次数。
3. 提取邮箱中 `@` 前的用户名。
4. 把 `"a,b,c"` 切分成数组并遍历输出。
5. 判断文件名是否为 `.jpg` 或 `.png` 结尾。
