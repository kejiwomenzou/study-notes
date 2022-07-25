# 了解Dubbo SPI机制

> 本文Dubbo版本：2.7.16

### Dubbo SPI

Dubbo SPI可配置在如下三个目录任意一个，文件为SPI接口全限定名
* `META-INF/dubbo/internal/`
* `META-INF/dubbo/`
* `META-INF/services/` #这个兼容JDK SPI路径

### Dubbo SPI使用及@SPI注解

* **配置路径**：
  
  * `resources/META-INF/dubbo/com.github.kejiwomenzou.dubbo.spi.MultiTransport`文件
  
* **`MultiTransport`文件内容**：

  ```shell
  grizzly=com.github.kejiwomenzou.dubbo.spi.adaptive.GrizzlyTransport
  netty=com.github.kejiwomenzou.dubbo.spi.adaptive.NeetyTransport
  unknow=com.github.kejiwomenzou.dubbo.spi.adaptive.UnknowTransport
  ```
* **代码**：

  ```java
  @SPI("unknow")
  public interface MultiTransport {
      void echo(String name, URL url);
      void echo(String name);
  }
  //这里只列表出一个，对应的还有NeetyTransport和GrizzlyTransport类
  public class UnknowTransport implements MultiTransport {
  
      @Override
      public void echo(String name, URL url) {
          System.out.printf("Unknow echo: name={%s}, url={%s}", name, url);
      }
      @Override
      public void echo(String name) {
          System.out.printf("Unknow echo: name={%s}", name);
      }
  }
  public static void main(String[] args) {
      URL dubboUrl = URL.valueOf("test://localhost:9090");
      ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
      MultiTransport transport = loader.getDefaultExtension();
      //输出：Unknow echo: name={哈哈}, url={test://localhost:9090}
      transport.echo("哈哈", dubboUrl);
  }
  ```

* **Dubbo SPI 说明：**

  * SPI接口需要加上 `@SPI`注解， 对应的配置文件（文件以**接口全限定名**称命名），`@SPI("value")`注解中的值这里我暂且称作**spi name**, 该值可以为空。该值就是配置在文件中的 `key=value`中的`key`, 如：unknow=com.github.kejiwomenzou.dubbo.spi.adaptive.UnknowTransport`中的**spi name**为"mina"。
  * **@SPI注解在接口上就表明这个接口是一个SPI接口**，能通过Dubbo SPI机制加载并获取实例。
  * 通过`ExtensionLoader.getExtensionLoader(MultiTransport.class)`获得`ExtensionLoader`
  * `@SPI`有默认值可以通过`ExtensionLoader.getDefaultExtension()`获取接口实例， 否则需要通过`ExtensionLoader.getDefaultExtension(String name)`来获取对应的实例（这里入参name为**spi name**）。

### @Adaptive注解使用

通过上面已了解对应`@SPI`的基本流程，下面直接先上代码来看一些案例：（**使用上面列表列出代码和配置**）

```java
//代码：
@SPI
public interface MultiTransport {
    void echo(String name, URL url);
    void echo(String name);
}
public class UnknowTransport implements MultiTransport {
    @Override
    public void echo(String name, URL url) {
        System.out.printf("Unknow echo: name={%s}, url={%s}", name, url);
    }
    @Override
    public void echo(String name) {
        System.out.printf("Unknow echo: name={%s}", name);
    }
}
public class GrizzlyTransport implements MultiTransport {
    @Override
    public void echo(String name, URL url) {
        System.out.printf("Grizzly echo: name={%s}, url={%s}", name, url);
    }
    @Override
    public void echo(String name) {
        System.out.printf("Grizzly echo: name={%s}", name);
    }
}
public class NettyTransport implements MultiTransport {

