package com.helospark.FakeSsh.hostkey;

import com.helospark.FakeSsh.domain.SshString;

/**
 * Represents the operations for the server host key.
 * @author helospark
 */
public interface ServerHostKeyAlgorithm {

	/**
	 * Get public key as String using the format described in the RFC.
	 * @return public key
	 */
	public SshString providePublicKey();

	/**
	 * Signs the given data with the private key.
	 * @param data to sign
	 * @return signature
	 * @throws Exception
	 */
	public byte[] sign(byte[] data) throws Exception;

	/**
	 * Readable name of this signature used for exchange algorithm.
	 * @return readable name
	 */
	public String getSignatureName();

}
