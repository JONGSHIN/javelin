package com.vkim.javelin;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;

public class JavelinCache<K, V> extends AbstractCache<K, V> {

	private LinkedHashMap<KeyReference<K>, V> data;
	private StripedLocks stripedLocks;

	private int initialCapacity;
	private int concurrencyLevel;
	private long expireAfterAccess;
	private long expireAfterWrite;

	public static enum ReferencePower {
		WEAK {
			@Override
			<K> KeyReference<K> newKeyReference(K key) {
				return new WeakKeyReference<K>(key);
			}
		},
		STRONG {
			@Override
			<K> KeyReference<K> newKeyReference(K key) {
				return null;
			}
		};

		abstract <K> KeyReference<K> newKeyReference(K key);
	}

	public interface KeyReference<K> {

		K getKey();

		int getHashCode();

		void setLastAccessTime(long lastAccessTime);

		long getLastAccessTime();

		void setLastWriteTime(long lastWriteTime);

		long getLastWriteTime(long lastWriteTimne);
	}

	static final class WeakKeyReference<K> implements KeyReference<K> {
		final K key;
		final int hashCode;

		long lastAccessTime;
		long lastWriteTime;

		WeakKeyReference(K key) {
			this.key = key;
			this.hashCode = key.hashCode();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + hashCode;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("rawtypes")
			WeakKeyReference other = (WeakKeyReference) obj;
			if (hashCode != other.hashCode)
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}

		@Override
		public K getKey() {
			return null;
		}

		@Override
		public int getHashCode() {
			return 0;
		}

		@Override
		public void setLastAccessTime(long lastAccessTime) {
			this.lastAccessTime = lastAccessTime;
		}

		@Override
		public long getLastAccessTime() {
			return lastAccessTime;
		}

		@Override
		public void setLastWriteTime(long lastWriteTime) {
			this.lastWriteTime = lastWriteTime;
		}

		@Override
		public long getLastWriteTime(long lastWriteTime) {
			return lastWriteTime;
		}
	}

	public JavelinCache(CacheBuilder builder) {
		this.initialCapacity = builder.getInitialCapacity();
		data = new LinkedHashMap<>(initialCapacity);

		this.concurrencyLevel = builder.getConcurrencyLevel();
		stripedLocks = new StripedLocks(concurrencyLevel);

		this.expireAfterAccess = builder.getExpireAfterAccess();
		this.expireAfterWrite = builder.getExpireAfterWrite();
	}

	@Override
	public void put(K key, V value) {
		Lock lock = stripedLocks.getLock(key.hashCode());
		KeyReference<K> keyReference = ReferencePower.WEAK.newKeyReference(key);
		lock.lock();
		try {
			keyReference.setLastWriteTime(System.nanoTime());
			data.put(keyReference, value);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public V get(K key) {
		KeyReference<K> keyReference = ReferencePower.WEAK.newKeyReference(key);
		return data.get(keyReference);
	}

	@Override
	public V invalidate(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V getOrLoad(K key, CacheLoader<K, V> loader) {
		// TODO Auto-generated method stub
		return null;
	}

}
