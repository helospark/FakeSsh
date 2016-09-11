package com.helospark.FakeSsh;

import org.springframework.stereotype.Component;

/**
 * Handle the SSH_MSG_IGNORE messages.
 * @author helospark
 */
@Component
public class SshIgnoreMessageHandler implements SshCommonState {

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		return StateMachineResult.SUCCESS;
	}

	@Override
	public boolean canHandle(byte[] previousPackage) {
		PacketType type = PacketType.fromValue(previousPackage[0]);
		return type == PacketType.SSH_MSG_IGNORE;
	}

}
