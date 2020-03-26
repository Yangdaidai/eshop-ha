package com.young.eshop.cache.hystrix.command.collapser;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.young.eshop.cache.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ProductsCollapser extends HystrixCollapser<List<Product>, Product, Integer> {

    public static final String PRODUCT_INFO = "product_info_";
    private Integer productId;
    public RestTemplate restTemplate;

    public ProductsCollapser(RestTemplate restTemplate,Integer productId) {
        this.productId = productId;
        this.restTemplate=restTemplate;
    }

    @Override
    public Integer getRequestArgument() {
        return productId;
    }

    @Override
    protected HystrixCommand<List<Product>> createCommand(
            Collection<CollapsedRequest<Product, Integer>> requests) {

        // 在这里，我们可以做到什么呢，将多个商品id合并在一个batch内，直接发送一次网络请求，获取到所有的结果
        List<Integer> idList = new ArrayList<>(requests.size());
        requests.forEach(request -> {
            idList.add(request.getArgument());
        });
        log.info("createCommand method invoke, params= {}" , idList);

        return new BatchCommand(restTemplate,idList);
    }

    @Override
    protected void mapResponseToRequests(
            List<Product> batchResponse,
            Collection<CollapsedRequest<Product, Integer>> requests) {

        AtomicInteger count = new AtomicInteger();
        requests.forEach(request -> {
            int index = count.getAndIncrement();
            request.setResponse(batchResponse.get(index));
        });

    }

    @Override
    protected String getCacheKey() {
        return PRODUCT_INFO + productId;
    }

}
