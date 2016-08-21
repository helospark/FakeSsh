package com.helospark.FakeSsh;

import javax.crypto.Cipher;

/**
 * Cipher that uses Java's {@link javax.crypto.Cipher}.
 * @author helospark
 */
public class JavaInterfaceBasedCipher implements SshCipher {
	private Cipher encryptCipher;
	private Cipher decryptCipher;

	/**
	 * Constructor.
	 * @param encryptCipher to encrypt
	 * @param decryptCipher to decrypt
	 */
	public JavaInterfaceBasedCipher(Cipher encryptCipher, Cipher decryptCipher) {
		this.encryptCipher = encryptCipher;
		this.decryptCipher = decryptCipher;
	}

	/**
	 * Encrypts the given data.
	 * This implementation is thread safe
	 * @param data to encrypt
	 */
	@Override
	public byte[] encrypt(byte[] data) {
		if (data.length % encryptCipher.getBlockSize() != 0) {
			throw new RuntimeException("Data is not multiple of block size");
		}
		synchronized (encryptCipher) {
			return encryptCipher.update(data);
		}
	}

	/**
	 * Decrypt the given data.
	 * This implementation is thread safe
	 * @param data to decrypt
	 */
	@Override
	public byte[] decrypt(byte[] data) {
		synchronized (decryptCipher) {
			return decryptCipher.update(data);
		}
	}

	@Override
	public int getBlockSize() {
		return encryptCipher.getBlockSize();
	}

}
