package com.github.kejiwomenzou.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI("netty")
public interface MultiTransport {

    @Adaptive({"test"})
    void echo(String name, URL url);

    void echo(String name);
}
