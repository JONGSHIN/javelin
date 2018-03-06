package com.vkim.javelin;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StripedLocks {
	static final int MAXIMUM_SIZE = 1 << 30;

	private final Lock[] locks;

	public StripedLocks(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("illegal size");
		}
		locks = new ReentrantLock[reSize(size)];
		Arrays.fill(locks, new ReentrantLock());
	}

	private static final int reSize(int size) {
		size |= size >> 1;
		size |= size >> 2;
		size |= size >> 4;
		size |= size >> 8;
		size |= size >> 16;
		return size > MAXIMUM_SIZE ? MAXIMUM_SIZE : size + 1;
	}

	/**
	 * Computes key.hashCode() and spreads (XORs) higher bits of hash to lower.
	 * Because the table uses power-of-two masking, sets of hashes that vary
	 * only in bits above the current mask will always collide. (Among known
	 * examples are sets of Float keys holding consecutive whole numbers in
	 * small tables.) So we apply a transform that spreads the impact of higher
	 * bits downward. There is a tradeoff between speed, utility, and quality of
	 * bit-spreading. Because many common sets of hashes are already reasonably
	 * distributed (so don't benefit from spreading), and because we use trees
	 * to handle large sets of collisions in bins, we just XOR some shifted bits
	 * in the cheapest possible way to reduce systematic lossage, as well as to
	 * incorporate impact of the highest bits that would otherwise never be used
	 * in index calculations because of table bounds.
	 */
	private static final int reHash(int hashCode) {
		int h;
		return (h = hashCode) ^ (h >>> 16);
	}

	public final Lock getLock(int hash) {
		int index = reHash(hash) & locks.length - 1;
		return locks[index];
	}

}
