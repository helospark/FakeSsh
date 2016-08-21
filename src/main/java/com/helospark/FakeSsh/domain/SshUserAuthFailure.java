package com.helospark.FakeSsh.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class SshUserAuthFailure {
	private PacketType type = PacketType.SSH_MSG_USERAUTH_FAILURE;
	private SshNamedList continuableAuthentication;
	private byte partialSuccess;

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public SshNamedList getContinuableAuthentication() {
		return continuableAuthentication;
	}

	public void setContinuableAuthentication(SshNamedList continuableAuthentication) {
		this.continuableAuthentication = continuableAuthentication;
	}

	public byte getPartialSuccess() {
		return partialSuccess;
	}

	public void setPartialSuccess(boolean partialSuccess) {
		this.partialSuccess = (byte) (partialSuccess ? 1 : 0);
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(continuableAuthentication.serialize());
		byteStream.write(partialSuccess);
		return byteStream.toByteArray();
	}
}
