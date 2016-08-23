package com.helospark.FakeSsh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Builds the thread for the SshConnection.
 * @author helospark
 */
@Component
public class SshConnectionThreadFactory {
	private SshStartState sshStartState;

	@Autowired
	public SshConnectionThreadFactory(@Qualifier(StateNames.START_STATE) SshStartState startState) {
		this.sshStartState = startState;
	}

	public Thread createSshConnectionThread(SshConnection sshConnection) {
		FakeSshConnectionRunnable fakeSshConnectionRunnable = new FakeSshConnectionRunnable(sshStartState, sshConnection);
		return new Thread(fakeSshConnectionRunnable);
	}
}
