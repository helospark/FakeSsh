package com.helospark.FakeSsh.channel;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.domain.ChannelDataPacket;
import com.helospark.FakeSsh.util.LoggerSupport;

@Component
public class ChannelDataMessageHandler implements ChannelMessageHandler {
	private LoggerSupport loggerSupport;

	@Autowired
	public ChannelDataMessageHandler(LoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	@Override
	public void handleMessage(SshConnection connection, byte[] packet) throws IOException {
		ChannelDataPacket channelDataPacket = new ChannelDataPacket(packet);
		Optional<Channel> channel = connection.getChannel(channelDataPacket.getRecepientChannel());
		channel.get().sendDataToConsole(channelDataPacket.getData());
		loggerSupport.logInfoString("Data: " + channelDataPacket.getData().getAsUtf8String());
	}

	@Override
	public boolean canHandle(PacketType type) {
		return type == PacketType.SSH_MSG_CHANNEL_DATA;
	}

}
