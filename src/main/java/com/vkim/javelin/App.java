package com.vkim.javelin;

public class App {
	public static void main(String[] args) {

		Cache<String, Integer> cache = new CacheBuilder().concurrencyLevel(3).setExpireAfterAccess(14).build();
		cache.put("hello", 456456);

		System.out.println(cache.get("hello"));
	}
}
