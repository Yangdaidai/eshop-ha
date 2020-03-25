package com.young.eshop.cache.localCache;

import java.util.HashMap;
import java.util.Map;

public class LocationCache {
	//模拟本地缓存
	private static Map<Integer, String> cityMap = new HashMap<Integer, String>();
	
	static {
		cityMap.put(11000, "北京");
		cityMap.put(31000, "上海");
	}
	
	public static String getCityName(Integer cityId) {
		return cityMap.get(cityId);
	}
	
}
