package com.young.eshop.cache.localcache;

import java.util.HashMap;
import java.util.Map;

public class LocationCache {
	//模拟本地缓存
	private static Map<Integer, String> cityMap = new HashMap<Integer, String>();

	private static Map<Integer, Integer> productCityMap = new HashMap<>();

	static {
		cityMap.put(11000, "北京");
		cityMap.put(31000, "上海");
	}

	static {
		productCityMap.put(110, 11000);
		productCityMap.put(111, 31000);
	}
	
	public static String getCityName(Integer cityId) {
		return cityMap.get(cityId);
	}

	public static Integer getCityId(Integer productId) {
		return productCityMap.get(productId);
	}
}
