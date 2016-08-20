package com.helospark.FakeSsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class SshHashFactory {

	public SshHash createForKeyExchangeMethod(String kexAlgorithm) throws NoSuchAlgorithmException {
		if (kexAlgorithm.contains("sha1")) {
			return new MessageDigestBackedSshHashed(MessageDigest.getInstance("SHA-1"));
		}
		return null;
	}

}
