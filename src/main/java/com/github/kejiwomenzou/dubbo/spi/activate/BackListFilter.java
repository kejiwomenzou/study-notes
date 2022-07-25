package com.github.kejiwomenzou.dubbo.spi.activate;

import com.github.kejiwomenzou.dubbo.spi.Filter;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;

@Activate(value = "111",order = -1)
public class BackListFilter implements Filter {
    @Override
    public void filter(String name, URL url) {
        System.out.println("BackListFilter" + " name : " + name + ", url: ");
    }
}
