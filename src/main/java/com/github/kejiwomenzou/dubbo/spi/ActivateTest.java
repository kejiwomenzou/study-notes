package com.github.kejiwomenzou.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.List;

public class ActivateTest {

    public static void main(String[] args) {
        ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
        URL url = URL.valueOf("test://localhost:8088");
        List<Filter> list = loader.getActivateExtension(url, new String[]{});
        System.out.println(list);

//        ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
//        URL url = URL.valueOf("test://localhost:8088");
//        Filter wrapper = loader.getExtension("auth");
//        wrapper.filter("哈哈", url);
    }
}
