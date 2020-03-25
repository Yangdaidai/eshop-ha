package com.young.eshop.product.ha.controller;

import com.young.eshop.product.ha.model.Product;
import com.young.eshop.product.ha.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping("/getProductInfo")
    public Product getProductInfo(Integer productId) {
        return productService.getProduct(productId);
    }
}
