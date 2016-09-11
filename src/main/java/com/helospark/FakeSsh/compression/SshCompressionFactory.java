package com.helospark.FakeSsh.compression;

/**
 * Factory to create {@link SshCompression}.
 * Note: Compression is stateful, so it has to be created for each connection separately
 * @author helospark
 */
public interface SshCompressionFactory {

	public SshCompression create();

	public String getName();
}
