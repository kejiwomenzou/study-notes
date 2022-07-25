package com.github.kejiwomenzou.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.SPI;

@SPI
public interface Filter {
    void filter(String name, URL url);
}
