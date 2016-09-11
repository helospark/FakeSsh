package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ChannelRequest {
	private PacketType type = PacketType.SSH_MSG_CHANNEL_REQUEST;
	private int recipientChannel;
	private String requestType;
	private boolean wantReply;

	public ChannelRequest() {

	}

	public ChannelRequest(byte[] packet) throws IOException {
		deserialize(packet);
	}

	public PacketType getType() {
		return type;
	}

	public int getRecipientChannel() {
		return recipientChannel;
	}

	public String getRequestType() {
		return requestType;
	}

	public boolean isWantReply() {
		return wantReply;
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		deserialize(byteStream);
	}

	protected void deserialize(ByteArrayInputStream byteStream) throws IOException {
		PacketType type = PacketType.fromValue((byte) byteStream.read());
		if (type != PacketType.SSH_MSG_CHANNEL_REQUEST) {
			throw new RuntimeException("Unexpected packet");
		}
		recipientChannel = ByteConverterUtils.readNextInt(byteStream);
		requestType = new SshString(byteStream).getAsUtf8String();
		wantReply = byteStream.read() > 0;
	}

	@Override
	public String toString() {
		return "ChannelRequest [type=" + type + ", recipientChannel=" + recipientChannel + ", requestType=" + requestType + ", wantReply=" + wantReply + "]";
	}

}
