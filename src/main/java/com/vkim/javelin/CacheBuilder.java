package com.vkim.javelin;

import com.vkim.javelin.JavelinCache.ReferencePower;

public class CacheBuilder {

	static final long UNSET_LONG = -1;
	static final int UNSET_INT = -1;
	private static final int MAXIMUM_CAPACITY = 1 << 30;

	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
	private static final long DEFAULT_EXPIRATION_NANOS = 0;

	private int initialCapacity = UNSET_INT;
	private int concurrencyLevel = UNSET_INT;
	private long expireAfterAccess = UNSET_LONG;
	private long expireAfterWrite = UNSET_LONG;
	private ReferencePower keyPower = ReferencePower.WEAK;

	public CacheBuilder initialCapacity(int initialCapacity) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("illegal initialCapacity");
		}
		this.initialCapacity = initialCapacity;
		return this;
	}

	public int getInitialCapacity() {
		return (initialCapacity == UNSET_INT || initialCapacity > MAXIMUM_CAPACITY) ? DEFAULT_INITIAL_CAPACITY
				: initialCapacity;
	}

	public CacheBuilder concurrencyLevel(int concurrencyLevel) {
		if (concurrencyLevel < 0) {
			throw new IllegalArgumentException("illegal concurrencyLevel ");
		}
		this.concurrencyLevel = concurrencyLevel;
		return this;
	}

	public int getConcurrencyLevel() {
		return concurrencyLevel == UNSET_INT ? DEFAULT_CONCURRENCY_LEVEL : concurrencyLevel;
	}

	public CacheBuilder setKeyPower(ReferencePower keyPower) {
		if (keyPower == null) {
			throw new NullPointerException();
		}
		this.keyPower = keyPower;
		return this;
	}

	public ReferencePower getKeyPower() {
		return keyPower;
	}

	public CacheBuilder setExpireAfterAccess(long expireAfterAccess) {
		if (expireAfterAccess < 0) {
			throw new IllegalArgumentException("illegal expireAfterAccess ");
		}
		this.expireAfterAccess = expireAfterAccess;
		return this;
	}

	long getExpireAfterAccess() {
		return expireAfterAccess == UNSET_LONG ? DEFAULT_EXPIRATION_NANOS : expireAfterAccess;
	}

	public CacheBuilder setExpireAfterWrite(long expireAfterWrite) {
		if (expireAfterWrite < 0) {
			throw new IllegalArgumentException("illegal expireAfterWrite");
		}
		this.expireAfterWrite = expireAfterWrite;
		return this;
	}

	public long getExpireAfterWrite() {
		return expireAfterWrite == UNSET_LONG ? DEFAULT_EXPIRATION_NANOS : expireAfterWrite;
	}

	public <K, V> Cache<K, V> build() {
		return new JavelinCache<K, V>(this);
	}

}
