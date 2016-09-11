package com.helospark.FakeSsh;

import java.io.IOException;

/**
 * Thread to start the SSH process.
 * @author helospark
 */
public class FakeSshConnectionRunnable implements Runnable {
	private SshStateMachine sshStateMachine;
	private SshConnection sshConnection;

	public FakeSshConnectionRunnable(SshStateMachine sshStateMachine, SshConnection sshConnection) {
		this.sshStateMachine = sshStateMachine;
		this.sshConnection = sshConnection;
	}

	@Override
	public void run() {
		try {
			sshStateMachine.handle(sshConnection);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
