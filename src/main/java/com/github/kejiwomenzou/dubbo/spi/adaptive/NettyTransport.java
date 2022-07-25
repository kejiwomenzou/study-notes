package com.github.kejiwomenzou.dubbo.spi.adaptive;

import com.github.kejiwomenzou.dubbo.spi.MultiTransport;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;


@Adaptive
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
