package com.helospark.FakeSsh.domain;

import java.math.BigInteger;
import java.util.Arrays;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.util.ByteConverterUtils;

public class DhGexInit {
	private PacketType type;
	private MpInt e;

	public DhGexInit(byte[] data) {
		deserialize(data);
	}

	public void deserialize(byte[] data) {
		type = PacketType.fromValue(data[0]);
		if (type != PacketType.SSH_MSG_KEX_DH_GEX_INIT) {
			throw new RuntimeException("Unexpected packet type (" + PacketType.SSH_MSG_KEX_DH_GEX_INIT + " != " + type);
		}
		int size = ByteConverterUtils.byteArrayToInt(data, 1);
		int startIndex = ApplicationConstants.BYTE_SIZE + ApplicationConstants.INTEGER_LENGTH_IN_BYTES;
		byte[] result = Arrays.copyOfRange(data, startIndex, startIndex + size);
		e = new MpInt(new BigInteger(result));
	}

	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public MpInt getE() {
		return e;
	}

	public void setE(MpInt e) {
		this.e = e;
	}

}
