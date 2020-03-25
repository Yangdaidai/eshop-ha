package com.young.eshop.cache.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import java.io.IOException;

/**
 * Copyright © 2020 YOUNG. All rights reserved.
 *
 * @Package com.young.eshop.cache.filter
 * @ClassName HystrixRequestContextFilter
 * @Description
 * @Author young
 * @Modify young
 * @Date 2020/3/25 16:10
 * @Version 1.0.0
 **/
public class HystrixRequestContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            chain.doFilter(request, response);
        } finally {
            context.shutdown();
        }
    }
}