    @Override
    public void echo(String name, URL url) {
        System.out.printf("Netty echo: name={%s}, url={%s}", name, url);
    }
    @Override
    public void echo(String name) {
        System.out.printf("Netty echo: name={%s}", name);
    }
}
```

根据如上代码和配置来看下面的case! 

* **情况1： 仅@Adaptive加在【接口实现类】上，且@SPI注解【无】默认值**
* **情况2： 仅@Adaptive加在【接口实现类】上，且@SPI注解【有】默认值， 输出同情况1**

```java
//@SPI 无默认值
@SPI("netty") //有默认值
public interface MultiTransport {
    void echo(String name, URL url);
    void echo(String name);
}
@Adaptive //注解加在实现类上
public class UnknowTransport implements MultiTransport {
    @Override
    public void echo(String name, URL url) {
        System.out.printf("Unknow echo: name={%s}, url={%s}", name, url);
    }
    @Override
    public void echo(String name) {
        System.out.printf("Unknow echo: name={%s}", name);
    }
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090");
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //情况1和2均输出（生成的不是代理类!）：com.github.kejiwomenzou.dubbo.spi.adaptive.UnknowTransport@123772c4
    System.out.println(transport);
    //情况1和2均输出： Unknow echo: name={哈哈}, url={test://localhost:9090}
    transport.echo("哈哈", dubboUrl);
}
```

* **情况3：仅@Adaptive加在【接口方法】上，且@SPI注解【有】默认值**

```java
@SPI("unknow")//有默认值
public interface MultiTransport {
    @Adaptive //注解加在接口方法上
    void echo(String name, URL url);
    void echo(String name);
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090");
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.MultiTransport$Adaptive@6a2bcfcb
    System.out.println(transport);
    //输出： Unknow echo: name={哈哈}, url={test://localhost:9090}
    transport.echo("哈哈", dubboUrl);
}
```

* **情况4： 仅@Adaptive加在【接口方法】上，且@SPI注解【无】默认值**

```java
@SPI //无默认值
public interface MultiTransport {
    @Adaptive //注解加在接口方法上
    void echo(String name, URL url);
    void echo(String name);
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090");
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.MultiTransport$Adaptive@66d2e7d9
    System.out.println(transport);
    //报错！！ 异常信息提示：Failed to get extension (com.github.kejiwomenzou.dubbo.spi.MultiTransport) name from url (test://localhost:9090) use keys([multi.transport])
    transport.echo("哈哈", dubboUrl);
}
```

* **情况5： 仅@Adaptive加在【接口方法】上，@SPI注解【无】默认值，但URL中有值**

```java
@SPI //无默认值
public interface MultiTransport {
    @Adaptive  //注解加在接口方法上
    void echo(String name, URL url);
    void echo(String name);
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090?multi.transport=grizzly");//注意URL中的参数
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.MultiTransport$Adaptive@66d2e7d9
    System.out.println(transport);
    //输出：Grizzly echo: name={哈哈}, url={test://localhost:9090?multi.transport=grizzly}
    transport.echo("哈哈", dubboUrl);
}
```

* **情况6： 仅@Adaptive加在【接口方法】上，@SPI注解【有】默认值，但URL中有值**

```java
@SPI("netty") //有默认值
public interface MultiTransport {
    @Adaptive  //注解加在接口方法上
    void echo(String name, URL url);
    void echo(String name);
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090?multi.transport=grizzly");//注意URL中的参数
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.MultiTransport$Adaptive@66d2e7d9
    System.out.println(transport);
    //输出：Grizzly echo: name={哈哈}, url={test://localhost:9090?multi.transport=grizzly}
    transport.echo("哈哈", dubboUrl);
}
```

* **情况7： 仅@Adaptive加在【接口实现类】上，@SPI注解【有】默认值，但URL中有值**

```java
@SPI("netty") //有默认值
public interface MultiTransport {
    void echo(String name, URL url);
    void echo(String name);
}
@Adaptive  //注解加在实现上
public class GrizzlyTransport implements MultiTransport {
    @Override
    public void echo(String name, URL url) {
        System.out.printf("Grizzly echo: name={%s}, url={%s}", name, url);
    }
    @Override
    public void echo(String name) {
        System.out.printf("Grizzly echo: name={%s}", name);
    }
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090?multi.transport=unknow");//注意URL中的参数
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.MultiTransport$Adaptive@66d2e7d9
    System.out.println(transport);
    //输出：Grizzly echo: name={哈哈}, url={test://localhost:9090?multi.transport=unknow}
    transport.echo("哈哈", dubboUrl);
}
```

* **情况8： @Adaptive加在【接口方法】上也加在【实现类】上，@SPI注解【有】默认值，但URL中有值**

```java
@SPI("netty") //有默认值
public interface MultiTransport {
    @Adaptive  //注解加在接口方法上
    void echo(String name, URL url);
    void echo(String name);
}
@Adaptive //注解也加在实现类上
public class GrizzlyTransport implements MultiTransport {
    @Override
    public void echo(String name, URL url) {
        System.out.printf("Grizzly echo: name={%s}, url={%s}", name, url);
    }
    @Override
    public void echo(String name) {
        System.out.printf("Grizzly echo: name={%s}", name);
    }
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090?multi.transport=unknow");//注意URL中的参数
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.adaptive.GrizzlyTransport@2d363fb3
    System.out.println(transport);
    //输出：Grizzly echo: name={哈哈}, url={test://localhost:9090?multi.transport=unknow}
    transport.echo("哈哈", dubboUrl);
}
```

* **情况9： @Adaptive只加在【接口方法】上且@Adaptive上有值，@SPI注解【有】默认值，但URL中有值**

```java
@SPI("netty") //有默认值
public interface MultiTransport {
    @Adaptive({"test"}) //注解加在接口方法上，且注解上有值
    void echo(String name, URL url);
    void echo(String name);
}
public static void main(String[] args) {
    URL dubboUrl = URL.valueOf("test://localhost:9090?test=unknow");//注意URL中的参数:test, 与@Adaptive中的对应
    ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
    MultiTransport transport = loader.getAdaptiveExtension();
    //输出（生成的是代理类）：com.github.kejiwomenzou.dubbo.spi.MultiTransport$Adaptive@66d2e7d9
    System.out.println(transport);
    //输出：Unknow echo: name={哈哈}, url={test://localhost:9090?test=unknow}
    transport.echo("哈哈", dubboUrl);
}
```

### @Adaptive注解总结

1. `URL`作为Dubbo的统一资源定位符贯穿整个Dubbo，使用@Adaptive在**接口方法**上时，方法必须要带`URL` 作为入参。
2. `@Adaptive`的作用就是为SPI接口提供一个自适应的类。
3. `ExtensionLoader.getAdaptiveExtension()`在寻找`@Adaptive`自适应类时，大致有两种情况：
   1. 只要SPI**实现类上注有`@Adaptive`注解**（如果接口方法也注有`@Adaptive`，依旧返回实现类），`ExtensionLoader.getAdaptiveExtension()`返回的就是这个类的实例。实现类注解了`@Adaptive`，优先级最高。
   2. 如果只有**接口方法上标注了`@Adaptive`**， 此时`ExtensionLoader.getAdaptiveExtension()`会生成后缀为$Adaptive的代理类。此时接口方法里必须要有 URL变量入参， 该方法中实现为：会解析URL地址中的`key=value`参数, `key`生成规则：是接口名 驼峰转小写用"."拼接（如: "multi.transport"）,再根据`value`值用 `ExtensionLoader.getExtension(name)`去找真正实现执行对应的方法。这也是为啥``ExtensionLoader`能根据`URL`来动态扩展。
