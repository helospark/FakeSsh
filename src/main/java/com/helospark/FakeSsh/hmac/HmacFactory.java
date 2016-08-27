package com.helospark.FakeSsh.hmac;

public interface HmacFactory {

	public SshMac createMac(byte[] key);

	public int getKeyLength();

	public String getName();
}
