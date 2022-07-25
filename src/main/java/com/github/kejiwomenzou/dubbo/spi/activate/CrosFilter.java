package com.github.kejiwomenzou.dubbo.spi.activate;

import com.github.kejiwomenzou.dubbo.spi.Filter;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;

@Activate(group = "cros")
public class CrosFilter implements Filter {

    @Override
    public void filter(String name, URL url) {
        System.out.println("OrderFilter" + " name : " + name + ", url: ");
    }
}
