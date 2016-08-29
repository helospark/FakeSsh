package com.helospark.FakeSsh;

public interface SshCommonState extends SshState {

	public boolean canHandle(byte[] previousPackage);
}
