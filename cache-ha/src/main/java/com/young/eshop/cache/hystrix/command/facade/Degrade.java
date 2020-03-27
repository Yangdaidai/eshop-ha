package com.young.eshop.cache.hystrix.command.facade;



public class Degrade {
	
	private static boolean degrade = false;


	public static void setDegrade(boolean degrade) {
		Degrade.degrade = degrade;
	}

	public static boolean isDegrade() {
		return degrade;
	}
}
