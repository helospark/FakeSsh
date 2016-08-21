package com.helospark.FakeSsh.configuration;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for random numbers.
 * @author helospark
 */
@Configuration
public class RandomConfiguration {

	@Bean(name = "secureRandom")
	public SecureRandom createSecureRandom() {
		return new SecureRandom();
	}
}
