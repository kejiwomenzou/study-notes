package com.github.kejiwomenzou.dubbo.spi.adaptive;

import com.github.kejiwomenzou.dubbo.spi.MultiTransport;
import org.apache.dubbo.common.URL;


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
