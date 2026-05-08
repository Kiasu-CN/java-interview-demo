# Java `Character` 工具类速记（面试/算法版）

目标：掌握字符串与字符题里最常用的 `Character` 方法，做到看到题就能直接选 API。

---

## 1. 最常用方法（先背这几个）

- `Character.isLetter(char c)`：是否字母
- `Character.isDigit(char c)`：是否数字
- `Character.isLetterOrDigit(char c)`：是否字母或数字
- `Character.toLowerCase(char c)`：转小写字符
- `Character.toUpperCase(char c)`：转大写字符
- `Character.isWhitespace(char c)`：是否空白字符

---

## 2. 方法 + 代码演示

### 2.1 `isLetter(c)`

```java
char a = 'A';
char b = '9';
System.out.println(Character.isLetter(a)); // true
System.out.println(Character.isLetter(b)); // false
```

### 2.2 `isDigit(c)`

```java
char a = '8';
char b = 'x';
System.out.println(Character.isDigit(a)); // true
System.out.println(Character.isDigit(b)); // false
```

### 2.3 `isLetterOrDigit(c)`

```java
char a = 'Z';
char b = '3';
char c = '#';
System.out.println(Character.isLetterOrDigit(a)); // true
System.out.println(Character.isLetterOrDigit(b)); // true
System.out.println(Character.isLetterOrDigit(c)); // false
```

### 2.4 `toLowerCase(c)` / `toUpperCase(c)`

```java
char a = 'A';
char b = 'm';
System.out.println(Character.toLowerCase(a)); // a
System.out.println(Character.toUpperCase(b)); // M
```

### 2.5 `isWhitespace(c)`

```java
char a = ' ';
char b = '\n';
char c = 'A';
System.out.println(Character.isWhitespace(a)); // true
System.out.println(Character.isWhitespace(b)); // true
System.out.println(Character.isWhitespace(c)); // false
```

---

## 3. 题目中的典型用法

### 3.1 LC125 验证回文串（过滤 + 忽略大小写）

```java
public boolean isPalindrome(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) {
        while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
        while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;

        if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
            return false;
        }
        l++;
        r--;
    }
    return true;
}
```

---

## 4. 易错点

- `Character.toLowerCase(c)` 处理的是“单个字符”，不是字符串。
- `isLetterOrDigit(c)` 可以保留数字，别误用 `isLetter(c)` 导致数字被过滤。
- 双指针跳过非法字符时要带上 `l < r`，防止越界。

---

## 5. 30秒记忆卡

- 过滤合法字符：`Character.isLetterOrDigit(c)`
- 忽略大小写比较：`Character.toLowerCase(c1) == Character.toLowerCase(c2)`
- 判断纯数字字符：`Character.isDigit(c)`

---

## 6. 微练习（每天2分钟）

1. 判断 `'_'` 是否 `isLetterOrDigit`。
2. 把 `'G'` 转小写。
3. 写一个方法统计字符串中字母个数（用 `isLetter`）。
4. 写一个方法统计字符串中数字个数（用 `isDigit`）。
5. 改写 LC125：只允许字母参与回文判断（不用数字）。
