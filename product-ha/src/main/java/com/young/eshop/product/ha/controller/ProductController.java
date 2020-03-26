package com.young.eshop.product.ha.controller;

import com.young.eshop.product.ha.model.Product;
import com.young.eshop.product.ha.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping("/getProductInfo")
    public Product getProductInfo(Integer productId) {
        return productService.getProduct(productId);
    }


    @RequestMapping("/getProductInfos")
    public List<Product> getProductInfos(@RequestBody List<Integer> ids) {
        return productService.getProducts(ids);
    }
}
