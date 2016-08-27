package com.helospark.FakeSsh.cipher;

public interface CipherFactory {

	public SshCipher createCipher(byte[] key, byte[] iv);

	public int getIvLength();

	public int getKeyLength();

	public String getName();
}
