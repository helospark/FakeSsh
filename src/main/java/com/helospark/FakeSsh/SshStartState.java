package com.helospark.FakeSsh;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.util.LoggerSupport;

/**
 * Initial state for the SSH connection.
 * Handles exceptions and delegates to real state
 * @author helospark
 */
@Component(StateNames.START_STATE)
public class SshStartState implements SshState {
	private SshState identificationDataExchanger;
	private LoggerSupport loggerSupport;

	public SshStartState(@Qualifier(StateNames.IDENTIFICATION_EXCHANGE_STATE) SshState identificationDataExchanger, LoggerSupport loggerSupport) {
		this.identificationDataExchanger = identificationDataExchanger;
		this.loggerSupport = loggerSupport;
	}

	@Override
	public void enterState(SshConnection sshConnection) {
		try {
			identificationDataExchanger.enterState(sshConnection);
		} catch (Exception e) {
			loggerSupport.logException(e);
		} finally {
			sshConnection.getConnection().close();
			sshConnection.setConnectionClosed(true);
			String ip = sshConnection.getRemoteIpAsString();
			loggerSupport.logInfoString("Connection was closed with " + ip);
		}
	}

}