4. `@Adaptive({"test"})`注解中的值与 URL中的key=value中的key对应，value对应的就是SPI name。
5. 可以同时在**接口方法**和**实现类**上加`@Adaptive`注解。优先级永远是生成**实现类**的实例，而不是生成代理类。
6. 不能在多个实现类上都加`@Adaptive`注解，会报错。

总的来说，**Dubbo要么直接用你自己注解了`@Adaptive`的实现类来作为接口的扩展类； 要么为接口（仅标注了`@Adaptive`注解在接口方法上）生成代理类， 然后在代理类的方法中通过解析URL中带的参数来动态选择指定扩展实现类。**



### @Activate注解使用

先看正面的代码案例，前置代码如下：

```java
@SPI
public interface Filter {
    void filter(String name, URL url);
}
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
public class BackListFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("BackListFilter" + " name : " + name + ", url: ");
    }
}
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
```

配置：

```shell
auth=com.github.kejiwomenzou.dubbo.spi.activate.AuthFilter
backList=com.github.kejiwomenzou.dubbo.spi.activate.BackListFilter
order=com.github.kejiwomenzou.dubbo.spi.activate.OrderFilter
```



* **情况1： 实现类上加@Activate**

```java
@Activate
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
@Activate
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}

public static void main(String[] args) {
    ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
    URL url = URL.valueOf("test://localhost:8088");
    List<Filter> list = loader.getActivateExtension(url, new String[]{});
    //输出: [com.github.kejiwomenzou.dubbo.spi.activate.AuthFilter@782830e,com.github.kejiwomenzou.dubbo.spi.activate.OrderFilter@470e2030]
    System.out.println(list);
}
```

