package com.helospark.FakeSsh;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Daemon to orchestrate the connections to the Fake SSH service. 
 * @author helospark
 */
@Component
public class FakeSshDaemon {
	private boolean isRunning = true;
	private ServerSocket serverSocket;
	private List<SshConnection> establishedConnections = new ArrayList<>();
	private ConnectionPermissionCheckingService connectionPermissionCheckingService;
	private SshConnectionInitializer sshConnectionInitializer;

	@Autowired
	public FakeSshDaemon(ConnectionPermissionCheckingService connectionPermissionCheckingService, SshConnectionInitializer sshConnectionInitializer,
			@Qualifier("fakeSshServerSocket") ServerSocket serverSocket) throws IOException {
		this.serverSocket = serverSocket;
		this.connectionPermissionCheckingService = connectionPermissionCheckingService;
		this.sshConnectionInitializer = sshConnectionInitializer;
	}

	public void run() throws IOException {
		while (isRunning()) {
			Socket newConnection = serverSocket.accept();
			if (connectionPermissionCheckingService.isConnectionAllowed(establishedConnections, newConnection)) {
				SshConnection sshConnection = sshConnectionInitializer.createSshConnection(newConnection);
				establishedConnections.add(sshConnection);
			} else {
				newConnection.close();
			}
			clearOldConnections();
		}
	}

	private void clearOldConnections() {
		establishedConnections = establishedConnections.stream()
				.filter(connection -> !connection.isConnectionClosed())
				.collect(Collectors.toList());
	}

	// TODO: When to turn of running?
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	private boolean isRunning() {
		return isRunning;
	}
}
