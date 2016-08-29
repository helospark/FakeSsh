package com.helospark.FakeSsh.channel;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.domain.ChannelConfirmationPackage;
import com.helospark.FakeSsh.domain.ChannelOpenPacket;
import com.helospark.FakeSsh.io.SshDataExchangeService;

@Component
public class ChannelOpenMessageHandler implements ChannelMessageHandler {
	private SshDataExchangeService dataExchangeService;

	@Autowired
	public ChannelOpenMessageHandler(SshDataExchangeService dataExchangeService) {
		this.dataExchangeService = dataExchangeService;
	}

	@Override
	public void handleMessage(SshConnection connection, byte[] packet) throws IOException {
		ChannelOpenPacket channelOpenPacket = new ChannelOpenPacket(packet);
		System.out.println(channelOpenPacket.toString());
		sendConfirmation(connection, channelOpenPacket);
	}

	private void sendConfirmation(SshConnection connection, ChannelOpenPacket channelOpenPacket) throws IOException {
		ChannelConfirmationPackage channelConfirmationPackage = new ChannelConfirmationPackage();
		channelConfirmationPackage.setInitialWindowSize(channelOpenPacket.getWindowSize());
		channelConfirmationPackage.setMaximumPacketSize(channelOpenPacket.getMaxPacketSize());
		channelConfirmationPackage.setRecipientChannel(channelOpenPacket.getSenderChannel());
		channelConfirmationPackage.setSenderChannel(0);
		channelConfirmationPackage.setInitialWindowSize(channelOpenPacket.getWindowSize());
		dataExchangeService.sendPacket(connection, channelConfirmationPackage.serialize());
	}

	@Override
	public boolean canHandle(PacketType type) {
		return type == PacketType.SSH_MSG_CHANNEL_OPEN;
	}

}
