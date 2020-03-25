package com.young.eshop.cache.hystrix.command;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.young.eshop.cache.model.Product;
import org.springframework.web.client.RestTemplate;

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
        );
        this.productId = productId;
    }

    @Override
    protected Product run() {
        String url = "http://127.0.0.1:8082/product/getProductInfo?productId=" + productId;
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, Product.class, productId);
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
