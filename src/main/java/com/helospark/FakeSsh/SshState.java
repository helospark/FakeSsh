package com.helospark.FakeSsh;

/**
 * State for the SSH flow state machine.
 * @author helospark
 */
public interface SshState {

	/**
	 * Enter this state.
	 * @param connection that enters this state
	 */
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage);

}
