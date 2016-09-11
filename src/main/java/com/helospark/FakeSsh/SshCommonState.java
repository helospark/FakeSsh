package com.helospark.FakeSsh;

/**
 * Interface for states that can occur any time.
 * @author helospark
 */
public interface SshCommonState extends SshState {

	/**
	 * Can this handler handle the given packet.
	 * @param readPackage to check
	 * @return true if this handler can handle it
	 */
	public boolean canHandle(byte[] readPackage);
}
