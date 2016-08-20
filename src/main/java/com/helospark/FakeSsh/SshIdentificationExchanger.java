package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshString;

@Component
public class SshIdentificationExchanger {
	private static final String LOCALE_IDENTIFICATION_MESSAGE = "SSH-2.0-OpenSSH_6.6.1p1\r\n";
	private SshDataExchangeService dataExchangeService;
	private SshSupportedAlgorithmExchange next;

	@Autowired
	public SshIdentificationExchanger(SshDataExchangeService dataExchangeService, SshSupportedAlgorithmExchange sshSupportedAlgorithmExchange) {
		this.dataExchangeService = dataExchangeService;
		this.next = sshSupportedAlgorithmExchange;
	}

	public void exchangeIdentification(SshConnection connection) {
		try {
			dataExchangeService.sendString(connection, LOCALE_IDENTIFICATION_MESSAGE);
			String remoteIdentification = dataExchangeService.readStringUntil(connection, '\n');
			System.out.println(remoteIdentification);
			connection.setRemoteIdentificationMessage(new SshString(remoteIdentification.replaceAll("\r", "").replace("\n", "")));
			connection.setLocaleIdentificationMessage(new SshString(LOCALE_IDENTIFICATION_MESSAGE.replaceAll("\r", "").replaceAll("\n", "")));
			next.negotiateAlgorithm(connection);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
