package com.helospark.FakeSsh;

/**
 * Hash function used in the Diffie-Hellman key exchange.
 * @author helospark
 */
public interface SshHash {

	/**
	 * Hash the given bytes.
	 * @param byteArray to hash
	 * @return hashed bytes
	 */
	byte[] hash(byte[] byteArray);

}
