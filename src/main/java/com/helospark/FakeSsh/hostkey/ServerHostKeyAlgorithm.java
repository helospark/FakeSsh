package com.helospark.FakeSsh.hostkey;

import com.helospark.FakeSsh.domain.SshString;

public interface ServerHostKeyAlgorithm {

	public SshString providePublicKey();

	public byte[] sign(byte[] hash) throws Exception;

	public String getSignatureName();

}
