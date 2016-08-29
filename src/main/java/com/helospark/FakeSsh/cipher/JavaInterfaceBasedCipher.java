package com.helospark.FakeSsh.cipher;

import javax.crypto.Cipher;

/**
 * Cipher that uses Java's {@link javax.crypto.Cipher}.
 * @author helospark
 */
public class JavaInterfaceBasedCipher implements SshCipher {
	private Cipher encryptCipher;
	private Cipher decryptCipher;
	byte[] key, iv;

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
			try {
				return encryptCipher.update(data);
			} catch (Exception e) {
				throw new RuntimeException("Unable to encrypt", e);
			}
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
			try {
				return decryptCipher.update(data);
			} catch (Exception e) {
				throw new RuntimeException("Unable to decypher", e);
			}
		}
	}

	@Override
	public int getBlockSize() {
		return encryptCipher.getBlockSize();
	}
}
