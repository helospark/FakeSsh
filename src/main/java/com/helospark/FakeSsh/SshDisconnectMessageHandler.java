package com.helospark.FakeSsh;

import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.domain.DisconnectRequest;
import com.helospark.FakeSsh.util.LoggerSupport;

@Component
public class SshDisconnectMessageHandler implements SshCommonState {
	private LoggerSupport loggerSupport;

	public SshDisconnectMessageHandler(LoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		DisconnectRequest disconnectRequest = new DisconnectRequest(previousPackage);
		loggerSupport.logDebugString(disconnectRequest.toString());
		return StateMachineResult.CLOSED;
	}

	@Override
	public boolean canHandle(byte[] previousPackage) {
		PacketType type = PacketType.fromValue(previousPackage[0]);
		return type == PacketType.SSH_MSG_DISCONNECT;
	}

}
