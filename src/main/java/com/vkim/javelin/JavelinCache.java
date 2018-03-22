package com.vkim.javelin;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.apache.log4j.Logger;

public class JavelinCache<K, V> extends AbstractCache<K, V> {

	static Logger _logger = Logger.getLogger(JavelinCache.class);

	static final int MAXIMUM_SIZE = 1 << 30;

	ReferenceQueue<K> valueReferenceQueue;

	StripedLocks stripedLocks;

	AtomicReferenceArray<CacheEntry<K, V>> atomicReferenceArray;

	int initialCapacity;
	int concurrencyLevel;
	long expireAfterAccess;
	long expireAfterWrite;

	public JavelinCache(CacheBuilder builder) {
		this.initialCapacity = builder.getInitialCapacity();
		atomicReferenceArray = new AtomicReferenceArray<>(reSize(initialCapacity));

		this.concurrencyLevel = builder.getConcurrencyLevel();
		stripedLocks = new StripedLocks(concurrencyLevel);

		this.expireAfterAccess = builder.getExpireAfterAccess();
		this.expireAfterWrite = builder.getExpireAfterWrite();
	}

	static enum CacheEntryFactory {
		STRONG {
			@Override
			<K, V> CacheEntry<K, V> newCacheEntry(K key, ValueItem<V> valueItem, int hashCode, CacheEntry<K, V> next,
					Cache<K, V> cache) {
				return new StrongCacheEntry<>(key, valueItem, hashCode, next);
			}

		},
		WEAK {
			@Override
			<K, V> CacheEntry<K, V> newCacheEntry(K key, ValueItem<V> valueItem, int hashCode, CacheEntry<K, V> next,
					Cache<K, V> cache) {
				return null;
			}
		};

		abstract <K, V> CacheEntry<K, V> newCacheEntry(K key, ValueItem<V> valueItem, int hashCode,
				CacheEntry<K, V> next, Cache<K, V> cache);
	}

	static class StrongCacheEntry<K, V> implements CacheEntry<K, V> {

		final K key;
		ValueItem<V> valueItem;
		int hashCode;
		CacheEntry<K, V> next;

		long lastAccessTime;
		long lastWriteTime;

		StrongCacheEntry(K key, ValueItem<V> valueItem, int hashCode, CacheEntry<K, V> next) {
			this.key = key;
			this.hashCode = hashCode;
			this.next = next;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public CacheEntry<K, V> getNext() {
			return next;
		}

		@Override
		public ValueItem<V> getValueItem() {
			return valueItem;
		}

		@Override
		public int getHashCode() {
			return hashCode;
		}

		@Override
		public long getLastAccessTime() {
			return lastAccessTime;
		}

		@Override
		public long getLastWriteTime() {
			return lastWriteTime;
		}

		@Override
		public void setLastAccessTime(long lastAccessTime) {
			this.lastAccessTime = lastAccessTime;
		}

		@Override
		public void setLastWriteTime(long lastWriteTime) {
			this.lastWriteTime = lastWriteTime;
		}

	}

	static class WeakCacheEntry<K, V> extends WeakReference<K> implements CacheEntry<K, V> {

		ValueItem<V> valueItem;
		int hashCode;
		CacheEntry<K, V> next;

		long lastAccessTime;
		long lastWriteTime;

		WeakCacheEntry(K key, ValueItem<V> valueItem, int hashCode, CacheEntry<K, V> next,
				ReferenceQueue<? super K> referenceQueue) {
			super(key, referenceQueue);
			this.hashCode = hashCode;
			this.next = next;
		}

		@Override
		public K getKey() {
			return get();
		}

		@Override
		public CacheEntry<K, V> getNext() {
			return next;
		}

		@Override
		public ValueItem<V> getValueItem() {
			return valueItem;
		}

		@Override
		public int getHashCode() {
			return hashCode;
		}

		@Override
		public long getLastAccessTime() {
			return lastAccessTime;
		}

		@Override
		public long getLastWriteTime() {
			return lastWriteTime;
		}

		@Override
		public void setLastAccessTime(long lastAccessTime) {
			this.lastAccessTime = lastAccessTime;
		}

		@Override
		public void setLastWriteTime(long lastWriteTime) {
			this.lastWriteTime = lastWriteTime;
		}

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

	static final int reSize(int size) {
		size |= size >> 1;
		size |= size >> 2;
		size |= size >> 4;
		size |= size >> 8;
		size |= size >> 16;
		return size > MAXIMUM_SIZE ? MAXIMUM_SIZE : size + 1;
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
