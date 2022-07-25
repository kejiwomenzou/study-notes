package com.github.kejiwomenzou.dubbo.spi.adaptive;

import com.github.kejiwomenzou.dubbo.spi.MultiTransport;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;

//@Adaptive
public class GrizzlyTransport implements MultiTransport {

    @Override
    public void echo(String name, URL url) {
        System.out.printf("Grizzly echo: name={%s}, url={%s}", name, url);
    }

    @Override
    public void echo(String name) {
        System.out.printf("Grizzly echo: name={%s}", name);
    }
}
