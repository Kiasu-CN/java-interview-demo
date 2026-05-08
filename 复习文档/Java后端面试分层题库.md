# Java后端面试分层题库（独立版）

使用说明：
1. 每个模块按 `必会 -> 高频追问 -> 加分项` 顺序刷。
2. 每题建议 60-90 秒口述。
3. 先保证“必会”全覆盖，再补“高频追问”，最后冲“加分项”。

---

## 1) Java基础

### 必会（15）
1. `==` 和 `equals` 区别
2. `hashCode` 作用与契约
3. String 三兄弟区别
4. 重载 vs 重写
5. final / finally / finalize
6. 接口 vs 抽象类
7. 异常体系（受检/非受检）
8. throw vs throws
9. try-catch-finally 执行顺序
10. 自动装箱拆箱坑点
11. 深拷贝 vs 浅拷贝
12. 泛型作用
13. 类型擦除
14. Optional 常见用法
15. Lambda 基础

### 高频追问（8）
1. 为什么 String 不可变
2. finally 不执行的场景
3. `List<? extends T>` 与 `List<? super T>`
4. 反射优缺点与使用场景
5. 序列化风险
6. transient/static 对序列化影响
7. Stream 中间操作与终止操作
8. map 与 flatMap

### 加分项（5）
1. 方法引用四种形式
2. 函数式接口设计
3. SPI 机制
4. 注解元注解与使用
5. Java 新版本常用增强

---

## 2) 集合

### 必会（16）
1. 集合体系结构
2. ArrayList vs LinkedList
3. ArrayList 扩容机制
4. HashMap 结构
5. HashMap put 流程
6. HashMap 为什么容量常用 2 的幂
7. HashMap 冲突处理
8. HashSet 去重
9. TreeMap/TreeSet 有序原理
10. LinkedHashMap 特点
11. fail-fast/fail-safe
12. ConcurrentModificationException
13. 迭代安全删除
14. PriorityQueue 原理与复杂度
15. 小顶堆/大顶堆
16. 集合选型思路

### 高频追问（8）
1. HashMap 树化条件
2. HashMap 扩容重排过程
3. 为什么 ArrayList 常用
4. RandomAccess 作用
5. ArrayBlockingQueue vs LinkedBlockingQueue
6. CopyOnWriteArrayList 场景
7. LinkedHashMap LRU 思路
8. 可变对象作为 HashMap key 风险

### 加分项（5）
1. TreeMap 范围查询场景
2. PriorityQueue TopK 模板
3. 集合与内存开销权衡
4. 并发容器选型
5. 项目中的集合性能优化

---

## 3) 并发

### 必会（14）
1. 线程创建方式
2. 线程状态
3. synchronized 原理
4. synchronized vs ReentrantLock
5. volatile 语义
6. CAS 原理
7. ABA 问题
8. AQS 核心思想
9. ConcurrentHashMap 线程安全
10. ThreadLocal 原理
11. 线程池核心参数
12. 拒绝策略
13. Executors 风险
14. CountDownLatch/CyclicBarrier/Semaphore 区别

### 高频追问（8）
1. 锁升级
2. ThreadLocal 内存泄漏排查
3. 线程池参数估算
4. 死锁定位
5. 可见性/原子性/有序性联系
6. CompletableFuture 基础编排
7. ReentrantLock 公平锁性能取舍
8. 并发容器与同步包装类区别

### 加分项（5）
1. LongAdder vs AtomicLong
2. ForkJoinPool 适用场景
3. 无锁编程边界
4. 并发压测思路
5. 线上并发问题案例复盘

---

## 4) JVM

### 必会（10）
1. 运行时内存区域
2. 对象创建流程
3. 可达性分析
4. GC 算法
5. 常见回收器（CMS/G1）
6. 类加载过程
7. 双亲委派
8. OOM 类型
9. Full GC 排查
10. 常用调优参数

### 高频追问（6）
1. 新生代/老年代晋升
2. 安全点与停顿
3. GC 日志阅读
4. 元空间溢出原因
5. 堆外内存相关问题
6. jstack/jmap 使用路径

