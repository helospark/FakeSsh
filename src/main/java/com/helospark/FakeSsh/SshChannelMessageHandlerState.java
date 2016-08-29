package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.ChannelConfirmationPackage;
import com.helospark.FakeSsh.domain.ChannelOpenPacket;
import com.helospark.FakeSsh.io.SshDataExchangeService;

@Component
public class SshChannelMessageHandlerState implements SshState {
	private SshDataExchangeService dataExchangeService;

	@Autowired
	public SshChannelMessageHandlerState(SshDataExchangeService dataExchangeService) {
		this.dataExchangeService = dataExchangeService;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		try {
			ChannelOpenPacket channelOpenPacket = new ChannelOpenPacket(previousPackage);
			System.out.println(channelOpenPacket.toString());
			sendConfirmation(connection, channelOpenPacket);
			byte[] newData = dataExchangeService.readPacket(connection);
			System.out.println(PacketType.fromValue(newData[0]));
			return StateMachineResult.REPEAT_STATE;
		} catch (Exception e) {
			throw new RuntimeException(e);
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

}
