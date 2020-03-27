package com.young.eshop.cache.hystrix.command;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.young.eshop.cache.localcache.BrandCache;
import com.young.eshop.cache.localcache.LocationCache;
import com.young.eshop.cache.model.Product;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * 获取商品信息
 *
 * @author Administrator
 */
public class ProductCommand extends HystrixCommand<Product> {

    public static final String PRODUCT_INFO = "product_info_";
    private Integer productId;
    private static final HystrixCommandKey PRODUCT_COMMAND = HystrixCommandKey.Factory.asKey("ProductInfoCommand");

    public ProductCommand(Integer productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductService"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("ProductInfoCommand"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ProductInfoPool"))
                        //default : coreSize = 10   QueueSize= 5
                        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(15).withQueueSizeRejectionThreshold(10))
//                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
//                        .withCircuitBreakerRequestVolumeThreshold(30)
//                        .withCircuitBreakerErrorThresholdPercentage(40)
//                        .withCircuitBreakerSleepWindowInMilliseconds(3000)
//                        .withExecutionTimeoutInMilliseconds(500)
//                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(30))
        );
        this.productId = productId;
    }

    @Override
    protected Product run() {
     //  int a= 1/0; 出现异常,如果复写了getFallback() 会进行 fallback
        String url = "http://127.0.0.1:8082/product/getProductInfo?productId=" + productId;
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, Product.class);
    }

    @Override
    protected Product getFallback() {
        Product productInfo = new Product();
        // 从请求参数中获取到的唯一条数据
        productInfo.setId(productId);
        // 从本地缓存中获取一些数据
        productInfo.setBrandId(BrandCache.getBrandId(productId));
        productInfo.setBrandName(BrandCache.getBrandName(productInfo.getBrandId()));
        productInfo.setCityId(LocationCache.getCityId(productId));
        productInfo.setCityName(LocationCache.getCityName(productInfo.getCityId()));
        // 手动填充一些默认的数据
        productInfo.setColor("默认颜色");
        Date createDate = new Date();
        productInfo.setCreatedTime(createDate);
        productInfo.setName("默认商品");

        return productInfo;
    }

    @Override
    protected String getCacheKey() {
        return PRODUCT_INFO + productId;
    }

    public static void flushCache(int id) {
        HystrixRequestCache.getInstance(PRODUCT_COMMAND,
                HystrixConcurrencyStrategyDefault.getInstance()).clear(PRODUCT_INFO + id);
    }
}
