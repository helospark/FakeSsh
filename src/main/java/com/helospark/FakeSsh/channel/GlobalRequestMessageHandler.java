package com.helospark.FakeSsh.channel;

import java.io.IOException;

import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;

@Component
public class GlobalRequestMessageHandler implements ChannelMessageHandler {

	@Override
	public void handleMessage(SshConnection connection, byte[] packet) throws IOException {

	}

	@Override
	public boolean canHandle(PacketType type) {
		return type == PacketType.SSH_MSG_GLOBAL_REQUEST;
	}

}
