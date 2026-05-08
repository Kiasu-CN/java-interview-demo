# Java面试完全指南（会背会用版）

定位：把“会背八股”变成“会写代码 + 会做选型 + 会面试表达”。
使用方式：每个知识点按 3 步走：
1. 先手写代码。
2. 再讲原理。
3. 最后说业务场景。

---

## 一、两份文档如何配合

1. 本文负责“会用”：代码、方法、场景。
2. 《问答版》负责“会答”：结构化口述。
3. 每个点都要形成闭环：
- 一段代码
- 一组方法名
- 一段60秒口述

---

## 二、集合模块（代码优先）

## 2.1 List：ArrayList / LinkedList

### 代码模板
```java
import java.util.*;

public class ListDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add(1, "X");
        System.out.println(list); // [A, X, B]

        list.set(0, "Z");
        System.out.println(list.get(1));
        System.out.println(list.subList(0, 2)); // [Z, X]

        list.remove("X");
        list.remove(0);

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        System.out.println(stack.pop());  // 2
        System.out.println(stack.peek()); // 1

        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(10);
        queue.offer(20);
        System.out.println(queue.poll()); // 10
        System.out.println(queue.peek()); // 20
    }
}
```

### 必须掌握的方法
1. `add/get/set/remove/subList`
2. `push/pop/peek`（栈）
3. `offer/poll/peek`（队列）

### 面试要点
1. ArrayList 读多写少场景优先。
2. LinkedList 更适合头尾操作或当 Deque 用。

---

## 2.2 HashMap：词频、分组、TopK

### 代码模板1：词频统计
```java
Map<String, Integer> freq = new HashMap<>();
String[] words = {"java", "redis", "java", "mysql"};
for (String w : words) {
    freq.merge(w, 1, Integer::sum);
}
System.out.println(freq); // {java=2, redis=1, mysql=1}
```

### 代码模板2：分组统计
```java
Map<String, List<String>> group = new HashMap<>();
String[][] data = {{"dev", "alice"}, {"dev", "bob"}, {"qa", "tom"}};
for (String[] row : data) {
    group.computeIfAbsent(row[0], k -> new ArrayList<>()).add(row[1]);
}
System.out.println(group);
```

### 代码模板3：TopK 高频
```java
Map<Integer, Integer> freq = new HashMap<>();
int[] nums = {1,1,1,2,2,3};
for (int n : nums) freq.merge(n, 1, Integer::sum);

PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(freq::get));
int k = 2;
for (int n : freq.keySet()) {
    pq.offer(n);
    if (pq.size() > k) pq.poll();
}
System.out.println(pq); // topK
```

### 必须掌握的方法
1. `put/get/getOrDefault`
2. `containsKey/remove`
3. `merge/computeIfAbsent`
4. `entrySet` 遍历

### 面试要点
1. `HashMap` JDK8：数组+链表+红黑树。
2. 扩容、冲突、2的幂必须讲清。

---

## 2.3 Set：去重与有序

### 代码模板
```java
Set<Integer> s1 = new HashSet<>(Arrays.asList(3, 1, 2, 2, 1));
System.out.println(s1); // 无序去重

Set<Integer> s2 = new LinkedHashSet<>(Arrays.asList(3, 1, 2, 2, 1));
System.out.println(s2); // 保插入顺序

Set<Integer> s3 = new TreeSet<>(Arrays.asList(3, 1, 2, 2, 1));
System.out.println(s3); // 自动排序
```

### 必须掌握的方法
1. `add/remove/contains`
2. `first/last`（TreeSet）
3. `ceiling/floor`（有序集合）

### 面试要点
1. 去重依赖 `equals/hashCode`。
2. TreeSet 去重基于比较结果是否为 0。

---

## 三、并发模块（必须手写）

## 3.1 线程池

### 代码模板
```java
import java.util.concurrent.*;

ThreadPoolExecutor pool = new ThreadPoolExecutor(
        4,
        8,
        60,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(100),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.CallerRunsPolicy()
);

for (int i = 0; i < 10; i++) {
    int id = i;
    pool.execute(() -> System.out.println("task:" + id + ", " + Thread.currentThread().getName()));
}
pool.shutdown();
```

### 必须掌握的方法/参数
1. `execute/submit/shutdown`
2. `corePoolSize/maxPoolSize/workQueue/handler`
3. 四种拒绝策略

### 面试要点
1. 不建议 `Executors.newFixedThreadPool` 直接无脑用。
2. 参数配置要结合 IO/CPU 场景。

---

## 3.2 锁与原子类

### 代码模板
```java
import java.util.concurrent.atomic.AtomicInteger;

class CounterDemo {
    private int count1 = 0;
    private final AtomicInteger count2 = new AtomicInteger(0);

    public synchronized void incSync() {
        count1++;
    }

    public void incAtomic() {
        count2.incrementAndGet();
    }
}
```

### volatile 演示
```java
class FlagDemo {
    volatile boolean running = true;

    void stop() { running = false; }
}
```

### 面试要点
1. `volatile` 不能保证 `i++` 原子性。
2. CAS 适合低冲突更新，冲突高时要考虑锁。

---

## 四、Spring模块（面试高频落地代码）

## 4.1 全局异常处理
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handle(Exception e) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 500);
        resp.put("msg", e.getMessage());
        return resp;
    }
}
```

## 4.2 参数校验
```java
public class UserReq {
    @NotBlank
    private String name;
    @Min(1)
    private Integer age;
}

