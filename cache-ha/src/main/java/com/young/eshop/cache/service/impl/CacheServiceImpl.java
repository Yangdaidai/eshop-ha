package com.young.eshop.cache.service.impl;

import com.young.eshop.cache.model.Product;
import com.young.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * 缓存Service实现类
 *
 * @author Administrator
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    /**
     * 将商品信息保存到本地缓存中
     *
     * @param product 商品信息
     */
    @CachePut(value = CACHE_NAME, key = "'key_'+#product.getId()")
    public void saveLocalCache(Product product) {
    }

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id id
     * @return Product
     */
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public Product getLocalCache(Integer id) {
        return null;
    }

}
