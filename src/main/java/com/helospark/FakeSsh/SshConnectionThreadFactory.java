package com.helospark.FakeSsh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Builds the thread for the SshConnection.
 * @author helospark
 */
@Component
public class SshConnectionThreadFactory {
	private StateMachineHandler stateMachineHandler;

	@Autowired
	public SshConnectionThreadFactory(StateMachineHandler stateMachineHandler) {
		this.stateMachineHandler = stateMachineHandler;
	}

	public Thread createSshConnectionThread(SshConnection sshConnection) {
		FakeSshConnectionRunnable fakeSshConnectionRunnable = new FakeSshConnectionRunnable(stateMachineHandler, sshConnection);
		return new Thread(fakeSshConnectionRunnable);
	}
}