* **情况2： 实现类上加@Activate(group="xxx"), 带group参数**

```java
@Activate(group={"auth","order"})
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
@Activate(group={"order"})
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
public static void main(String[] args) {
    ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
    URL url = URL.valueOf("test://localhost:8088");
    List<Filter> list = loader.getActivateExtension(url, new String[]{}, "order");
    //输出:[com.github.kejiwomenzou.dubbo.spi.activate.AuthFilter@782830e,com.github.kejiwomenzou.dubbo.spi.activate.OrderFilter@470e2030]
    System.out.println(list);
}
```

* **情况3： 实现类上加@Activate(group="xxx",value=""), 带group参数**

```java
@Activate(group={"auth"})
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
@Activate(group = {"order", "auth"}, value ={"a:b, c:d"})
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
public static void main(String[] args) {
    ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
    URL url = URL.valueOf("test://localhost:8088");
    List<Filter> list = loader.getActivateExtension(url, new String[]{}, "auth");
    //输出: []
    System.out.println(list);
}
```

* **情况4： 实现类上加@Activate(group="xxx",value=""), 带group参数, URL中带参数**

```java
@Activate(group={"auth"})
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
@Activate(group = {"order", "auth"}, value ={"a:b, c:d"})
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
public static void main(String[] args) {
    ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
    URL url = URL.valueOf("test://localhost:8088?c=d");
    List<Filter> list = loader.getActivateExtension(url, new String[]{}, "auth");
    //输出: [com.github.kejiwomenzou.dubbo.spi.activate.OrderFilter@5aaa6d82]
    System.out.println(list);
}
```

* **情况5： 实现类上加@Activate(group="xxx",value=""), 带group参数, URL中带参数, 指定getActivateExtension入参 value**

```java
@Activate(group={"auth"})
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
@Activate(group = {"order", "auth"}, value ={"a:b, c:d"})
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
@Activate
public class BackListFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("BackListFilter" + " name : " + name + ", url: ");
    }
}
public static void main(String[] args) {
    ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
    URL url = URL.valueOf("test://localhost:8088?c=d");
    List<Filter> list = loader.getActivateExtension(url, new String[]{"blackList"}, "order");
    //输出：[com.github.kejiwomenzou.dubbo.spi.activate.OrderFilter@5aaa6d82, com.github.kejiwomenzou.dubbo.spi.activate.BackListFilter@73a28541]
    System.out.println(list);
}
```

* **情况5： 实现类上加@Activate(group="xxx",value="", oder=-1), order属性来指定输出顺序**

```java
@Activate(group={"auth"})
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
@Activate(group = {"order", "auth"}, value ={"a:b, c:d"}, order = 999)
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
@Activate(group = "order", order=-1)
public class BackListFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("BackListFilter" + " name : " + name + ", url: ");
    }
}
public static void main(String[] args) {
    ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
    URL url = URL.valueOf("test://localhost:8088?c=d");
    List<Filter> list = loader.getActivateExtension(url, new String[]{}, "order");
    //输出：[com.github.kejiwomenzou.dubbo.spi.activate.BackListFilter@6f75e721, com.github.kejiwomenzou.dubbo.spi.activate.OrderFilter@69222c14]
    System.out.println(list);
}
```

### @Activate注解总结

