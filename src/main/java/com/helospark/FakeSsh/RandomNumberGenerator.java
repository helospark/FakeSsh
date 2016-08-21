package com.helospark.FakeSsh;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Generates random numbers.
 * @author helospark
 */
@Component
public class RandomNumberGenerator {
	private SecureRandom random;

	@Autowired
	public RandomNumberGenerator(SecureRandom random) {
		this.random = random;
	}

	/**
	 * Generate the given amount of random bytes.
	 * @param numberOfBytes to generate
	 * @return generated random bytes
	 */
	public byte[] generateRandomBytes(int numberOfBytes) {
		byte[] result = new byte[numberOfBytes];
		random.nextBytes(result);
		return result;
	}
}
