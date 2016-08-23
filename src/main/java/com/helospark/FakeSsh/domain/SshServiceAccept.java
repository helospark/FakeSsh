package com.helospark.FakeSsh.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class SshServiceAccept {
	private PacketType type = PacketType.SSH_MSG_SERVICE_ACCEPT;
	private SshString service;

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

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(service.serialize());
		return byteStream.toByteArray();
	}
}
