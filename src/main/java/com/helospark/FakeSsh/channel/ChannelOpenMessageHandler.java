package com.helospark.FakeSsh.channel;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.channel.factory.ChannelFactory;
import com.helospark.FakeSsh.domain.ChannelConfirmationPackage;
import com.helospark.FakeSsh.domain.ChannelOpenFailedPackage;
import com.helospark.FakeSsh.domain.ChannelOpenPacket;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.io.SshDataExchangeService;

@Component
public class ChannelOpenMessageHandler implements ChannelMessageHandler {
	private SshDataExchangeService dataExchangeService;
	private ChannelFactory channelFactory;

	@Autowired
	public ChannelOpenMessageHandler(SshDataExchangeService dataExchangeService, ChannelFactory channelFactory) {
		this.dataExchangeService = dataExchangeService;
		this.channelFactory = channelFactory;
	}

	@Override
	public void handleMessage(SshConnection connection, byte[] packet) throws IOException {
		ChannelOpenPacket channelOpenPacket = new ChannelOpenPacket(packet);
		if (connection.getChannels().size() > 1) {
			sendChannelOpenDenied(connection, channelOpenPacket);
		} else {
			sendConfirmation(connection, channelOpenPacket);
			Channel channel = channelFactory.createChannel(connection, channelOpenPacket.getSenderChannel(), channelOpenPacket.getMaxPacketSize());
			connection.addChannel(channel);
		}
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

	private void sendChannelOpenDenied(SshConnection connection, ChannelOpenPacket channelOpenPacket) throws IOException {
		ChannelOpenFailedPackage channelOpenFailedPackage = new ChannelOpenFailedPackage();
		channelOpenFailedPackage.setDescription(new SshString("Open failed"));
		channelOpenFailedPackage.setReasonCode(ChannelOpenFailedPackage.SSH_OPEN_CONNECT_FAILED);
		channelOpenFailedPackage.setLanguage(new SshString(""));
		channelOpenFailedPackage.setRecipientChannel(channelOpenPacket.getSenderChannel());
		dataExchangeService.sendPacket(connection, channelOpenFailedPackage.serialize());
	}

	@Override
	public boolean canHandle(PacketType type) {
		return type == PacketType.SSH_MSG_CHANNEL_OPEN;
	}

}
