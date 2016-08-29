package com.helospark.FakeSsh;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshServiceAccept;
import com.helospark.FakeSsh.domain.SshServiceRequest;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.io.SshDataExchangeService;

/**
 * Ssh state that handles service requests.
 * @author helospark
 */
@Component(StateNames.SERVICE_REQUEST_STATE)
public class SshServiceRequestService implements SshState {
	private static final String SUPPORTED_SERVICE = "ssh-userauth";
	private SshDataExchangeService dataExchangeService;

	@Autowired
	public SshServiceRequestService(SshDataExchangeService dataExchangeService) {
		this.dataExchangeService = dataExchangeService;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPacket) {
		try {
			@SuppressWarnings("unused")
			SshServiceRequest sshServiceRequest = readServiceRequest(previousPacket);
			sendSupportedUserAuth(connection);
			return StateMachineResult.SUCCESS;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sendSupportedUserAuth(SshConnection connection) throws UnsupportedEncodingException, IOException {
		SshServiceAccept sshServiceAccept = new SshServiceAccept();
		sshServiceAccept.setService(new SshString(SUPPORTED_SERVICE));
		dataExchangeService.sendPacket(connection, sshServiceAccept.serialize());
	}

	private SshServiceRequest readServiceRequest(byte[] previousPacket) throws IOException {
		SshServiceRequest sshServiceRequest = new SshServiceRequest(previousPacket);
		return sshServiceRequest;
	}

}
