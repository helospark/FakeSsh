package com.helospark.FakeSsh.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ChannelRequestResult {
	private PacketType type;
	private int recepientChannel;

	public void setType(PacketType type) {
		this.type = type;
	}

	public void setRecepientChannel(int recepientChannel) {
		this.recepientChannel = recepientChannel;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(ByteConverterUtils.intToByteArray(recepientChannel));
		return byteStream.toByteArray();
	}
}
