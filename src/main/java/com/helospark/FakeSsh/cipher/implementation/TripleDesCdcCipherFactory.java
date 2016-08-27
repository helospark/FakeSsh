package com.helospark.FakeSsh.cipher.implementation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.cipher.AbstractJavaInterfaceBasedCipherFactory;
import com.helospark.FakeSsh.cipher.SshCipher;

@Component
@Order(1)
public class TripleDesCdcCipherFactory extends AbstractJavaInterfaceBasedCipherFactory {
	private static final String CIPHER_IDENTIFIER = "3des-cbc";
	private static final String JAVA_ALGORITHM_NAME = "DESede";
	private static final String JAVA_CIPHER_TYPE = "DESede/CBC/NoPadding";

	@Override
	public SshCipher createCipher(byte[] key, byte[] iv) {
		return super.createJavaInterfaceBasedCipher(JAVA_CIPHER_TYPE, JAVA_ALGORITHM_NAME, key, iv);
	}

	@Override
	public int getIvLength() {
		return 8;
	}

	@Override
	public int getKeyLength() {
		return 24;
	}

	@Override
	public String getName() {
		return CIPHER_IDENTIFIER;
	}

}
