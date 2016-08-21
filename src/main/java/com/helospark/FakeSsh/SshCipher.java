package com.helospark.FakeSsh;

/**
 * Cipher to encrypt the SSH data stream.
 * @author helospark
 *
 */
public interface SshCipher {
	/**
	 * Encrypt data.
	 * @param data to encrypt
	 * @return encrypted data
	 */
	public byte[] encrypt(byte[] data);

	/**
	 * Decrypt data.
	 * @param data to decrypt
	 * @return decrypted data
	 */
	public byte[] decrypt(byte[] data);

	/**
	 * Block size of the underlying encryption algorithm.
	 * @return the block size
	 */
	public int getBlockSize();
}
