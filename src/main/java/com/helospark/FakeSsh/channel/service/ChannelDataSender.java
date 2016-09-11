package com.helospark.FakeSsh.channel.service;

import java.io.PipedInputStream;

import org.bouncycastle.util.Arrays;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.domain.ChannelDataPacket;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.io.SshDataExchangeService;

public class ChannelDataSender {
	private PipedInputStream pipedInputStream;
	private SshDataExchangeService dataExchangeService;
	private SshConnection connection;
	private int maxPacketSize;
	private int recepientChannel;

	public ChannelDataSender(PipedInputStream pipedInputStream, SshDataExchangeService dataExchangeService, SshConnection connection, int maxPacketSize, int recepientChannel) {
		this.pipedInputStream = pipedInputStream;
		this.dataExchangeService = dataExchangeService;
		this.connection = connection;
		this.maxPacketSize = maxPacketSize;
		this.recepientChannel = recepientChannel;
	}

	public void startDataSender() {
		new Thread(() -> sendData()).start();
	}

	private void sendData() {
		try {
			byte[] buffer = new byte[maxPacketSize];
			while (true) {
				int readBytes = pipedInputStream.read(buffer);
				ChannelDataPacket packet = new ChannelDataPacket();
				packet.setData(new SshString(Arrays.copyOfRange(buffer, 0, readBytes)));
				packet.setRecepientChannel(recepientChannel);
				packet.setType(PacketType.SSH_MSG_CHANNEL_DATA);
				dataExchangeService.sendPacket(connection, packet.serialize());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
