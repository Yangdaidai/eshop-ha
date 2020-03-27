package com.young.eshop.cache.hystrix.command.multistage;

import com.young.eshop.cache.model.Product;


public class MultistageProductCommandTest {
    public static void main(String[] args) {

//        Product product1 = new MultistageProductCommand(-1).execute();
//        System.out.println("product1 = " + product1);

        Product product2 = new MultistageProductCommand(-2).execute();
        System.out.println("product2 = " + product2);
    }
}