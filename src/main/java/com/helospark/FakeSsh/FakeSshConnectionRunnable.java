package com.helospark.FakeSsh;

import java.io.IOException;

/**
 * Thread to start the SSH process.
 * @author helospark
 */
public class FakeSshConnectionRunnable implements Runnable {
	private StateMachineHandler stateMachineHandler;
	private SshConnection sshConnection;

	public FakeSshConnectionRunnable(StateMachineHandler stateMachineHandler, SshConnection sshConnection) {
		this.stateMachineHandler = stateMachineHandler;
		this.sshConnection = sshConnection;
	}

	@Override
	public void run() {
		try {
			stateMachineHandler.handle(sshConnection);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
