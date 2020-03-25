package com.young.eshop.product.ha.service;

import com.young.eshop.product.ha.model.Product;

/**
 * Copyright © 2020 YOUNG. All rights reserved.
 *
 * @Package com.young.eshop.product.ha.service
 * @ClassName ProductService
 * @Description
 * @Author young
 * @Modify young
 * @Date 2020/3/24 23:35
 * @Version 1.0.0
 **/
public interface ProductService {
    Product getProduct(Integer id);
}
