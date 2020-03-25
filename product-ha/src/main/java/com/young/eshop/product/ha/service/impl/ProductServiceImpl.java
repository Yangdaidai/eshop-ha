package com.young.eshop.product.ha.service.impl;

import com.young.eshop.product.ha.mapper.ProductMapper;
import com.young.eshop.product.ha.model.Product;
import com.young.eshop.product.ha.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright © 2020 YOUNG. All rights reserved.
 *
 * @Package com.young.eshop.product.ha.service.impl
 * @ClassName ProductServiceImpl
 * @Description
 * @Author young
 * @Modify young
 * @Date 2020/3/24 23:36
 * @Version 1.0.0
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public Product getProduct(Integer id) {
        return productMapper.selectOne(id);
    }
}
