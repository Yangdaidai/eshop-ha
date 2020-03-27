package com.young.eshop.cache.hystrix.command.facade;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.young.eshop.cache.model.Product;

import java.util.Date;

public class ProductFromMySQLCommand extends HystrixCommand<Product> {

	private Integer productId;
	
	public ProductFromMySQLCommand(Integer productId) {
		super(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"));
		this.productId = productId;
	}
	
	@Override
	protected Product run() throws Exception {
		// 模拟从mysql中直接去查询，获取一些数据，但是因为我们不是太了解业务逻辑，所以只能取到少数一些字段
		Product product = new Product();
		product.setId(productId);
		product.setName("iphone 8 ");
		product.setPrice(7999.00);
		product.setCreatedTime(new Date());
		return product;
	}
	
}
