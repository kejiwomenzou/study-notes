package com.github.kejiwomenzou.dubbo.spi.activate;

import com.github.kejiwomenzou.dubbo.spi.Filter;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;

@Activate(group = {"order"}, order = 999)
public class OrderFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
