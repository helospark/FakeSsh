package com.helospark.FakeSsh.configuration;

import java.security.SecureRandom;

import com.helospark.lightdi.annotation.Bean;
import com.helospark.lightdi.annotation.Configuration;

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
