package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class SshServiceRequest {
	private PacketType type;
	private SshString service;

	public SshServiceRequest(byte[] data) throws IOException {
		this.deserialize(data);
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public SshString getService() {
		return service;
	}

	public void setService(SshString message) {
		this.service = message;
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		this.type = PacketType.fromValue((byte) byteStream.read());
		if (this.type != PacketType.SSH_MSG_SERVICE_REQUEST) {
			throw new RuntimeException("Unexpected packetType " + type);
		}
		this.service = new SshString(byteStream);
	}
}
