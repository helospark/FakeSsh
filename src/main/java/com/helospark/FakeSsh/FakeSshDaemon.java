package com.helospark.FakeSsh;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.util.LoggerSupport;

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
	private LoggerSupport loggerSupport;
	private int timeout;

	@Autowired
	public FakeSshDaemon(ConnectionPermissionCheckingService connectionPermissionCheckingService, SshConnectionInitializer sshConnectionInitializer,
			@Qualifier("fakeSshServerSocket") ServerSocket serverSocket, LoggerSupport loggerSupport, @Value("${USER_TIMEOUT}") int timeout) throws IOException {
		this.serverSocket = serverSocket;
		this.connectionPermissionCheckingService = connectionPermissionCheckingService;
		this.sshConnectionInitializer = sshConnectionInitializer;
		this.loggerSupport = loggerSupport;
		this.timeout = timeout;
	}

	public void run() throws IOException {
		while (isRunning()) {
			Socket newConnection = serverSocket.accept();
			newConnection.setSoTimeout(timeout);
			if (connectionPermissionCheckingService.isConnectionAllowed(establishedConnections, newConnection)) {
				SshConnection sshConnection = sshConnectionInitializer.createSshConnection(newConnection);
				establishedConnections.add(sshConnection);
				loggerSupport.logInfoString("Created connection for " + newConnection.getInetAddress());
			} else {
				loggerSupport.logInfoString("Too many connection from " + newConnection.getInetAddress());
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
