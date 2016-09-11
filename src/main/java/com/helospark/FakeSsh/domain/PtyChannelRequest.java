package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.util.ByteConverterUtils;

public class PtyChannelRequest extends ChannelRequest {
	private String terminalType;
	private int terminalWidth;
	private int terminalHeight;
	private int terminalWidthInPixels;
	private int terminalHeightInPixels;
	private String modes;

	public PtyChannelRequest(byte[] packet) throws IOException {
		deserialize(packet);
	}

	@Override
	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		super.deserialize(byteStream);
		terminalType = new SshString(byteStream).getAsUtf8String();
		terminalWidth = ByteConverterUtils.readNextInt(byteStream);
		terminalHeight = ByteConverterUtils.readNextInt(byteStream);
		terminalWidthInPixels = ByteConverterUtils.readNextInt(byteStream);
		terminalHeightInPixels = ByteConverterUtils.readNextInt(byteStream);
		modes = new SshString(byteStream).getAsUtf8String();
	}

	public String getTerminalType() {
		return terminalType;
	}

	public int getTerminalWidth() {
		return terminalWidth;
	}

	public int getTerminalHeight() {
		return terminalHeight;
	}

	public int getTerminalWidthInPixels() {
		return terminalWidthInPixels;
	}

	public int getTerminalHeightInPixels() {
		return terminalHeightInPixels;
	}

	public String getModes() {
		return modes;
	}

	@Override
	public String toString() {
		return "PtyChannelRequest [terminalType=" + terminalType + ", terminalWidth=" + terminalWidth + ", terminalHeight=" + terminalHeight + ", terminalWidthInPixels=" + terminalWidthInPixels + ", terminalHeightInPixels="
				+ terminalHeightInPixels + ", modes=" + modes + "]";
	}

}
