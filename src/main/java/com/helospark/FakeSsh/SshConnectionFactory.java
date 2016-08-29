package com.helospark.FakeSsh;

import java.io.IOException;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.io.ReadWriteSocketConnection;

/**
 * Builds the Ssh connection.
 * @author helospark
 */
@Component
public class SshConnectionFactory {

	public SshConnection createSshConnection(Socket socket) throws IOException {
		SshConnection sshConnection = new SshConnection();
		ReadWriteSocketConnection readWriteConnection = new ReadWriteSocketConnection(socket);
		sshConnection.setConnection(readWriteConnection);
		return sshConnection;
	}
}
