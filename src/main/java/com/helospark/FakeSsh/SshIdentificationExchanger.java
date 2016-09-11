package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.io.SshDataExchangeService;
import com.helospark.FakeSsh.util.LoggerSupport;

/**
 * Ssh connection state the exchanges the identification strings.
 * @author helospark
 */
@Component
public class SshIdentificationExchanger {
	private static final String LOCALE_IDENTIFICATION_MESSAGE = "SSH-2.0-OpenSSH_6.6.1p1\r\n";
	private SshDataExchangeService dataExchangeService;
	private LoggerSupport loggerSupport;

	@Autowired
	public SshIdentificationExchanger(SshDataExchangeService dataExchangeService, LoggerSupport loggerSupport) {
		this.dataExchangeService = dataExchangeService;
		this.loggerSupport = loggerSupport;
	}

	public void exchangeIdentification(SshConnection connection) {
		try {
			String remoteIdentification = dataExchangeService.readStringUntilDelimiter(connection, '\n');
			dataExchangeService.sendString(connection, LOCALE_IDENTIFICATION_MESSAGE);

			connection.setRemoteIdentificationMessage(new SshString(clearNewlineCharacters(remoteIdentification)));
			connection.setLocaleIdentificationMessage(new SshString(clearNewlineCharacters(LOCALE_IDENTIFICATION_MESSAGE)));

			logIdentification(connection);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String clearNewlineCharacters(String remoteIdentification) {
		return remoteIdentification.replaceAll("\r", "").replace("\n", "");
	}

	private void logIdentification(SshConnection connection) {
		loggerSupport.logDebugString("Remote identification: " + connection.getRemoteIdentificationMessage());
		loggerSupport.logDebugString("Locale identification: " + connection.getLocaleIdentificationMessage());
	}

}