### 加分项（4）
1. MAT 分析步骤
2. JVM 参数调优案例
3. 类加载隔离场景
4. 线上内存泄漏复盘

---

## 5) Spring / Spring Boot / MyBatis

### 必会（16）
1. IOC/DI
2. AOP 原理
3. Bean 生命周期
4. 事务传播行为
5. 事务失效场景
6. 自动配置原理
7. @SpringBootApplication 组成
8. 拦截器/过滤器/AOP 区别
9. 参数校验
10. 全局异常处理
11. 循环依赖
12. #{} vs ${}
13. MyBatis 执行流程
14. 一级/二级缓存
15. 动态 SQL 标签
16. JWT 鉴权链路

### 高频追问（8）
1. 同类自调用失效根因
2. rollbackFor 边界
3. 代理机制差异
4. 条件装配常见注解
5. BeanFactory vs ApplicationContext
6. Mapper 参数绑定细节
7. 幂等方案落地
8. Spring 工程化治理（日志/链路/异常）

### 加分项（5）
1. 自定义 starter 思路
2. 多数据源事务处理
3. Spring 事件机制
4. MyBatis 插件机制
5. 项目可观测性设计

---

## 6) MySQL

### 必会（14）
1. B+树原因
2. 聚簇索引/二级索引
3. 回表/覆盖索引
4. 联合索引最左前缀
5. 索引失效场景
6. ACID
7. 隔离级别
8. MVCC
9. 快照读/当前读
10. 行锁/间隙锁/Next-Key
11. Explain 字段
12. 慢 SQL 优化流程
13. 深分页优化
14. 死锁定位

### 高频追问（8）
1. count(*) 优化
2. 大表加索引风险
3. 主从复制原理
4. RR 与幻读
5. 自增主键与UUID取舍
6. 事务长时间持有风险
7. 索引选择性评估
8. SQL 改写案例

### 加分项（5）
1. 分库分表触发条件
2. 在线 DDL 风险控制
3. 读写分离一致性问题
4. 慢查询治理体系
5. 数据归档策略

---

## 7) Redis

### 必会（12）
1. 五大结构与场景
2. 穿透/击穿/雪崩
3. 各自方案与副作用
4. RDB/AOF
5. 过期策略/淘汰策略
6. 分布式锁要点
7. 误删锁避免
8. 锁续期
9. 主从/哨兵/集群区别
10. 缓存一致性方案
11. 大Key/热Key
12. Redis 为什么快

### 高频追问（7）
1. Lua 原子释放锁
2. 缓存降级策略
3. 双写一致性方案比较
4. 布隆过滤器落地
5. Pipeline 使用场景
6. 热点Key扩散
7. Redis 内存优化

### 加分项（4）
1. Redisson 常见特性
2. 多级缓存设计
3. 冷热数据分层
4. Redis 故障演练

---

## 8) 网络 / 安全 / 场景

### 必会（14）
1. HTTP vs HTTPS
2. TLS 核心流程
3. 三次握手四次挥手
4. Cookie/Session/JWT
5. XSS/CSRF/SQL注入防护
6. 幂等设计
7. 超卖防护
8. 缓存一致性选型
9. 消息重复消费
10. 熔断与降级
11. 限流算法
12. 性能抖动定位
13. 故障排查路径
14. 项目量化表达

### 高频追问（7）
1. JWT 撤销与续签
2. HTTPS 性能影响
3. 限流落地参数如何定
4. 消息积压处理
5. 项目瓶颈定位方法
6. 技术方案权衡怎么讲
7. 上线风险控制

### 加分项（5）
1. 灰度发布策略
2. 压测方案设计
3. SLA/SLO 指标设计
4. 复盘机制模板
5. 跨团队协作中的技术推进

---

## 建议打卡规则

1. 每天 12 题：8 必会 + 3 高频追问 + 1 加分项。
2. 每周至少 2 次模块混合快答（20题/次）。
3. 卡壳题进入错题本，72小时内二刷。
