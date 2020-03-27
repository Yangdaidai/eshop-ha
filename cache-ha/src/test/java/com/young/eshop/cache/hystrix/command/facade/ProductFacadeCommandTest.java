package com.young.eshop.cache.hystrix.command.facade;


class ProductFacadeCommandTest {
    public static void main(String[] args) {

//        ProductFacadeCommand productFacadeCommand = new ProductFacadeCommand(1);
//        System.out.println(productFacadeCommand.execute());
        Degrade.setDegrade(true);
        ProductFacadeCommand productFacadeCommand1 = new ProductFacadeCommand(1);
        System.out.println(productFacadeCommand1.execute());
    }
}