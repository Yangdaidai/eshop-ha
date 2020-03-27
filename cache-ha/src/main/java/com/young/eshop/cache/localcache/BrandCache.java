package com.young.eshop.cache.localcache;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2020 YOUNG. All rights reserved.
 *
 * @Package com.young.eshop.cache.localcache
 * @ClassName BrandCache
 * @Description
 * @Author young
 * @Modify young
 * @Date 2020/3/27 10:28
 * @Version 1.0.0
 **/
public class BrandCache {
    //模拟本地缓存
    private static Map<Integer, String> brandMap = new HashMap<Integer, String>();
    private static Map<Integer, Integer> productBrandMap = new HashMap<>();


    static {
        brandMap.put(5020, "iPhone");
        brandMap.put(5021, "RED MI");
    }

    static {
        productBrandMap.put(110, 5020);
        productBrandMap.put(111, 5021);
    }

    public static String getBrandName(Integer brandId) {
        return brandMap.get(brandId);
    }


    public static Integer getBrandId(Integer productId) {
        return productBrandMap.get(productId);
    }


}
