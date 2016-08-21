package com.helospark.FakeSsh;

import java.io.IOException;

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

	@Override
	public boolean canHandle(SshUserauthRequest sshUserauthRequest) {
		return sshUserauthRequest.getMethodName().equals(SSH_PASSWORD_METHOD_NAME);
	}

	@Override
	public boolean isSuccessful(byte[] rawData) throws IOException {
		SshPasswordUserAuthRequest sshPasswordUserAuthRequest = new SshPasswordUserAuthRequest(rawData);
		System.out.println(sshPasswordUserAuthRequest.getUsername() + " " + sshPasswordUserAuthRequest.getPassword());
		return false;
	}

	@Override
	public String getHandledMethodName() {
		return SSH_PASSWORD_METHOD_NAME;
	}

}
