package com.young.eshop.cache.hystrix.command.facade;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.young.eshop.cache.hystrix.command.ProductCommand;
import com.young.eshop.cache.model.Product;


public class ProductFacadeCommand extends HystrixCommand<Product> {
	
	private Integer productId;

	public static final String PRODUCT_INFO = "product_info_";

	public ProductFacadeCommand(Integer productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("ProductFacadeCommand"))
		        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
		        		.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)
		        		.withExecutionIsolationSemaphoreMaxConcurrentRequests(15)));
		this.productId = productId;
	}

	@Override
	protected Product run()  {
		if(!Degrade.isDegrade()) {
			return new ProductCommand(productId).execute();
		} else {
			return new ProductFromMySQLCommand(productId).execute();
		}
	}

	@Override
	protected Product getFallback() {
		return new Product();
	}

}
