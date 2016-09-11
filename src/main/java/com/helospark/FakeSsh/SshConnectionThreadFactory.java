package com.helospark.FakeSsh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Builds the thread for the SshConnection.
 * @author helospark
 */
@Component
public class SshConnectionThreadFactory {
	private SshStateMachine sshStateMachine;

	@Autowired
	public SshConnectionThreadFactory(SshStateMachine sshStateMachine) {
		this.sshStateMachine = sshStateMachine;
	}

	public Thread createSshConnectionThread(SshConnection sshConnection) {
		FakeSshConnectionRunnable fakeSshConnectionRunnable = new FakeSshConnectionRunnable(sshStateMachine, sshConnection);
		return new Thread(fakeSshConnectionRunnable);
	}
}
