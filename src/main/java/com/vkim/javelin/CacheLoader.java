package com.vkim.javelin;

public interface CacheLoader<K, V> {
	V load(K key);
}