@PostMapping("/user")
public String create(@Valid @RequestBody UserReq req) {
    return "ok";
}
```

## 4.3 事务示例
```java
@Service
public class OrderService {
    @Transactional(rollbackFor = Exception.class)
    public void createOrder() {
        // db操作1
        // db操作2
        // throw new RuntimeException("error"); // 触发回滚
    }
}
```

### 面试要点
1. 事务失效场景：同类自调用、异常被吞、方法不在代理范围。
2. AOP 在日志、鉴权、耗时统计中常用。

---

## 五、MySQL模块（会背要落到SQL）

## 5.1 高频 SQL + Explain

```sql
-- 联合索引: idx_user_status_time(status, create_time)
EXPLAIN
SELECT id, name
FROM user
WHERE status = 1
  AND create_time >= '2026-01-01'
ORDER BY create_time DESC
LIMIT 20;
```

### 必须掌握
1. 看 `type/key/rows/extra`。
2. 识别回表、覆盖索引、索引失效。
3. 知道联合索引最左前缀。

### 面试要点
1. 先定位慢SQL，再改索引，再回归验证。
2. 不要先拍脑袋加索引。

---

## 六、Redis模块（会背要能防事故）

## 6.1 缓存旁路代码
```java
public String queryUser(Long id) {
    String key = "user:" + id;
    String cache = redis.get(key);
    if (cache != null) return cache;

    String db = userMapper.selectById(id);
    if (db == null) {
        redis.setex(key, 60, "NULL"); // 防穿透
        return null;
    }

    redis.setex(key, 300 + ThreadLocalRandom.current().nextInt(60), db); // 过期打散
    return db;
}
```

## 6.2 分布式锁基础
```java
String lockKey = "lock:order:" + orderId;
boolean ok = redis.setIfAbsent(lockKey, requestId, 30); // SET NX EX
if (!ok) return;
try {
    // 业务逻辑
} finally {
    // 生产建议用 Lua 校验 requestId 再删，避免误删别人的锁
    redis.del(lockKey);
}
```

### 面试要点
1. 穿透/击穿/雪崩方案要带副作用说明。
2. 分布式锁要说明误删与续期问题。

---

## 七、项目表达（把技术说成人话）

每个亮点用这个模板：
1. 问题：比如某接口 P95 响应 1.2s。
2. 动作：缓存 + 索引优化 + 线程池隔离。
3. 结果：P95 降到 300ms。
4. 代价：一致性窗口、缓存成本、复杂度提升。

---

## 八、7天执行版（每天2小时）

Day1：List/Set 代码模板全部手写一遍。
Day2：HashMap 三个模板（词频/分组/TopK）。
Day3：线程池 + synchronized/atomic/volatile。
Day4：Spring 三段代码（异常/校验/事务）。
Day5：写3条 SQL，逐条 Explain。
Day6：缓存旁路 + 分布式锁代码。
Day7：结合《问答版》做 10 题口述。

---

## 九、自测标准

1. 能脱稿写出 HashMap 词频、Deque 栈队列、线程池模板。
2. 能解释每段代码用到的方法和复杂度。
3. 能把一段代码对应到一个项目场景。
4. 能用60秒说清“结论-原理-场景-边界”。

达到以上4点，就不只是“会背”，而是“可面试可落地”。

---

## 十、建议补充（复试常追问）

下面 4 块是你当前文档最值得补齐的内容，补完后覆盖会明显完整。

## 10.1 MyBatis 动态SQL与安全参数

### 代码模板
```java
@Mapper
public interface UserMapper {
    @Select({
        "<script>",
        "SELECT id, name, status FROM user",
        "<where>",
        "  <if test='name != null and name != \"\"'>",
        "    AND name = #{name}",
        "  </if>",
        "  <if test='status != null'>",
        "    AND status = #{status}",
        "  </if>",
        "</where>",
        "ORDER BY id DESC",
        "</script>"
    })
    List<User> query(@Param(\"name\") String name, @Param(\"status\") Integer status);
}
```

### 必须掌握
1. `#{}` 预编译防注入，`${}` 字符串拼接有风险。
2. `<if>/<where>/<foreach>` 常用动态SQL标签。
3. Mapper 参数绑定与返回映射。

---

## 10.2 JWT + 拦截器鉴权链路

### 代码模板
```java
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) return false;
        // 伪代码：验证签名、过期时间、业务claim
        // JwtUtil.verify(token);
        return true;
    }
}
```

### 必须掌握
1. Header 读取、token 校验、异常返回。
2. 过期刷新策略（短token+刷新token）。
3. 拦截器与过滤器差异。

---

## 10.3 并发工具类（CountDownLatch / CompletableFuture）

### 代码模板
```java
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            // do work
        } finally {
            latch.countDown();
        }
    }).start();
}
latch.await();
```

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
String result = f1.thenCombine(f2, (a, b) -> a + b).join();
```

### 必须掌握
1. 并发等待、任务编排、异常处理。
2. `join/get` 区别与超时控制。

---

## 10.4 测试与排查闭环（面试加分项）

### 代码模板（JUnit）
```java
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void should_query_user() {
        User user = userService.queryById(1L);
        assertNotNull(user);
    }
}
```

### 必须掌握
1. 单测目标：关键路径、边界条件、异常分支。
2. 排查链路：日志 -> 指标 -> SQL Explain -> 线程栈/堆信息。
3. 面试表达：不仅“怎么写”，还要“怎么验证”和“怎么定位问题”。
