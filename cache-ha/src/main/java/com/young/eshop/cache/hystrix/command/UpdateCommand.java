package com.young.eshop.cache.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 更新商品信息缓存
 *
 * @author Administrator
 */
public class UpdateCommand extends HystrixCommand<Boolean> {

    private Integer productId;

    protected UpdateCommand(Integer productId) {
        super(HystrixCommandGroupKey.Factory.asKey("UpdateCommand"));
        this.productId = productId;
    }


    @Override
    protected Boolean run() {
        ProductCommand.flushCache(productId);
        return true;
    }
}
