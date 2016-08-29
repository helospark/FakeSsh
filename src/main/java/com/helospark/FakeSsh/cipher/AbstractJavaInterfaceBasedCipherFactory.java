package com.helospark.FakeSsh.cipher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractJavaInterfaceBasedCipherFactory implements CipherFactory {

	protected SshCipher createJavaInterfaceBasedCipher(String cipherAlgorithm, String algorithm, byte[] key, byte[] iv) {
		try {
			assertKeySizeIsLessThanMaximum(cipherAlgorithm, key.length);
			Cipher encryptCipher = createCipher(cipherAlgorithm, algorithm, Cipher.ENCRYPT_MODE, key, iv);
			Cipher decryptCipher = createCipher(cipherAlgorithm, algorithm, Cipher.DECRYPT_MODE, key, iv);
			return new JavaInterfaceBasedCipher(encryptCipher, decryptCipher);
		} catch (Exception e) {
			throw new RuntimeException("Unable to create cipher", e);
		}
	}

	private void assertKeySizeIsLessThanMaximum(String cipherAlgorithm, int length) throws NoSuchAlgorithmException {
		if (length > Cipher.getMaxAllowedKeyLength(cipherAlgorithm)) {
			throw new RuntimeException("Key length is too large, probably because of jurisdiction policy file limits");
		}
	}

	public Cipher createCipher(String name, String algorithm, int mode, byte[] key, byte[] iv) throws Exception {
		Cipher encryptCipher = Cipher.getInstance(name);
		SecretKey keyValue = new SecretKeySpec(key, algorithm);
		AlgorithmParameterSpec IVspec = new IvParameterSpec(iv);
		encryptCipher.init(mode, keyValue, IVspec);
		return encryptCipher;
	}

}
