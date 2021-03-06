package com.helospark.FakeSsh;

import java.io.IOException;
import java.net.Socket;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

/**
 * Starts the sshConnections.
 * @author helospark
 */
@Component
public class SshConnectionInitializer {
	private SshConnectionFactory sshConnectionFactory;
	private SshConnectionThreadFactory sshConnectionThreadFactory;

	@Autowired
	public SshConnectionInitializer(SshConnectionFactory sshConnectionFactory, SshConnectionThreadFactory sshConnectionThreadFactory) {
		this.sshConnectionFactory = sshConnectionFactory;
		this.sshConnectionThreadFactory = sshConnectionThreadFactory;
	}

	public SshConnection createSshConnection(Socket connection) throws IOException {
		SshConnection sshConnection = sshConnectionFactory.createSshConnection(connection);
		Thread thread = sshConnectionThreadFactory.createSshConnectionThread(sshConnection);
		thread.start();
		return sshConnection;
	}
}
