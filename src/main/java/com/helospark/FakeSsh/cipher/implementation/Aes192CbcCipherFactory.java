package com.helospark.FakeSsh.cipher.implementation;

import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.cipher.AbstractJavaInterfaceBasedCipherFactory;
import com.helospark.FakeSsh.cipher.SshCipher;

@Component
@Order(4)
public class Aes192CbcCipherFactory extends AbstractJavaInterfaceBasedCipherFactory {
	private static final String CIPHER_IDENTIFIER = "aes192-cbc";
	private static final String JAVA_ALGORITHM_NAME = "AES";
	private static final String JAVA_CIPHER_TYPE = "AES/CBC/NoPadding";

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
		return 24;
	}

	@Override
	public String getName() {
		return CIPHER_IDENTIFIER;
	}

}
