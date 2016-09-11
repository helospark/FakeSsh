package com.helospark.FakeSsh.hmac;

/**
 * Factory to create {@link SshMac}.
 * @author helospark
 */
public interface HmacFactory {

	public SshMac createMac(byte[] key);

	public int getKeyLength();

	public String getName();
}
