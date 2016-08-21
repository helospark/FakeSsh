package com.helospark.FakeSsh;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

/**
 * Creates the cipher based on the String representation.
 * @author helospark
 */
@Component
public class SshCipherFactory {

	/**
	 * Create cipher based on it's String representation.
	 * @param name of the cipher
	 * @param key the encryption key
	 * @param iv the initialization vector
	 * @return created cipher
	 * @throws RuntimeException if no cipher matches
	 */
	public SshCipher createCipher(String name, byte[] key, byte[] iv) {
		try {
			return initCipher(name, key, iv);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException("Cipher initializazion failed", e);
		}
	}

	private SshCipher initCipher(String name, byte[] key, byte[] iv) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher encryptCipher;
		Cipher decryptCipher;
		switch (name) {
		case "aes128-ctr":
			encryptCipher = createEncryptionCipher("AES/CTR/NoPadding", "AES", Cipher.ENCRYPT_MODE, key, iv);
			decryptCipher = createEncryptionCipher("AES/CTR/NoPadding", "AES", Cipher.DECRYPT_MODE, key, iv);
			return new JavaInterfaceBasedCipher(encryptCipher, decryptCipher);
		case "3des-cbc":
			encryptCipher = createEncryptionCipher("DESede/CBC/PKCS5Padding", "DESede", Cipher.ENCRYPT_MODE, key, iv);
			decryptCipher = createEncryptionCipher("DESede/CBC/PKCS5Padding", "DESede", Cipher.DECRYPT_MODE, key, iv);
			return new JavaInterfaceBasedCipher(encryptCipher, decryptCipher);
		default:
			throw new RuntimeException("No cipher by " + name + " name is defined");
		}
	}

	private Cipher createEncryptionCipher(String name, String algorithm, int mode, byte[] key, byte[] iv) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher encryptCipher = Cipher.getInstance(name);
		SecretKey keyValue = new SecretKeySpec(key, algorithm);
		AlgorithmParameterSpec IVspec = new IvParameterSpec(iv);
		encryptCipher.init(mode, keyValue, IVspec);
		return encryptCipher;
	}
}
