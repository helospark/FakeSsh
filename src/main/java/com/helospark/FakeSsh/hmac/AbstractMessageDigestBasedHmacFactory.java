package com.helospark.FakeSsh.hmac;

import java.security.MessageDigest;

public abstract class AbstractMessageDigestBasedHmacFactory implements HmacFactory {

	protected SshMac createMessageDigestBackedHMac(String algorithmName, int blockSize, int macSize, byte[] key) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);
			MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm = new MessageDigestBackedHMacAlgorithm(messageDigest, key, blockSize, macSize);
			return new MessageDigestBackedSshHmac(messageDigestBackedHMacAlgorithm);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create mac algorithm", e);
		}
	}

}
