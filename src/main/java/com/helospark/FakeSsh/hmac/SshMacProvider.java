package com.helospark.FakeSsh.hmac;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates SSH HMAC from the given name and key.
 * @author helospark
 */
@Component
public class SshMacProvider {
	private List<HmacFactory> hmacFactories;

	@Autowired
	public SshMacProvider(List<HmacFactory> hmacFactories) {
		this.hmacFactories = hmacFactories;
	}

	/**
	 * Create SSH HMAC from the given name and key
	 * @param hmacName name of the HMAC algorithm
	 * @param key for the HMAC algorithm
	 * @return the created SshMac
	 * @throws RuntimeException when no SshMac algorithm found
	 */
	public SshMac createMac(String hmacName, byte[] key) throws NoSuchAlgorithmException {
		HmacFactory factory = findHmacFactoryByName(hmacName);
		return factory.createMac(key);
	}

	private HmacFactory findHmacFactoryByName(String hmacName) {
		return hmacFactories.stream()
				.filter(hmacFactory -> hmacFactory.getName().equals(hmacName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No HMAC with " + hmacName));
	}

	public int getKeyLength(String hmacName) {
		return findHmacFactoryByName(hmacName).getKeyLength();
	}

	public List<String> getSupportedAlgorithms() {
		return hmacFactories.stream()
				.map(hmacFactory -> hmacFactory.getName())
				.collect(Collectors.toList());
	}

}
