package com.helospark.FakeSsh;

/**
 * Result of executing a state.
 * @author helospark
 */
public enum StateMachineResult {
	/**
	 * Connection was closed.
	 */
	CLOSED,
	/**
	 * Successully handled message, could be passed to the next state
	 */
	SUCCESS,
	/**
	 * Should repeat the current state
	 */
	REPEAT_STATE,
	/**
	 * This message handler was unable to handle state
	 */
	NOT_HANDLED
}
