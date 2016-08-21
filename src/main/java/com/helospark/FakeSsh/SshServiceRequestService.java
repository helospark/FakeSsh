package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshServiceAccept;
import com.helospark.FakeSsh.domain.SshServiceRequest;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.domain.SshUserauthRequest;

@Component
public class SshServiceRequestService {
	private SshDataExchangeService dataExchangeService;

	@Autowired
	public SshServiceRequestService(SshDataExchangeService dataExchangeService) {
		this.dataExchangeService = dataExchangeService;
	}

	public void handleServiceRequest(SshConnection connection) {
		try {
			byte[] serviceRequest = dataExchangeService.readPacket(connection);
			SshServiceRequest sshServiceRequest = new SshServiceRequest(serviceRequest);

			SshServiceAccept sshServiceAccept = new SshServiceAccept();
			sshServiceAccept.setService(new SshString("ssh-userauth"));
			System.out.println(new String(sshServiceRequest.getService().getData(), "utf-8"));

			dataExchangeService.sendPacket(connection, sshServiceAccept.serialize());

			byte[] authRequest = dataExchangeService.readPacket(connection);
			SshUserauthRequest sshUserauthRequest = new SshUserauthRequest(authRequest);
			System.out.println(sshUserauthRequest);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
