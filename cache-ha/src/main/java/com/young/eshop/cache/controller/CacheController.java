package com.young.eshop.cache.controller;

import com.netflix.hystrix.HystrixCommand;
import com.young.eshop.cache.hystrix.command.CityNameCommand;
import com.young.eshop.cache.hystrix.command.ProductCommand;
import com.young.eshop.cache.hystrix.command.ProductsCommand;
import com.young.eshop.cache.hystrix.command.collapser.ProductsCollapser;
import com.young.eshop.cache.model.Product;
import com.young.eshop.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * 缓存Controller
 *
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("cache")
public class CacheController {

    @Resource
    private CacheService cacheService;

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/testPutCache")
    public String testPutCache(Product productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("/testGetCache")
    public Product testGetCache(Integer id) {
        return cacheService.getLocalCache(id);
    }


    @GetMapping("/change/product")
    public String changeProduct(@RequestParam Integer productId) {
        //通过http模拟MQ得到最新的商品信息
        // 拿到一个商品id
        // 调用商品服务的接口，获取商品id对应的商品的最新数据
        // 用restTemplate去调用商品服务的http接口
        String url = "http://127.0.0.1:8082/product/getProductInfo?id=" + productId;
        Product response = restTemplate.getForObject(url, Product.class, productId);
        log.info("Product : {}", response);
        return "success";
    }

    /**
     * nginx 开始，各级缓存都失效了，nginx发送很多的请求直接到缓存服务要求拉取最原始的数据
     *
     * @param id 商品id
     * @return Product
     */
    @GetMapping("/getProductInfo/{id}")
    public Product getProductInfo(@PathVariable Integer id) {
        // 拿到一个商品id
        // 调用商品服务的接口，获取商品id对应的商品的最新数据
        // 用HttpClient去调用商品服务的http接口
        HystrixCommand<Product> productInfoCommand = new ProductCommand(id);
        //同步调用
        Product productInfo = productInfoCommand.execute();
        Integer cityId = productInfo.getCityId();

        //信号量隔离
        CityNameCommand cityNameCommand = new CityNameCommand(cityId);
        String cityName = cityNameCommand.execute();
        productInfo.setCityName(cityName);

        //异步调用
		/*Future<ProductInfo> future = productInfoCommand.queue();
		try {
			Thread.sleep(1000);
			System.out.println(future.get());
		} catch (Exception e) {
			e.printStackTrace();
		}*/

        log.info("productInfo : {}", productInfo);

        return productInfo;
    }

    /**
     * 一次性批量查询多条商品数据的请求
     */
    @RequestMapping("/getProductInfos")
    public List<Product> getProductInfos(@RequestBody List<Integer> productIds) {
        List<Product> productList = new ArrayList<>();
        ProductsCommand productsCommand = new ProductsCommand(productIds);
        Observable<List<Product>> observable = productsCommand.observe();
        observable.subscribe(new Observer<List<Product>>() {
            @Override
            public void onCompleted() {

                log.info("productList query completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Product> products) {
                log.info("products : {}", products);
            }


        });

        try {
            productList = observable.toBlocking().toFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return productList;
    }

    /**
     * 批量查询多条商品数据,进行请求的缓存
     */
    @RequestMapping("/getProductsWithCache")
    public List<Product> getProductsWithCache(@RequestBody List<Integer> productIds) {
        List<Product> products = new ArrayList<>();
        productIds.forEach(id -> {
            ProductCommand productCommand = new ProductCommand(id);
            Product product = productCommand.execute();
            boolean fromCache = productCommand.isResponseFromCache();
            if(!fromCache)
                products.add(product);
            log.info("product is : {}", product);
            log.info("isResponseFromCache : {}", fromCache);
        });

        return products;
    }

    @RequestMapping("/getProducts")
    public List<Product> getProducts(@RequestBody List<Integer> productIds) {

        List<Product> productList = new ArrayList<>();

        productIds.forEach(productId -> {
            ProductsCollapser productsCollapser = new ProductsCollapser(restTemplate, productId);
            try {
                productList.add(productsCollapser.queue().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        List<Product> resultList = productList.stream().distinct().collect(Collectors.toList());
        return resultList;
    }

}
