package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.PacketType;

public class SshServiceRequest {
	private PacketType type;
	private String service;

	public SshServiceRequest(byte[] data) throws IOException {
		this.deserialize(data);
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		this.type = PacketType.fromValue((byte) byteStream.read());
		if (this.type != PacketType.SSH_MSG_SERVICE_REQUEST) {
			throw new RuntimeException("Unexpected packet type (" + PacketType.SSH_MSG_SERVICE_REQUEST + " != " + type);
		}
		this.service = new String(new SshString(byteStream).serialize(), ApplicationConstants.SSH_CHARSET);
	}
}
