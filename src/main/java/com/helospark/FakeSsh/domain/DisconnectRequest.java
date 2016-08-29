package com.helospark.FakeSsh.domain;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class DisconnectRequest {
	private PacketType type;
	private int reason;

	public DisconnectRequest(byte[] data) {
		deserialize(data);
	}

	public void deserialize(byte[] data) {
		type = PacketType.fromValue(data[0]);
		if (type != PacketType.SSH_MSG_DISCONNECT) {
			throw new RuntimeException("Unexpected packet type");
		}
		reason = ByteConverterUtils.byteArrayToInt(data, 1);
	}

	public PacketType getType() {
		return type;
	}

	public int getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return "DisconnectRequest [type=" + type + ", reason=" + reason + "]";
	}

}
