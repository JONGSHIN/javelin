package com.vkim.javelin;

public interface Cache<K, V> {
	
	void put(K key, V value);

	V get(K key);

	V invalidate(K key);

	V getOrLoad(K key, CacheLoader<K, V> loader);
	
}