1. `@Activate`表示用来激活满足条件的SPI接口实现类的实例， `@Activate(group={}, value={}, order=0)`三个属性：
   - `group= {"x1", "x2"}` 用来指定SPI实现类**所归属的组**。
   - `value= {"k1:v1, v2:v2"}` 该属性的值为 `k1:v1, v2:v2...`这种形式， **用于匹配URL中的key=value**, 比如： `test://localhost:8088?a1=b1`,URL有一对key,value匹配上了就能激活该类。**重要的一点：`value`属性是`group`属性过滤后的二次过滤**。
   - `order=0`  该属性用于对 `getActivateExtension(url, value[], group)`方法返回的集合进行排序，按自然顺序排序。

2. `@Activate`注解上的属性`group[]`和`value[]`匹配规则 ：
   1. `group[]`空  且   `value[]`空时， **`getActivateExtension`会搜索所有标注了`@Activate`的类，不管是group[]中为什么值都算**。
   2. `group[]`不空 且 `value[]`空时， **`getActivateExtension(url, value[], groupName)`会搜索所有标注了`@Activate`且与入参groupName相等的类**。
   3. `group[]`空 且 `value[]`不空时， `getActivateExtension(url, value[], groupName)`会对搜索出来的标注了`@Activate`且groupName相等的类进行过滤 ，过滤的key和value来自于url中的参数，匹配上了`value`中的任一一对就算。
   4. `group[]`空 且 `value[]`不空时， 这种情况下会啥都匹配不出来。

3. `getActivateExtension(url, value[], group)`中的`value[]`参数：
   1. 该参数不参与`@Activate`中的`group`和`value`的过滤， 它只是**表示当前指定要拿到的SPI 实例，里面的值就是spi name，但会结合过滤出来的SPI 实例进行去重**。比如： 根据`@Activate(group={"a"}, value={"k:v"})`过滤出来 A1, B1, 而`value[]`入参中需要 B1， C1， 最终返回： A1, B1, C1
4. `@Activate`中的`order`属性：
   1. 对结果集按自然顺序排序， 但排序的结果集不包括`getActivateExtension(url, value[], group)`中`value[]`指定的实例。

### ExtensionLoader源码简单分析

