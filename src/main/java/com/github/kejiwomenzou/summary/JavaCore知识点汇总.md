# 知识点汇总

## Java核心 
* **Object类的核心方法及作用**
  * `wait()`、`notify()`、`toString()`、`equals()`、`hashCode()`

* **ThreadLocal原理**
  * 关键点："线程封闭"、Thread、ThreadLocal、ThreadLocalMap关联关系、内存泄露、实现原理

* **从StringBuilder和StringBuffer*
  * 字符串常量池在和JDK版本中的差异

* **HashMap/ConcurrentHashMap实现细节**
  * 关键点1：散列查找冲突解决： "开放地址法"、"链地址法" 
  * 关键点2：HashMap内部结构，核心方法get/put实现原理、jdk1.7/1.8区别，线程不安全、hash优化
  * 关键点3：ConcurrentHashMap内部结构， jdk1.7/1.8区别、实现机制、hash优化

* **TreeMap/LinkedHashMap实现**
  * 关键点：内部结构、利用LinkedHashMap实现LRU
  
* **ArrayList/LinkedList/Vector/Stack原理**
  * 关键点1：线程不安全、ArrayList初始及扩容、fast-fail和fast-safe机制、LinkedList实现
  * 关键点2：数组和链表的区别： 数组随机访问、有效利用cpu缓存行，效率更佳、链表更新删除快

* **HashSet/LinkedHashSet/TreeSet实现原理**

* **Java异常体系**
  * 关键点1：Throwable接口，Exception和Error区别， 异常分类：CheckedException和UnCheckedException 
  * 关键点2：业务中异常的最佳实践

* Integer/Short/Long等类的缓存池
  * 非常规操作：反射操作时会直接改掉缓存池
  
## Java并发
* **理解什么是线程不安全，产生的根因是什么**
  * 关键点： 线程安全： 见《Java并发编程实战》，特性：原子性、可见性、有序性
  * 根因：指令乱序执行（"无关指令"分在不同流水线，提高cpu吞吐率）、层次化存储（cpu与高速缓存）
  * 线程安全解决办法： 加锁、复制数据

* **从线程安全开始聊Java并发编程关键知识点：**
  * 理解Java内存模型（Java Memory Model）《Java并发编程实战》16章
  * volatile语义，底层实现， 引申点：单例模式中的DCL/线程启动停止volatile开关， 读多写少的时候volatile比synchronized快，不需要刷新和置缓存无效
  * final关键字语义
  * synchronized使用、底层原理，jdk1.6之后锁的优化：无锁、偏向锁、轻量级锁、重量级锁升级过程
  * java对象内存布局：对象头（MarkWord + 类型指针 + 数组长度） + 实例数据 
    * 对象压缩提升性能，如使用AtomicXXXFieldUpdater替换AtomicXXX
  * 由对象压缩引申的cpu多层缓存及伪共享问题, java中使用变量填充或@Contended, 还有Disruptor框架
  * CAS技术：底层实现, 优点/缺点
  * happens-before原则及禁止指令重排序的内存屏障： load store(全能屏障), load load, store load, store sotre
  * cpu缓存一致性协议

* **Java并发框架基石：AQS, 原理及源码解析**
  * 关键点1： 了解AQS等待队列与条件队列（对比Synchronized底层实现中的WaitSet/EntryList实现）
  * 关键点2： 公平与非公平锁体现在哪一块
  
* **基于AQS框架的并发工具**：
  * ReentrantLock 可重入锁
  * CountDownLatch 
  * Semaphore 信号量， 实现原理引申到限流算法：令牌桶算法
  * CyclicBarrier 可以重复使用的barrier, 引申点：dubbo重试计数机制
  * ReadWriteLock 读写锁

* **线程池**
  * 关键点1：ThreadPoolExecutor参数含义，corePoolSize, maximumPoolSize,keepAliveTime/timeUnit,BlockingQueue,threadFactory,RejectedExecutionHandler
  * 关键点2：线程池原理/执行过程 
  * 关键点3：业务场景中如何设定参数：
    * 1.业界经验： cpu密集型 n+1, io密集型: 2*n
    * 2.压测，根据预估业务接口的QPS及机器配置来确定corePoolSize和maximumPoolSize， 顶得住并发调整corePoolSize，顶不住调整队列大小，
    * 3.队列最好使用队列，留好一定的阀值，做好监控，拒绝策略根据业务场景抛弃或日志持久化
  * 关键点4：`Executors.newFixedThreadPool(int nThreads)、newCachedThreadPool()、newScheduledThreadPool(int corePoolSize)、newSingleThreadExecutor()`

* **Thread类**
  * Thread线程状态及状态的转换： NEW、RUNNABLE、WAITING/TIME_WAITING、BLOCK、TERMINATED
  * Thread类中的主要方法
  * wait虚假唤醒