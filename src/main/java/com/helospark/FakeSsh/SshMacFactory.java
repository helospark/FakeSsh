package com.helospark.FakeSsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class SshMacFactory {
	private static final int SHA1_BLOCK_SIZE_IN_BYTES = 64;

	public SshMac createMac(String macAlgorithmsClientToServer, byte[] integrityKeyClientToServer) throws NoSuchAlgorithmException {
		switch (macAlgorithmsClientToServer) {
		case "hmac-sha1":
			MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm = new MessageDigestBackedHMacAlgorithm(MessageDigest.getInstance("SHA-1"), integrityKeyClientToServer, SHA1_BLOCK_SIZE_IN_BYTES);
			return new MessageDigestBackedSshHmac(messageDigestBackedHMacAlgorithm);
		}
		return null;
	}

}
