package com.young.eshop.cache.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.young.eshop.cache.model.Product;
import org.springframework.web.client.RestTemplate;

/**
 * 获取商品信息
 *
 * @author Administrator
 */
public class ProductCommand extends HystrixCommand<Product> {

    private Integer productId;

    public ProductCommand(Integer productId) {
        super(HystrixCommandGroupKey.Factory.asKey("ProductGroup"));
        this.productId = productId;
    }

    @Override
    protected Product run() {
        String url = "http://127.0.0.1:8082/product/getProductInfo?productId=" + productId;
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, Product.class, productId);
    }

}
