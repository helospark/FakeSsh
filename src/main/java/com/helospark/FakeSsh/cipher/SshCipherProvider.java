package com.helospark.FakeSsh.cipher;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates the cipher based on the String representation.
 * @author helospark
 */
@Component
public class SshCipherProvider {
	private List<CipherFactory> cipherFactories;

	@Autowired
	public SshCipherProvider(List<CipherFactory> cipherFactories) {
		this.cipherFactories = cipherFactories;
	}

	/**
	 * Create cipher based on it's name.
	 * @param name of the cipher
	 * @param key the encryption key
	 * @param iv the initialization vector
	 * @return created cipher
	 * @throws RuntimeException if no cipher matches
	 */
	public SshCipher createCipher(String name, byte[] key, byte[] iv) {
		CipherFactory cipherFactory = findCipherFactoryByName(name);
		return cipherFactory.createCipher(key, iv);
	}

	public int getKeySize(String name) {
		return findCipherFactoryByName(name).getKeyLength();
	}

	public int getIvSize(String name) {
		return findCipherFactoryByName(name).getIvLength();
	}

	public List<String> getAvailableAlgorithmNames() {
		return cipherFactories.stream()
				.map(cipherFactory -> cipherFactory.getName())
				.collect(Collectors.toList());
	}

	private CipherFactory findCipherFactoryByName(String name) {
		return cipherFactories.stream()
				.filter(cipherFactory -> cipherFactory.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No cipher found for " + name));
	}

}
