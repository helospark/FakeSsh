package com.helospark.FakeSsh.cipher.implementation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.cipher.AbstractJavaInterfaceBasedCipherFactory;
import com.helospark.FakeSsh.cipher.SshCipher;

@Component
@Order(0)
public class Aes128CtrCipherFactory extends AbstractJavaInterfaceBasedCipherFactory {
	private static final String CIPHER_IDENTIFIER = "aes128-ctr";
	private static final String JAVA_ALGORITHM_NAME = "AES";
	private static final String JAVA_CIPHER_TYPE = "AES/CTR/NoPadding";

	@Override
	public SshCipher createCipher(byte[] key, byte[] iv) {
		return super.createJavaInterfaceBasedCipher(JAVA_CIPHER_TYPE, JAVA_ALGORITHM_NAME, key, iv);
	}

	@Override
	public int getIvLength() {
		return 16;
	}

	@Override
	public int getKeyLength() {
		return 16;
	}

	@Override
	public String getName() {
		return CIPHER_IDENTIFIER;
	}

}
