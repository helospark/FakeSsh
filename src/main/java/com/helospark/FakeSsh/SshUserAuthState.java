package com.helospark.FakeSsh;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshNamedList;
import com.helospark.FakeSsh.domain.SshUserAuthFailure;
import com.helospark.FakeSsh.domain.SshUserauthRequest;
import com.helospark.FakeSsh.io.SshDataExchangeService;

@Component
public class SshUserAuthState implements SshState {
	private SshDataExchangeService dataExchangeService;
	private List<AuthenticationHandler> authenticationHandler;

	@Autowired
	public SshUserAuthState(SshDataExchangeService dataExchangeService, List<AuthenticationHandler> authenticationHandler) {
		this.dataExchangeService = dataExchangeService;
		this.authenticationHandler = authenticationHandler;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		try {
			SshUserauthRequest sshUserauthRequest = new SshUserauthRequest(previousPackage);

			boolean succesfulAuthentication = false;
			boolean partialSuccess = false;
			for (AuthenticationHandler handler : authenticationHandler) {
				if (handler.canHandle(sshUserauthRequest)) {
					if (handler.isSuccessful(previousPackage, connection)) {
						succesfulAuthentication = true;
					}
					partialSuccess = true;
				}
			}
			if (!succesfulAuthentication) {
				sendUserAuthenticationFailedMessage(connection, partialSuccess);
				return StateMachineResult.REPEAT_STATE;
			} else {
				sendAuthenticationSuccesfulMessage(connection);
				return StateMachineResult.SUCCESS;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sendUserAuthenticationFailedMessage(SshConnection connection, boolean partialSuccess) throws IOException {
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
