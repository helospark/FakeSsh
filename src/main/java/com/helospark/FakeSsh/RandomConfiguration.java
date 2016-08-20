package com.helospark.FakeSsh;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomConfiguration {

	@Bean(name = "secureRandom")
	public SecureRandom createSecureRandom() {
		return new SecureRandom();
	}
}
