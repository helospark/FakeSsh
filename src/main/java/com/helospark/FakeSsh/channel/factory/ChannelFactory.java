package com.helospark.FakeSsh.channel.factory;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.channel.Channel;
import com.helospark.FakeSsh.fakeos.FakeOsFactory;

@Component
public class ChannelFactory {
	private ChannelDataSenderFactory channelDataSenderFactory;
	private FakeOsFactory fakeOsFactory;
	private ChannelDataReceiverFactory channelDataReceiverFactory;

	@Autowired
	public ChannelFactory(ChannelDataSenderFactory channelDataSenderFactory, FakeOsFactory fakeOsFactory, ChannelDataReceiverFactory channelDataReceiverFactory) {
		this.channelDataSenderFactory = channelDataSenderFactory;
		this.fakeOsFactory = fakeOsFactory;
		this.channelDataReceiverFactory = channelDataReceiverFactory;
	}

	public Channel createChannel(SshConnection connection, int channelId, int maxPacketSize) {
		if (maxPacketSize > 4096) {
			maxPacketSize = 4096;
		}
		PipedOutputStream consoleOutputStream = new PipedOutputStream();
		PipedInputStream consoleInputStream = new PipedInputStream();
		// TODO: Something is smelly here
		Channel channel = new Channel();
		channel.setChannelId(channelId);
		channel.setChannelDataSender(channelDataSenderFactory.createAndRun(connection, consoleOutputStream, maxPacketSize, channelId));
		channel.setChannelDataReceiver(channelDataReceiverFactory.create(consoleInputStream));
		channel.setFakeOs(fakeOsFactory.createFakeOs(connection, channel, consoleOutputStream, consoleInputStream));
		return channel;
	}
}
