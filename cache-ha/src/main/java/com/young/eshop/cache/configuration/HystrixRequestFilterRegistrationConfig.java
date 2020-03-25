package com.young.eshop.cache.configuration;

import com.young.eshop.cache.filter.HystrixRequestContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName HystrixRequestFilterRegistrationConfig
 * @Description HystrixRequestContextFilter
 * 1. 注册 hystrix filter
 * @Author young
 * @Date 2020/2/17 13:00
 * @Version 1.0
 **/
@Configuration
public class HystrixRequestFilterRegistrationConfig {


    /**
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean<com.young.eshop.cache.filter.HystrixRequestContextFilter
     * @Description 注册 hystrix filter
     * @param:
     * @Author young
     * @CreatedTime 2020/3/25 16:21
     * @Version V1.0.0
     **/
    @Bean
    public FilterRegistrationBean<HystrixRequestContextFilter> filterRegistration() {
        FilterRegistrationBean<HystrixRequestContextFilter> registration = new FilterRegistrationBean<>(new HystrixRequestContextFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
