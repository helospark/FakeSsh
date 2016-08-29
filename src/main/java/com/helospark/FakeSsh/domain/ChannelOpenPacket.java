package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ChannelOpenPacket {
	private PacketType type = PacketType.SSH_MSG_CHANNEL_OPEN;
	private SshString channelType;
	private int senderChannel;
	private int windowSize;
	private int maxPacketSize;

	public ChannelOpenPacket(byte[] data) throws IOException {
		deserialize(data);
	}

	public PacketType getType() {
		return type;
	}

	public SshString getChannelType() {
		return channelType;
	}

	public int getSenderChannel() {
		return senderChannel;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public int getMaxPacketSize() {
		return maxPacketSize;
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		type = PacketType.fromValue((byte) byteStream.read());
		channelType = new SshString(byteStream);
		senderChannel = ByteConverterUtils.readNextInt(byteStream);
		maxPacketSize = ByteConverterUtils.readNextInt(byteStream);
		windowSize = ByteConverterUtils.readNextInt(byteStream);
	}

	@Override
	public String toString() {
		return "ChannelOpenPacket [type=" + type + ", channelType=" + channelType + ", senderChannel=" + senderChannel + ", windowSize=" + windowSize + ", maxPacketSize=" + maxPacketSize + "]";
	}

}
