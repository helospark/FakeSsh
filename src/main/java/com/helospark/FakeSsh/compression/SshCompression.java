package com.helospark.FakeSsh.compression;

/**
 * Interface for SSH compression operations.
 * @author helospark
 */
public interface SshCompression {

	public byte[] compress(byte[] data);

	public byte[] decompress(byte[] data);

}
