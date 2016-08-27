package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.PacketType;

public class SshUserauthRequest {
	private PacketType type = PacketType.SSH_MSG_USERAUTH_REQUEST;
	private String username;
	private String serviceName;
	private String methodName;

	public SshUserauthRequest(byte[] data) throws IOException {
		deserialize(data);
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		deserialize(byteStream);
	}

	protected void deserialize(ByteArrayInputStream byteStream) throws IOException {
		type = PacketType.fromValue((byte) byteStream.read());
		if (type != PacketType.SSH_MSG_USERAUTH_REQUEST) {
			throw new RuntimeException("Unexpected packet type (" + PacketType.SSH_MSG_USERAUTH_REQUEST + " != " + type);
		}
		username = new String(new SshString(byteStream).getData(), ApplicationConstants.SSH_CHARSET);
		serviceName = new String(new SshString(byteStream).getData(), ApplicationConstants.SSH_CHARSET);
		methodName = new String(new SshString(byteStream).getData(), ApplicationConstants.SSH_CHARSET);
	}

}
