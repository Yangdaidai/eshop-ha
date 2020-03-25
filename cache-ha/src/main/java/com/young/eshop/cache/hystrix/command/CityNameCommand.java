package com.young.eshop.cache.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.young.eshop.cache.localcache.LocationCache;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取城市名称的command
 *
 * @author Administrator
 */
@Slf4j
public class CityNameCommand extends HystrixCommand<String> {

    private Integer cityId;

    public CityNameCommand(Integer cityId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CityNameGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)));
        this.cityId = cityId;
    }

    @Override
    protected String run() {
        return LocationCache.getCityName(cityId);
    }

    @Override
    protected String getFallback() {
        log.info("从本地缓存获取默认的城市名称数据，cityId : {}", cityId);
        return LocationCache.getCityName(cityId);
    }

}
