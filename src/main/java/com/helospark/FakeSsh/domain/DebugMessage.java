package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class DebugMessage {
	private PacketType type = PacketType.SSH_MSG_DEBUG;
	private boolean alwaysDisplay;
	private SshString message;
	private SshString languageTag;

	public DebugMessage(byte[] data) throws IOException {
		deserialize(data);
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		type = PacketType.fromValue((byte) byteStream.read());
		if (type != PacketType.SSH_MSG_DEBUG) {
			throw new RuntimeException("Unexpected packet for debug");
		}
		alwaysDisplay = byteStream.read() > 0;
		message = new SshString(byteStream);
		languageTag = new SshString(byteStream);
	}

	public PacketType getType() {
		return type;
	}

	public boolean isAlwaysDisplay() {
		return alwaysDisplay;
	}

	public SshString getMessage() {
		return message;
	}

	public SshString getLanguageTag() {
		return languageTag;
	}

	@Override
	public String toString() {
		return "DebugMessage [type=" + type + ", alwaysDisplay=" + alwaysDisplay + ", message=" + message + ", languageTag=" + languageTag + "]";
	}

}
