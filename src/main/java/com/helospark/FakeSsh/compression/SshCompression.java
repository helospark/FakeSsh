package com.helospark.FakeSsh.compression;

public interface SshCompression {

	public byte[] compress(byte[] data);

	public byte[] decompress(byte[] data);

	public String getName();
}
