package com.helospark.FakeSsh.domain;

import static com.helospark.FakeSsh.ApplicationConstants.BYTE_SIZE;
import static com.helospark.FakeSsh.ApplicationConstants.INTEGER_LENGTH_IN_BYTES;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class DhKeySize {
	private PacketType type;
	private int minimumLength;
	private int preferredLength;
	private int maximumLength;

	public DhKeySize(byte[] data) {
		deserialize(data);
	}

	public int getMinimumLength() {
		return minimumLength;
	}

	public int getPreferredLength() {
		return preferredLength;
	}

	public int getMaximumLength() {
		return maximumLength;
	}

	public void deserialize(byte[] data) {
		this.type = PacketType.fromValue(data[0]);
		if (this.type != PacketType.SSH_MSG_KEX_DH_GEX_REQUEST) {
			throw new RuntimeException("Unexpected packet type (" + PacketType.SSH_MSG_KEX_DH_GEX_REQUEST + " != " + type);
		}
		this.minimumLength = ByteConverterUtils.byteArrayToInt(data, BYTE_SIZE + INTEGER_LENGTH_IN_BYTES * 0);
		this.preferredLength = ByteConverterUtils.byteArrayToInt(data, BYTE_SIZE + INTEGER_LENGTH_IN_BYTES * 1);
		this.maximumLength = ByteConverterUtils.byteArrayToInt(data, BYTE_SIZE + INTEGER_LENGTH_IN_BYTES * 2);
	}
}
