package com.helospark.FakeSsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

/**
 * Creates hash for the KEX algorithm.
 * @author helospark
 */
@Component
public class SshHashFactory {
	private static final String SHA_1_DIGEST_NAME = "SHA-1";

	/**
	 * Creates a hash function for the given KEX algorithm.
	 * @param kexAlgorithm to create hash for
	 * @return the created SshHash
	 * @throws RuntimeException if no hash is found for the given algorithm
	 */
	public SshHash createForKeyExchangeMethod(String kexAlgorithm) throws NoSuchAlgorithmException {
		if (kexAlgorithm.contains("sha1")) {
			return new MessageDigestBackedSshHash(MessageDigest.getInstance(SHA_1_DIGEST_NAME));
		} else {
			throw new RuntimeException("No hash found for the given " + kexAlgorithm);
		}
	}

}
