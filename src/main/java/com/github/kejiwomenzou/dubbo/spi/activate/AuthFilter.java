package com.github.kejiwomenzou.dubbo.spi.activate;

import com.github.kejiwomenzou.dubbo.spi.Filter;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;

@Activate(group = {"order", "auth"}, value ={"a:b, c:d"}, order = 2)
public class AuthFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("AuthFilter" + " name : " + name + ", url: ");
    }
}
