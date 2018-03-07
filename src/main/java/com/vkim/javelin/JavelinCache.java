package com.vkim.javelin;

import java.lang.ref.ReferenceQueue;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

public class JavelinCache<K, V> extends AbstractCache<K, V> {

	static Logger _logger = Logger.getLogger(JavelinCache.class);

	ReferenceQueue<K> valueReferenceQueue;

	StripedLocks stripedLocks;

	int initialCapacity;
	int concurrencyLevel;
	long expireAfterAccess;
	long expireAfterWrite;

	public JavelinCache(CacheBuilder builder) {
		this.initialCapacity = builder.getInitialCapacity();

		this.concurrencyLevel = builder.getConcurrencyLevel();
		stripedLocks = new StripedLocks(concurrencyLevel);

		this.expireAfterAccess = builder.getExpireAfterAccess();
		this.expireAfterWrite = builder.getExpireAfterWrite();
	}

	static final class LruMap<K, V> extends LinkedHashMap<K, V> {
		private static final long serialVersionUID = 8375990792617474417L;

		LruMap(int initialCapacity) {
			this(initialCapacity, 0.75f);
		}

		LruMap(int initialCapacity, float loadFactor) {
			super(initialCapacity, loadFactor, true);
		}
	}

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub

	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(K key) {
		// TODO Auto-generated method stub
		return null;
	}

}
