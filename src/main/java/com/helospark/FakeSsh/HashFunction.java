package com.helospark.FakeSsh;

/**
 * Interface for SSH hash functions.
 * @author helospark
 */
public interface HashFunction {

	/**
	 * Hash the given data.
	 * @param value to hash
	 * @return hashed data
	 */
	byte[] hash(byte[] value);
}
