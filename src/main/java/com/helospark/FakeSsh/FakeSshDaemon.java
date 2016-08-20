package com.helospark.FakeSsh;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FakeSshDaemon {
	private static final int SERVER_PORT = 2222;
	private boolean isRunning = true;
	private ServerSocket serverSocket;
	private List<SshConnection> connections = new ArrayList<>();
	private List<Runnable> tmp = new ArrayList<>();
	private SshIdentificationExchanger identificationDataExchanger;

	@Autowired
	public FakeSshDaemon(SshIdentificationExchanger identificationDataExchanger) throws IOException {
		serverSocket = new ServerSocket(SERVER_PORT);
		this.identificationDataExchanger = identificationDataExchanger;
	}

	public void run() throws IOException {
		while (isRunning) {
			Socket connection = serverSocket.accept();
			final SshConnection sshConnection = new SshConnection();
			ReadWriteSocketConnection readWriteConnection = new ReadWriteSocketConnection(connection);
			sshConnection.setConnection(readWriteConnection);
			new Runnable() {
				@Override
				public void run() {
					identificationDataExchanger.exchangeIdentification(sshConnection);
				}
			}.run();
			connections.add(sshConnection);
		}
	}
}
