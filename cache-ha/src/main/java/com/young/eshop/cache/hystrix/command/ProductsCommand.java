package com.young.eshop.cache.hystrix.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.young.eshop.cache.model.Product;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 批量查询多个商品数据的command
 *
 * @author Administrator
 */
public class ProductsCommand extends HystrixObservableCommand<List<Product>> {

    private List<Integer> productIds;

    public ProductsCommand(List<Integer> productIds) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
        this.productIds = productIds;
    }

    @Override
    protected Observable<List<Product>> construct() {

        return Observable.unsafeCreate((Observable.OnSubscribe<List<Product>>) observer -> {
            try {
                RestTemplate restTemplate = new RestTemplate();
                Objects.requireNonNull(productIds, "productIds be not null!");
				List<Product> products = new ArrayList<>();
				productIds.forEach(productId -> {
                    String url = "http://127.0.0.1:8082/product/getProductInfo?productId=" + productId;
                    Product product = restTemplate.getForObject(url, Product.class, productId);
                    products.add(product);
                });
                observer.onNext(products);
                observer.onCompleted();
            } catch (Exception e) {
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

}
