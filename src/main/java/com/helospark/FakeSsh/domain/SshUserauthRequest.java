package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class SshUserauthRequest {
	private PacketType type = PacketType.SSH_MSG_USERAUTH_REQUEST;
	private SshString username;
	private SshString serviceName;
	private SshString methodName;

	public SshUserauthRequest(byte[] data) throws IOException {
		deserialize(data);
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public SshString getUsername() {
		return username;
	}

	public void setUsername(SshString username) {
		this.username = username;
	}

	public SshString getServiceName() {
		return serviceName;
	}

	public void setServiceName(SshString serviceName) {
		this.serviceName = serviceName;
	}

	public SshString getMethodName() {
		return methodName;
	}

	public void setMethodName(SshString methodName) {
		this.methodName = methodName;
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		type = PacketType.fromValue((byte) byteStream.read());
		if (type != PacketType.SSH_MSG_USERAUTH_REQUEST) {
			throw new RuntimeException("Unexpected packet");
		}
		username = new SshString(byteStream);
		serviceName = new SshString(byteStream);
		methodName = new SshString(byteStream);
	}

}
