package com.github.kejiwomenzou.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;


public class AdaptiveTest {

//    public static void main(String[] args) {
//        URL dubboUrl = URL.valueOf("test://localhost:9090");
//        ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
//        MultiTransport transport = loader.getDefaultExtension();
//        transport.echo("哈哈", dubboUrl);
//    }
    public static void main(String[] args) {
        URL dubboUrl = URL.valueOf("test://localhost:9090?test=unknow");
        ExtensionLoader<MultiTransport> loader = ExtensionLoader.getExtensionLoader(MultiTransport.class);
        MultiTransport transport = loader.getAdaptiveExtension();
        System.out.println(transport);
        transport.echo("哈哈", dubboUrl);
    }

}
