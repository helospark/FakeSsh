package com.helospark.FakeSsh.compression.implementation;

import com.helospark.FakeSsh.compression.SshCompression;

public class NoneSshCompression implements SshCompression {

	@Override
	public byte[] compress(byte[] data) {
		return data;
	}

	@Override
	public byte[] decompress(byte[] data) {
		return data;
	}

	@Override
	public String getName() {
		return "none";
	}

}
