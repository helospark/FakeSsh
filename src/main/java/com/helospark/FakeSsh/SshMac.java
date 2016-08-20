package com.helospark.FakeSsh;

import java.io.IOException;

public interface SshMac {
	public byte[] createMac(byte[] message, int packageNumber) throws IOException;

	public boolean checkMac(byte[] message, byte[] mac, int packageNumber) throws IOException;

	public int getMacLength();
}
