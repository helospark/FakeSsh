package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshString;

/**
 * Ssh connection state the exchanges the identification strings.
 * @author helospark
 */
@Component(StateNames.IDENTIFICATION_EXCHANGE_STATE)
public class SshIdentificationExchanger implements SshState {
	private static final String LOCALE_IDENTIFICATION_MESSAGE = "SSH-2.0-OpenSSH_6.6.1p1\r\n";
	private SshDataExchangeService dataExchangeService;
	private LoggerSupport loggerSupport;
	private SshState next;

	@Autowired
	public SshIdentificationExchanger(SshDataExchangeService dataExchangeService, LoggerSupport loggerSupport, @Qualifier(StateNames.SUPPORTED_ALGORITHM_EXCHANGE_STATE) SshState sshSupportedAlgorithmExchange) {
		this.dataExchangeService = dataExchangeService;
		this.next = sshSupportedAlgorithmExchange;
		this.loggerSupport = loggerSupport;
	}

	@Override
	public void enterState(SshConnection connection) {
		try {
			dataExchangeService.sendString(connection, LOCALE_IDENTIFICATION_MESSAGE);
			String remoteIdentification = dataExchangeService.readStringUntilDelimiter(connection, '\n');

			connection.setRemoteIdentificationMessage(new SshString(clearNewlineCharacters(remoteIdentification)));
			connection.setLocaleIdentificationMessage(new SshString(clearNewlineCharacters(LOCALE_IDENTIFICATION_MESSAGE)));

			logIdentification(connection);

			next.enterState(connection);
		} catch (ConnectionClosedException e) {
			return;
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
