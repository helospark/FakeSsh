package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ChannelDataPacket {
	private PacketType type = PacketType.SSH_MSG_CHANNEL_DATA;
	private int recepientChannel;
	private SshString data;

	public ChannelDataPacket() {

	}

	public ChannelDataPacket(byte[] packet) throws IOException {
		deserialize(packet);
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public void setRecepientChannel(int recepientChannel) {
		this.recepientChannel = recepientChannel;
	}

	public void setData(SshString data) {
		this.data = data;
	}

	public PacketType getType() {
		return type;
	}

	public int getRecepientChannel() {
		return recepientChannel;
	}

	public SshString getData() {
		return data;
	}

	public void deserialize(byte[] packet) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(packet);
		type = PacketType.fromValue((byte) byteStream.read());
		recepientChannel = ByteConverterUtils.readNextInt(byteStream);
		data = new SshString(byteStream);
	}

	@Override
	public String toString() {
		return "ChannelDataPacket [type=" + type + ", recepientChannel=" + recepientChannel + ", data=" + data + "]";
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(ByteConverterUtils.intToByteArray(recepientChannel));
		byteStream.write(data.serialize());
		return byteStream.toByteArray();
	}

}
