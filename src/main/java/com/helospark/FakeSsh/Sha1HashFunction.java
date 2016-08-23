package com.helospark.FakeSsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

/**
 * Hash function that uses SHA-1.
 * @author helospark
 */
@Component
public class Sha1HashFunction implements HashFunction {

	@Override
	public byte[] hash(byte[] value) {
		try {
			return MessageDigest.getInstance("SHA-1").digest(value);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No SHA1 found");
		}
	}
}
