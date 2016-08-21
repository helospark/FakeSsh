package com.helospark.FakeSsh;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshServiceAccept;
import com.helospark.FakeSsh.domain.SshServiceRequest;
import com.helospark.FakeSsh.domain.SshString;

/**
 * Ssh state that handles service requests.
 * @author helospark
 */
@Component(StateNames.SERVICE_REQUEST_STATE)
public class SshServiceRequestService implements SshState {
	private static final String SUPPORTED_SERVICE = "ssh-userauth";
	private SshDataExchangeService dataExchangeService;
	private UserAuthHandler userAuthHandler;
	private SshState authenticationState;

	@Autowired
	public SshServiceRequestService(UserAuthHandler userAuthHandler, SshDataExchangeService dataExchangeService) {
		this.userAuthHandler = userAuthHandler;
		this.dataExchangeService = dataExchangeService;
	}

	@Override
	public void enterState(SshConnection connection) {
		try {
			@SuppressWarnings("unused")
			SshServiceRequest sshServiceRequest = readServiceRequest(connection);
			sendSupportedUserAuth(connection);
			while (true) {
				if (userAuthHandler.wasAuthRequestSuccesful(connection)) {
					authenticationState.enterState(connection);
				}
			}
		} catch (ConnectionClosedException e) {
			return;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sendSupportedUserAuth(SshConnection connection) throws UnsupportedEncodingException, IOException {
		SshServiceAccept sshServiceAccept = new SshServiceAccept();
		sshServiceAccept.setService(new SshString(SUPPORTED_SERVICE));
		dataExchangeService.sendPacket(connection, sshServiceAccept.serialize());
	}

	private SshServiceRequest readServiceRequest(SshConnection connection) throws IOException {
		byte[] serviceRequest = dataExchangeService.readPacket(connection);
		SshServiceRequest sshServiceRequest = new SshServiceRequest(serviceRequest);
		return sshServiceRequest;
	}

}
