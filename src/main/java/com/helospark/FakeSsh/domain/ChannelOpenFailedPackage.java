package com.helospark.FakeSsh.domain;

import static com.helospark.FakeSsh.PacketType.SSH_MSG_CHANNEL_OPEN_FAILURE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ChannelOpenFailedPackage {
	public static final int SSH_OPEN_ADMINISTRATIVELY_PROHIBITED = 1;
	public static final int SSH_OPEN_CONNECT_FAILED = 2;
	public static final int SSH_OPEN_UNKNOWN_CHANNEL_TYPE = 3;
	public static final int SSH_OPEN_RESOURCE_SHORTAGE = 4;

	private PacketType type = SSH_MSG_CHANNEL_OPEN_FAILURE;
	private int recipientChannel;
	private int reasonCode;
	private SshString description;
	private SshString language;

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public int getRecipientChannel() {
		return recipientChannel;
	}

	public void setRecipientChannel(int recipientChannel) {
		this.recipientChannel = recipientChannel;
	}

	public int getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(int reasonCode) {
		this.reasonCode = reasonCode;
	}

	public SshString getDescription() {
		return description;
	}

	public void setDescription(SshString description) {
		this.description = description;
	}

	public SshString getLanguage() {
		return language;
	}

	public void setLanguage(SshString language) {
		this.language = language;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(ByteConverterUtils.intToByteArray(recipientChannel));
		byteStream.write(ByteConverterUtils.intToByteArray(reasonCode));
		byteStream.write(description.serialize());
		byteStream.write(language.serialize());
		return byteStream.toByteArray();
	}
}
