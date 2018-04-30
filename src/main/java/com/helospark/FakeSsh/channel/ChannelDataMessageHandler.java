package com.helospark.FakeSsh.channel;

import java.io.IOException;

import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.domain.ChannelDataPacket;

@Component
public class ChannelDataMessageHandler implements ChannelMessageHandler {

	@Override
	public void handleMessage(SshConnection connection, byte[] packet) throws IOException {
		ChannelDataPacket handler = new ChannelDataPacket(packet);
		System.out.println(handler.toString());
	}

	@Override
	public boolean canHandle(PacketType type) {
		return type == PacketType.SSH_MSG_CHANNEL_DATA;
	}

}
