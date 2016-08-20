package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.ByteConverterUtils;

public class SshString {
	private byte[] payload = new byte[0];

	public SshString() {

	}

	public SshString(ByteArrayInputStream byteArrayInputStream) throws IOException {
		deserialize(byteArrayInputStream);
	}

	public SshString(String payload) throws UnsupportedEncodingException {
		this.payload = payload.getBytes(ApplicationConstants.SSH_PROTOCOL_STRING_ENCODING_CHARSET);
	}

	public SshString(byte[] payload) {
		this.payload = payload;
	}

	public SshString(byte[] byteArray, int startIndex) {
		payload = new byte[byteArray.length - startIndex];
		System.arraycopy(byteArray, 0, payload, 0, payload.length);
	}

	public byte[] serialize() throws UnsupportedEncodingException, IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByte(payload.length));
		byteStream.write(payload);
		return byteStream.toByteArray();
	}

	public void deserialize(ByteArrayInputStream byteArrayInputStream) throws IOException {
		byte[] sizeBytes = new byte[4];
		byteArrayInputStream.read(sizeBytes);
		int size = ByteConverterUtils.byteToInt(sizeBytes);
		byte[] data = new byte[size];
		byteArrayInputStream.read(data);
		payload = data;
	}

	public byte[] getData() {
		return payload;
	}
}
