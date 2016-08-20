package com.helospark.FakeSsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class SshMacFactory {
	private static final int SHA1_BLOCK_SIZE_IN_BYTES = 20;

	public SshMac createMac(String macAlgorithmsClientToServer, byte[] integrityKeyClientToServer) throws NoSuchAlgorithmException {
		switch (macAlgorithmsClientToServer) {
		case "hmac-sha1":
			return new MessageDigestBackedMacAlgorithm(MessageDigest.getInstance("SHA-1"), integrityKeyClientToServer, SHA1_BLOCK_SIZE_IN_BYTES);
		}
		return null;
	}

}
