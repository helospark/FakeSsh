package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class EnvChannelRequest extends ChannelRequest {
	private String variableName;
	private String variableValue;

	public EnvChannelRequest(byte[] packet) throws IOException {
		deserialize(packet);
	}

	@Override
	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		super.deserialize(byteStream);
		variableName = new SshString(byteStream).getAsUtf8String();
		variableValue = new SshString(byteStream).getAsUtf8String();
	}

	public String getVariableName() {
		return variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	@Override
	public String toString() {
		return "EnvChannelRequest [variableName=" + variableName + ", variableValue=" + variableValue + "]";
	}

}
