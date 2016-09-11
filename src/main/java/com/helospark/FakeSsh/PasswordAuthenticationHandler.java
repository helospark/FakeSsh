package com.helospark.FakeSsh;

import java.io.IOException;
import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshPasswordUserAuthRequest;
import com.helospark.FakeSsh.domain.SshUserauthRequest;

/**
 * Authentication handler that handles password authentication.
 * This implementation will always rejects and logs username-password pair
 * @author helospark
 */
@Component
public class PasswordAuthenticationHandler implements AuthenticationHandler {
	private static final String SSH_PASSWORD_METHOD_NAME = "password";
	private SynchronizedFileLogger synchronizedFileLogger;

	@Autowired
	public PasswordAuthenticationHandler(SynchronizedFileLogger synchronizedFileLogger) {
		this.synchronizedFileLogger = synchronizedFileLogger;
	}

	@Override
	public boolean canHandle(SshUserauthRequest sshUserauthRequest) {
		return sshUserauthRequest.getMethodName().equals(SSH_PASSWORD_METHOD_NAME);
	}

	@Override
	public boolean isSuccessful(byte[] rawData, final SshConnection connection) throws IOException {
		SshPasswordUserAuthRequest sshPasswordUserAuthRequest = new SshPasswordUserAuthRequest(rawData);
		System.out.println(sshPasswordUserAuthRequest.getUsername() + " " + sshPasswordUserAuthRequest.getPassword());
		synchronizedFileLogger.logToFile(sshPasswordUserAuthRequest.getUsername(), sshPasswordUserAuthRequest.getPassword(), getInetAddress(connection));
		return "asd".equals(sshPasswordUserAuthRequest.getPassword());
	}

	private InetAddress getInetAddress(final SshConnection connection) {
		return connection.getConnection().getSocket().getInetAddress();
	}

	@Override
	public String getHandledMethodName() {
		return SSH_PASSWORD_METHOD_NAME;
	}

}
