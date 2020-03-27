package com.young.eshop.cache.hystrix.command.collapser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.young.eshop.cache.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * Copyright © 2020 YOUNG. All rights reserved.
 *
 * @Package com.young.eshop.cache.hystrix.command
 * @ClassName BatchCommand
 * @Description
 * @Author young
 * @Modify young
 * @Date 2020/3/26 11:05
 * @Version 1.0.0
 **/
@Slf4j
public class ProductBatchCommand extends HystrixCommand<List<Product>> {

    public List<Integer> idList;
    public RestTemplate restTemplate;

    public ProductBatchCommand(RestTemplate restTemplate, List<Integer> idList) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("ProductsCollapser")));
        this.idList = idList;
        this.restTemplate = restTemplate;
    }

    @Override
    protected List<Product> run() {

        List<Product> productList = getProductList();
        Objects.requireNonNull(productList).forEach(product -> {
            log.info("in BatchCommand, productInfo = {}", product);
        });

        return productList;
    }

    private List<Product> getProductList() {
        String url = "http://127.0.0.1:8082/product/getProductInfos";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Integer>> httpEntity = new HttpEntity<>(idList, headers);
        ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<List<Product>>() {};
        ResponseEntity<List<Product>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType);
        return response.getBody();
    }

}
