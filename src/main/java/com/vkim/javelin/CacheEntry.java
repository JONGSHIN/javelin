package com.vkim.javelin;

public interface CacheEntry<K, V> {
	
	K getKey();

	int getHashCode();

	long getLastAccessTime();

	long getLastWriteTime();
	
	void setLastAccessTime(long lastAccessTime);
	
	void setLastWriteTime (long lastWriteTime);
}
