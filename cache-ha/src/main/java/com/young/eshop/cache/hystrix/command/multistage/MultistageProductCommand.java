package com.young.eshop.cache.hystrix.command.multistage;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.young.eshop.cache.localcache.BrandCache;
import com.young.eshop.cache.localcache.LocationCache;
import com.young.eshop.cache.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.apache.HttpClientUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 获取商品信息
 *
 * @author Administrator
 */
@Slf4j
public class MultistageProductCommand extends HystrixCommand<Product> {

    private Integer productId;

    public MultistageProductCommand(Integer productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductService"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("ProductInfoCommand"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ProductInfoPool"))
        );
        this.productId = productId;
    }

    @Override
    protected Product run() throws Exception {

        if(productId.equals(-1)) {
            throw new Exception();
        }
        if(productId.equals(-2)) {
            throw new Exception();
        }

        Product product = getRemoteProduct(productId);

        log.info(" product = {}",product);
        return product;
    }

    private static Product getRemoteProduct(Integer productId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8082/product/getProductInfo?productId=" + productId;
        return restTemplate.getForObject(url, Product.class);
    }

    @Override
    protected Product getFallback() {
        log.info("into FirstLevelFallbackCommand ");
        return new FirstLevelFallbackCommand(productId).execute();
    }

    private static class FirstLevelFallbackCommand extends HystrixCommand<Product> {

        private Integer productId;

        public FirstLevelFallbackCommand(Integer productId) {
            // 第一级的降级策略，因为这个command是运行在fallback中的
            // 所以至关重要的一点是，在做多级降级的时候，要将降级command的线程池单独做一个出来
            // 如果主流程的command都失败了，可能线程池都已经被占满了
            // 降级command必须用自己的独立的线程池
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("FirstLevelFallbackCommand"))
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FirstLevelFallbackPool"))
            );
            this.productId = productId;
        }

        @Override
        protected Product run() throws Exception {
            // 这里，因为是第一级降级的策略，所以说呢，其实是要从备用机房的机器去调用接口
            // 但是，我们这里没有所谓的备用机房，所以说还是调用同一个服务来模拟
            if(productId.equals(-2)) {
                throw new Exception();
            }
            Product product = getRemoteProduct(productId);
            log.info(" FirstLevelFallbackCommand product = {}",product);

            return product;
        }

        @Override
        protected Product getFallback() {
            // 第二级降级策略，第一级降级策略，都失败了
            log.info(" 第二级降级策略，第一级降级策略，都失败了");

            Product product = new Product();
            // 从请求参数中获取到的唯一条数据
            product.setId(productId);
            // 从本地缓存中获取一些数据
            product.setBrandId(BrandCache.getBrandId(productId));
            product.setBrandName(BrandCache.getBrandName(product.getBrandId()));
            product.setCityId(LocationCache.getCityId(productId));
            product.setCityName(LocationCache.getCityName(product.getCityId()));
            // 手动填充一些默认的数据
            product.setColor("默认的颜色");
            Date createDate = new Date();
            product.setCreatedTime(createDate);
            product.setName("默认商品");
            return product;
        }

    }
}
