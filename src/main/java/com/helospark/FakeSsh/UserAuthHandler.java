package com.helospark.FakeSsh;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshNamedList;
import com.helospark.FakeSsh.domain.SshUserAuthFailure;
import com.helospark.FakeSsh.domain.SshUserauthRequest;

/**
 * Handles the client's authentication request.
 * @author helospark
 */
@Component
public class UserAuthHandler {
	private SshDataExchangeService dataExchangeService;
	private List<AuthenticationHandler> authenticationHandler;

	/**
	 * Constructor.
	 * @param dataExchangeService to read the authentication request
	 * @param authenticationHandler list of handlers to delegate call to
	 */
	@Autowired
	public UserAuthHandler(SshDataExchangeService dataExchangeService, List<AuthenticationHandler> authenticationHandler) {
		this.dataExchangeService = dataExchangeService;
		this.authenticationHandler = authenticationHandler;
	}

	// TODO! this class has too many responsibilities, should be separated
	/**
	 * Reads the authentication message, processes it and sends response.
	 * @param connection to the client
	 * @return true if we successfully authenticated the user
	 */
	public boolean wasAuthRequestSuccesful(SshConnection connection) throws Exception {
		byte[] authRequest = dataExchangeService.readPacket(connection);
		SshUserauthRequest sshUserauthRequest = new SshUserauthRequest(authRequest);

		boolean succesfulAuthentication = false;
		boolean partialSuccess = false;
		for (AuthenticationHandler handler : authenticationHandler) {
			if (handler.canHandle(sshUserauthRequest)) {
				if (handler.isSuccessful(authRequest, connection)) {
					sendAuthenticationSuccesfulMessage(connection);
					succesfulAuthentication = true;
				}
				partialSuccess = true;
			}
		}
		if (!succesfulAuthentication) {
			sendUserAuthFailedMessage(connection, partialSuccess);
		}
		return succesfulAuthentication;
	}

	private void sendUserAuthFailedMessage(SshConnection connection, boolean partialSuccess) throws IOException {
		SshUserAuthFailure sshUserAuthFailure = new SshUserAuthFailure();
		List<String> continuableAuthentications = authenticationHandler.stream()
				.map(handler -> handler.getHandledMethodName())
				.collect(Collectors.toList());
		sshUserAuthFailure.setPartialSuccess(partialSuccess);
		sshUserAuthFailure.setContinuableAuthentication(new SshNamedList(continuableAuthentications));
		sshUserAuthFailure.setType(PacketType.SSH_MSG_USERAUTH_FAILURE);
		dataExchangeService.sendPacket(connection, sshUserAuthFailure.serialize());
	}

	private void sendAuthenticationSuccesfulMessage(SshConnection connection) throws IOException {
		dataExchangeService.sendPacket(connection, PacketType.SSH_MSG_USERAUTH_SUCCESS);
	}
}
