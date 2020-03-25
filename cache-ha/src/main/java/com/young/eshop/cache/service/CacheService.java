package com.young.eshop.cache.service;

import com.young.eshop.cache.model.Product;

/**
 * 缓存service接口
 *
 * @author Administrator
 */
public interface CacheService {

    /**
     * 将商品信息保存到本地缓存中
     *
     * @param product 商品信息
     */
    void saveLocalCache(Product product);

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id id
     * @return com.young.eshop.com.young.cache.model.ProductInfo
     */
    Product getLocalCache(Integer id);

}
