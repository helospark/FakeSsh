package com.helospark.FakeSsh.domain;

import com.helospark.FakeSsh.PacketType;

public class SshServiceRequest {
	private PacketType type;
	private SshString message;

	public SshServiceRequest(byte[] data) {
		this.serialize(data);
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public SshString getMessage() {
		return message;
	}

	public void setMessage(SshString message) {
		this.message = message;
	}

	public void serialize(byte[] data) {
		this.type = PacketType.fromValue(data[0]);
		if (this.type != PacketType.SSH_MSG_SERVICE_REQUEST) {
			throw new RuntimeException("Unexpected packetType " + type);
		}
		this.message = new SshString(data, 1);
	}
}
