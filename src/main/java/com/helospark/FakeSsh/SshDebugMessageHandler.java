package com.helospark.FakeSsh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.DebugMessage;
import com.helospark.FakeSsh.util.LoggerSupport;

/**
 * Handle the SSH_MSG_DEBUG messages.
 * @author helospark
 */
@Component
public class SshDebugMessageHandler implements SshCommonState {
	private LoggerSupport loggerSupport;

	@Autowired
	public SshDebugMessageHandler(LoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		try {
			DebugMessage debugMessage = new DebugMessage(previousPackage);
			loggerSupport.logDebugString(debugMessage.toString());
			return StateMachineResult.CLOSED;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canHandle(byte[] previousPackage) {
		PacketType type = PacketType.fromValue(previousPackage[0]);
		return type == PacketType.SSH_MSG_DEBUG;
	}

}
