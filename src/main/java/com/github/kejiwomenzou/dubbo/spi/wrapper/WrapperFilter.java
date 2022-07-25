package com.github.kejiwomenzou.dubbo.spi.wrapper;

import com.github.kejiwomenzou.dubbo.spi.Filter;
import org.apache.dubbo.common.URL;

public class WrapperFilter implements Filter {
    private Filter filter;
    public WrapperFilter(Filter filter) {
        this.filter = filter;
    }
    @Override
    public void filter(String name, URL url) {
        System.out.println("WrapperFilter: name = {" + name + "}, this.filer = {" + filter + "}");
    }
}
