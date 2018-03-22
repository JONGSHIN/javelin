package com.vkim.javelin;

public interface CacheEntry<K, V> {

	K getKey();

	CacheEntry<K, V> getNext();

	ValueItem<V> getValueItem();

	int getHashCode();

	long getLastAccessTime();

	long getLastWriteTime();

	void setLastAccessTime(long lastAccessTime);

	void setLastWriteTime(long lastWriteTime);

}
