package com.helospark.FakeSsh.compression.implementation;

import com.helospark.FakeSsh.compression.SshCompression;

/**
 * Implementation of {@link SshCompression} that does no compression.
 * Using the NullObject pattern
 * @author helospark
 */
public class NoneSshCompression implements SshCompression {

	@Override
	public byte[] compress(byte[] data) {
		return data;
	}

	@Override
	public byte[] decompress(byte[] data) {
		return data;
	}

}
