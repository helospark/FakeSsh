package com.helospark.FakeSsh;

public interface SshCipher {
	public byte[] encrypt(byte[] data);

	public byte[] decrypt(byte[] data);

	public int getBlockSize();
}
