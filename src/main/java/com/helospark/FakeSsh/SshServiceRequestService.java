package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshServiceRequest;

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
			System.out.println(sshServiceRequest);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