1. **ExtensionLoader<T>创建分析**:

   ```java
   //静态属性，所有实例共享， 缓存了 Class<T> -> ExtensionLoader<?>， 一个SPI接口对应一个ExtensionLoader<?>
   private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>(64);
   
   public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
       //1.校验判断忽略： type不空,type只能是接口,type接口上一定要有@SPI注解
       ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
       if (loader == null) {
           //2.找不到就new一个
           EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
           loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
       }
       return loader;
   }
   //私有构造 
   private ExtensionLoader(Class<?> type) {
       this.type = type;
       //3.这里判断type是不是ExtensionFactory类型，不是的话触发 ExtensionFactory SPI接口的加载
       objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
   }
   
   //ExtensionFactory是dubbo自带的,其中还有一个标注的@Adaptive
   spring=org.apache.dubbo.config.spring.extension.SpringExtensionFactory
   adaptive=org.apache.dubbo.common.extension.factory.AdaptiveExtensionFactory
   spi=org.apache.dubbo.common.extension.factory.SpiExtensionFactory
       
   //4.ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
   //触发.getAdaptiveExtension()
    public T getAdaptiveExtension() {
       Object instance = cachedAdaptiveInstance.get();
       if (instance == null) {
           //异常判断，忽略
           synchronized (cachedAdaptiveInstance) {
               instance = cachedAdaptiveInstance.get();
               if (instance == null) {
                   try {
                       instance = createAdaptiveExtension();
                       cachedAdaptiveInstance.set(instance);
                   } catch (Throwable t) {
                       //....
                   }
               }
           }
       }
       return (T) instance;
   }
   //5.createAdaptiveExtension() 创建AdaptiveExtension
   //先获取 adaptiveExtensionClass, 再创建对象
   private T createAdaptiveExtension() {
       try {
           //拆一下代码，方便查看
           T t =  (T) getAdaptiveExtensionClass().newInstance()
               return injectExtension(t);
       } catch (Exception e) {
           //...
       }
   }
   //5.1 获取 adaptiveExtensionClass
   private Class<?> getAdaptiveExtensionClass() {
       //5.2这一步很重要，会触发从META-INF中加载信息，并缓存一些信息
       getExtensionClasses();
       //如果cachedAdaptiveClass不空就直接返回
       //什么时候不空呢？上面getExtensionClasses()加载时，发现了有注解了@Adaptive的类就放到该变量上
       if (cachedAdaptiveClass != null) {
           return cachedAdaptiveClass;
       }
       //没有的话，加载了type对应的所有实现类上都没有加载@Adaptive, 那就创建一个
       return cachedAdaptiveClass = createAdaptiveExtensionClass();
   }
   //手动编织一个AdaptiveExtensionClass出来
   private Class<?> createAdaptiveExtensionClass() {
       //拼接源代码xxxxx$Adaptive.java
       String code = new AdaptiveClassCodeGenerator(type, cachedDefaultName).generate();
       ClassLoader classLoader = findClassLoader();
       //编译， 这里从SPI接口Compiler中取,dubbo自带的, 具体逻辑其实是从AdaptiveCompiler
       //最终看AdaptiveCompiler中loader.getDefaultExtension()来获取 Complier实例， 也可以配置spi name, 默认使用javassist
       //META-INF中的配置如下：
       //adaptive=org.apache.dubbo.common.compiler.support.AdaptiveCompiler
   	//jdk=org.apache.dubbo.common.compiler.support.JdkCompiler
   	//javassist=org.apache.dubbo.common.compiler.support.JavassistCompiler
       org.apache.dubbo.common.compiler.Compiler compiler =
           ExtensionLoader.getExtensionLoader(org.apache.dubbo.common.compiler.Compiler.class).getAdaptiveExtension();
       return compiler.compile(code, classLoader);
   }
   //5.2 getExtensionClasses 加载类的逻辑
   private Map<String, Class<?>> getExtensionClasses() {
       Map<String, Class<?>> classes = cachedClasses.get();
       if (classes == null) {
           synchronized (cachedClasses) {
               classes = cachedClasses.get();
               if (classes == null) {
                   
                   classes = loadExtensionClasses();
                   cachedClasses.set(classes);
               }
           }
       }
       return classes;
   }
   //5.3 loadExtensionClasses()
   private Map<String, Class<?>> loadExtensionClasses() {
       //@SPI注解的defaultName缓存起来
       cacheDefaultExtensionName();
       Map<String, Class<?>> extensionClasses = new HashMap<>();
       //在 META-INF/dubbo/internal/、/META-INF/dubbo/、 /META-INF/services/下加载
       for (LoadingStrategy strategy : strategies) {
           loadDirectory(extensionClasses, strategy.directory(), type.getName(), strategy.preferExtensionClassLoader(),
                         strategy.overridden(), strategy.excludedPackages());
           loadDirectory(extensionClasses, strategy.directory(), type.getName().replace("org.apache", "com.alibaba"),
                         strategy.preferExtensionClassLoader(), strategy.overridden(), strategy.excludedPackages());
       }
   
       return extensionClasses;
   }
   //5.4  loadDirectory -> ... -> loadClass
    private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name,
                              boolean overridden) throws NoSuchMethodException {
      	 //adaptiveClass缓存起来
        if (clazz.isAnnotationPresent(Adaptive.class)) {
            cacheAdaptiveClass(clazz, overridden);
        } 
        //wrapper类缓存起来， wrapper类判断依据： clazz.getConstructor(type) 
        else if (isWrapperClass(clazz)) {
            cacheWrapperClass(clazz);
        } else {
            clazz.getConstructor();
            if (StringUtils.isEmpty(name)) {
                name = findAnnotationName(clazz);
                if (name.length() == 0) {
                    throw new IllegalStateException(
                        "No such extension name for the class " + clazz.getName() + " in the config " + resourceURL);
                }
            }
            String[] names = NAME_SEPARATOR.split(name);
            if (ArrayUtils.isNotEmpty(names)) {
                //缓存activeAclass
                cacheActivateClass(clazz, names[0]);
                for (String n : names) {
                    cacheName(clazz, n);
                    saveInExtensionClass(extensionClasses, clazz, n, overridden);
                }
            }
        }
    }
   //最终回到第1步， 加载了ExtensionFactory子类，然后加载回到了type，再加载type子类
   ```

