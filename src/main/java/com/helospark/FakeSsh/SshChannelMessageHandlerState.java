package com.helospark.FakeSsh;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.channel.ChannelMessageHandler;

/**
 * Handle channel related messages.
 * @author helospark
 */
@Component
public class SshChannelMessageHandlerState implements SshState {
	private List<ChannelMessageHandler> channelMessageHandler;

	@Autowired
	public SshChannelMessageHandlerState(List<ChannelMessageHandler> channelMessageHandler) {
		this.channelMessageHandler = channelMessageHandler;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		try {
			PacketType type = PacketType.fromValue(previousPackage[0]);
			channelMessageHandler.stream()
					.filter(handler -> handler.canHandle(type))
					.findFirst()
					.orElseThrow(() -> new RuntimeException("No handler for channel message type " + type))
					.handleMessage(connection, previousPackage);
			return StateMachineResult.REPEAT_STATE;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
