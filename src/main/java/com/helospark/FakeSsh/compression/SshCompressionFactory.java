package com.helospark.FakeSsh.compression;

public interface SshCompressionFactory {

	public SshCompression create();

	public String getName();
}
