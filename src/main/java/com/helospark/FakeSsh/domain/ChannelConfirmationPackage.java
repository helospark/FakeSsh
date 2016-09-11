package com.helospark.FakeSsh.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ChannelConfirmationPackage {
	private PacketType type = PacketType.SSH_MSG_CHANNEL_OPEN_CONFIRMATION;
	private int recipientChannel;
	private int senderChannel;
	private int initialWindowSize;
	private int maximumPacketSize;

	public void setType(PacketType type) {
		this.type = type;
	}

	public void setRecipientChannel(int recipientChannel) {
		this.recipientChannel = recipientChannel;
	}

	public void setSenderChannel(int senderChannel) {
		this.senderChannel = senderChannel;
	}

	public void setInitialWindowSize(int initialWindowSize) {
		this.initialWindowSize = initialWindowSize;
	}

	public void setMaximumPacketSize(int maximumPacketSize) {
		this.maximumPacketSize = maximumPacketSize;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(ByteConverterUtils.intToByteArray(recipientChannel));
		byteStream.write(ByteConverterUtils.intToByteArray(senderChannel));
		byteStream.write(ByteConverterUtils.intToByteArray(initialWindowSize));
		byteStream.write(ByteConverterUtils.intToByteArray(maximumPacketSize));
		return byteStream.toByteArray();
	}
}
