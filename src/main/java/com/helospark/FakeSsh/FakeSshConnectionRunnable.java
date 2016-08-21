package com.helospark.FakeSsh;

/**
 * Thread to start the SSH process.
 * @author helospark
 */
public class FakeSshConnectionRunnable implements Runnable {
	private SshStartState sshStartState;
	private SshConnection sshConnection;

	public FakeSshConnectionRunnable(SshStartState sshStartState, SshConnection sshConnection) {
		this.sshStartState = sshStartState;
		this.sshConnection = sshConnection;
	}

	@Override
	public void run() {
		sshStartState.enterState(sshConnection);
	}

}
