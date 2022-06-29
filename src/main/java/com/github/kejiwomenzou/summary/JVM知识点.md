# JVM知识点

## JVM类加载子系统

* **类加载机制**

    通过一个**类的全限定名**来获取定义此类的**二进制字节流**，将这个字节流所代表的**静态存储结构**转化为**方法区**的运行时数据结构。 在**内存中**生成一个代表该类的`java.lang.Class`对象，作为**方法区该类的各种数据结构的访问入口**。
  * 加载(Loading)
  
  * 链接(Linking)
    * 验证(Verification)
      * 验证.class文件字节流正确性：文件格式、元数据、字节码、符号引用验证
    * 准备(Preparation)
      * 为**静态（类）变量**分配初始值（注意什么是静态变量，不包括**常量**， 类变量在方法区中）
      * jdk6运行时常量、静态变量、字符串常量池在方法区(Perm), jdk1.7/1.8运行时常量在方法区(MetaSpace)，静态变量、字符串常量池在堆中
    * 解析(Resolution)
      * 将常量池内的符号引用转化为直接引用（解析一般在JVM执行完**初始化**操作后才执行）
  
  * 初始化(Initialization)
    * 有<cinit>方法执行<cinit>方法，也就是把静态变量和静态代码块合并执行， **执行顺序为定义顺序**；注意：父类有<cinit>先执行父类的
    * 强调一下**成员变量**的初始化时间：java new关键字用来**分配对象空间并对其做默认初始化**，默认初始化会将对象的所有成员字段设到其类型对应的默认值（零值）。
而**构造器**用来初始化对象，执行构造方法中的代码，当然默认隐含了调用super父类构造。

* **运行时数据区**
  * **堆**
    
    * 所有线程共享（线程不安全）、堆大小可以调节、逻辑上是连续的内存空间、在堆上可以划分线程私有的缓冲区（TLAB：Thread Local Allocation Buffer），栈上的对象引用指向堆上的对象实例。
    * **几乎（JIT逃逸分析能让对象栈上分配）所有**的对象实例和数组都分配在堆上，堆是GC的重点区域。
    
    * 现代垃圾回收器大部分基于堆分代收集理论设计，所以堆空间在被在逻辑上被分代：
      * jdk1.7及之前 : 
        * 新生代（Young Generation Space，又叫Young区） = Eden区 + Survivor区（from + to两部分 或 叫S0和S1区）
        * 老年代（Tenure Generation Space，又叫Old/Tenure区） 
        * **永久代（Permanent Space，又叫Perm区）**
      * jdk1.8及之后 :
        * 新生代（Young Generation Space，又叫Young/New区） = Eden区 + Survivor区（from + to两部分 或 叫S0和S1区）
        * 老年代（Tenure Generation Space，又叫Old/Tenure区）
        * **元空间（Meta Space，又叫Meta）**
    
    * 堆区会产生OOM错误（Out Of Memory）
    
    * 堆的一些基本参数设置：
      * -Xms 256m（或-XX:InitialHeapSize 256m）堆起始大小256m。 默认： 物理内存大小/64
      * -Xmx 256m（或-XX:MaxHeapSize 256m）堆区最大内存256m。默认： 物理内存大小/4
      * -XX:NewRatio = 4， 表示新生代占1份，老年代占4份， 新生代占整个堆的1/5, 老年占整个堆的4/5
      * -XX:SurvivorRatio = 8， 表示新生代中 Eden:from:to = 8:1:1，默认是8:1:1
      * -Xmn 1G 可以直接指定新生代最大内存大小，一般为默认值不设置
      * -XX:MaxTenuringThreshold = <N> 表示 Survivor区中的对象经历多少次GC后会进入老年代
    
    * 对象分配过程：
      * 1.新创建的对象会入到堆中的Eden区
      * 2.Eden区大小不够时再创建新对象进来时，会进行垃圾回收（Minor GC/Young GC），未回收的对象进S0区，新进行来的对象依旧分配到Eden区
      * 3.S0和S1复制是交替的，一般GC复制后S0和S1哪个区域空哪个区域就是to区。
      * 4.对象在新生代经历过-XX:MaxTenuringThreshold = <N>次GC后未被回收直接进入老年代（还有一种情况是对象过大直接进入老年代，放不下触发Old GC）
      * 5.老年代不足时先触发老年代GC（Major GC/Old GC），GC后不足以把对象入到老年代则OOM。 
  
* **方法区**
    
  * 就HotSpot而言，方法区叫做非堆（Non-Heap），是独立于Java堆的内存空间，该区域**所有线程共享**，空间区域大小可通过参数配置。
  
  * **方法区的内部结构**：    
    * 类型信息（如类Class、接口Interface、枚举Enum、注解Annotation类）： 包括修饰符、直接父类的有效名称、全限定名、类的直接接口组成的列表
    * 域信息： 包括域名称、域类型、域修饰符
    * 方法信息：包括名称、返回值、参数个数及类型、修饰符、方法字节码、操作数栈、局部变量表及大小、异常表
    * 编译器编译后的代码缓存
    * 全局常量：被static final修饰且在编译期就确定了的值
    * 静态变量

  * 不同版本的方法区：
    * jdk1.7及以前： **永久代（Permanent Generation）**
      * -XX:PermSize=<N> 初始永久代空间大小设置，默认20.75m
      * -XX:MaxPermSize=<N> 永久代最大空间大小设置， 32位默认64m， 64位置默认82m
        
    * jdk1.8及以后： **元空间（Meta Space）**，元空间使用本地内存。
      * -XX:MetaSpaceSize=<N> 初始元空间大小设置
      * -XX:MaxMetaSpaceSize=<N> 最大元空间大小设置
      * 默认值依赖于平台，不指定大小元空间可能会耗尽系统内存，触发OOM。-XX:MetaSpaceSize有可重置的水位线，超过水位线会触发Full GC。
    
  * HotSpot方法区的变化：
    * jdk1.6及以前：叫永久代（Permanent Generation）， **运行时常量池**（**字符串常量池StringTable运行时常量池中**） 和 **静态变量**在永久代中
    * jdk1.7：叫永久代（Permanent Generation），**运行时常量池**在永久代中， **静态变量**和**字符串常量池StringTable**在**堆**中
    * jdk1.8：叫元空间（Meta Space），使用**本地内存**，**运行时常量池**在元空间中，**静态变量**和**字符串常量池StringTable**在**堆**中

  * 方法区的垃圾回收：
    * 主要回收**常量池中不被引用的常量**和**类型信息**（可以通过-Xnoclassgc来控制），类型信息回收很严格： 该类的对象没了 + 加载该类的类加载器没了 + 该的`java.lang.Class`对象没了。
    * 注：类加载和卸载可以用 -XX:+TraceClassLoading 和 -XX:-TraceClassUnLoading来观察
 
* **本地方法区**
* **程序计数器**
* **虚拟机栈**