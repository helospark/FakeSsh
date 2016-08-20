package com.helospark.FakeSsh;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomNumberGenerator {
	private SecureRandom random;

	@Autowired
	public RandomNumberGenerator(SecureRandom random) {
		this.random = random;
	}

	public byte[] generateRandomBytes(int numberOfBytes) {
		byte[] result = new byte[numberOfBytes];
		random.nextBytes(result);
		return result;
	}
}
