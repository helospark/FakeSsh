package com.helospark.FakeSsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

/**
 * Creates SSH HMAC from the given name and key.
 * @author helospark
 */
@Component
public class SshMacFactory {
	private static final int SHA1_BLOCK_SIZE_IN_BYTES = 64;

	/**
	 * Create SSH HMAC from the given name and key
	 * @param macName name of the HMAC algorithm
	 * @param key for the HMAC algorithm
	 * @return the created SshMac
	 * @throws RuntimeException when no SshMac algorithm found
	 */
	public SshMac createMac(String macName, byte[] key) throws NoSuchAlgorithmException {
		switch (macName) {
		case "hmac-sha1":
			MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm = new MessageDigestBackedHMacAlgorithm(MessageDigest.getInstance("SHA-1"), key, SHA1_BLOCK_SIZE_IN_BYTES);
			return new MessageDigestBackedSshHmac(messageDigestBackedHMacAlgorithm);
		default:
			throw new RuntimeException("No HMAC found for the given " + macName);
		}
	}

}
