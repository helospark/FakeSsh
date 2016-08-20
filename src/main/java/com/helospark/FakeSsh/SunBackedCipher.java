package com.helospark.FakeSsh;

import javax.crypto.Cipher;

public class SunBackedCipher implements SshCipher {
	private Cipher encryptCipher;
	private Cipher decryptCipher;

	public SunBackedCipher(Cipher encryptCipher, Cipher decryptCipher) {
		this.encryptCipher = encryptCipher;
		this.decryptCipher = decryptCipher;
	}

	@Override
	public byte[] encrypt(byte[] data) {
		if (data.length % encryptCipher.getBlockSize() != 0) {
			throw new RuntimeException("Data is not multiple of block size");
		}
		synchronized (encryptCipher) {
			byte[] result = encryptCipher.update(data);
			return result;
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		synchronized (decryptCipher) {
			byte[] result = decryptCipher.update(data);
			return result;
		}
	}

	@Override
	public int getBlockSize() {
		return encryptCipher.getBlockSize();
	}

}
