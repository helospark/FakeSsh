package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.ApplicationConstants;

public class SshPasswordUserAuthRequest extends SshUserauthRequest {
	private boolean someBoolean;
	private String password;

	public SshPasswordUserAuthRequest(byte[] data) throws IOException {
		super(data);
		deserialize(data);
	}

	@Override
	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		super.deserialize(byteStream);
		setSomeBoolean(byteStream.read() > 0);
		password = new String(new SshString(byteStream).getData(), ApplicationConstants.SSH_CHARSET);

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSomeBoolean() {
		return someBoolean;
	}

	public void setSomeBoolean(boolean someBoolean) {
		this.someBoolean = someBoolean;
	}

}