到此为止上面的代码做了以下事情:

- 获取 `type`对应的`ExtensionLoader`时， 先加载了`ExtensionFactory`, 而且这个`ExtensionFactory`的子类会放到每个`type`的`objectFactories`中。
- 类上注解了`@Adaptive`，` getApaptiveExtension()`直接返回该类，否则会手动创建一个$Adaptive代理类出来， 在这过程中还加载了SPI接口 `Complier`。
- 加载`META-INF`中SPI接口过程中，会缓存 @SPI default name, 缓存adaptiveClass, 缓存activeClass， 方便后面获取。
- `wrapper`装饰类的其实就是： `clazz.getConstructor(type)`,SPI接口实现类把 type作为构造入参了， 后面处理时会把type传进去。



2. **Dubbo SPI中的IOC、AOP** ：

   1. IOC:

      - 就是上面提到的那个`wrapper`类，如果SPI接口的实现类中有一个参数的构造且入参是该 SPI接口类型。

      - 举例说明： **只要有`wrapper`实现类， 获取所有的扩展类时都会被`wrapper`类包装**。

        ```java
        //配置: wrapper=com.github.kejiwomenzou.dubbo.spi.wrapper.WrapperFilter
        @SPI
        public interface Filter {
            void filter(String name, URL url);
        }
        public class WrapperFilter implements Filter {
            private Filter filter;
            public WrapperFilter(Filter filter) {
                this.filter = filter;
            }
            @Override
            public void filter(String name, URL url) {
                System.out.println("WrapperFilter: name = {" + name + "}, this.filer = {" + filter + "}");
            }
        }
        public static void main(String[] args) {
            ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
            URL url = URL.valueOf("test://localhost:8088");
            Filter wrapper = loader.getExtension("auth");
            //输出:WrapperFilter: name = {哈哈}, this.filer = {com.github.kejiwomenzou.dubbo.spi.activate.AuthFilter@69222c14}
            wrapper.filter("哈哈", url);
        }
        ```

        

   2. AOP在上面已经体现了: @Adaptive 注解在

   ```java
   private Class<?> getAdaptiveExtensionClass() {
           getExtensionClasses();
           if (cachedAdaptiveClass != null) {
               return cachedAdaptiveClass;
           }
           //子类上没有注解@Adaptive注解
           return cachedAdaptiveClass = createAdaptiveExtensionClass();
       }
   
   private Class<?> createAdaptiveExtensionClass() {
       //手动拼接xxx$Adaptive.java => 拼接过程中处理了@Adaptive注解在接口方法上的逻辑
       String code = new AdaptiveClassCodeGenerator(type, cachedDefaultName).generate();
       ClassLoader classLoader = findClassLoader();
       org.apache.dubbo.common.compiler.Compiler compiler =
           ExtensionLoader.getExtensionLoader(org.apache.dubbo.common.compiler.Compiler.class).getAdaptiveExtension();
       return compiler.compile(code, classLoader);
   }
   //generate() -> generateMethodContent（）
   private String generateMethodContent(Method method) {
       Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
       StringBuilder code = new StringBuilder(512);
       if (adaptiveAnnotation == null) {
           return generateUnsupported(method);
       } else {
           int urlTypeIndex = getUrlTypeIndex(method);
   
           // found parameter in URL type
           if (urlTypeIndex != -1) {
               // Null Point check
               code.append(generateUrlNullCheck(urlTypeIndex));
           } else {
               //这里处理了 @Adaptive方法一定要有参数 URL
               // did not find parameter in URL type
               code.append(generateUrlAssignmentIndirectly(method));
           }
           //.....
       }
       return code.toString();
   }
   ```



全文完。

> 参考博客：https://www.jianshu.com/p/dc616814ce98， 感谢！

