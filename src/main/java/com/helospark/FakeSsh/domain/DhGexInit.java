package com.helospark.FakeSsh.domain;

import java.math.BigInteger;
import java.util.Arrays;

import com.helospark.FakeSsh.ByteConverterUtils;
import com.helospark.FakeSsh.PacketType;

public class DhGexInit {
	private static final int BYTE_SIZE = 1;
	private static final int INTEGER_SIZE = 4;
	private PacketType type;
	private MpInt e;

	public DhGexInit(byte[] data) {
		deserialize(data);
	}

	public void deserialize(byte[] data) {
		type = PacketType.fromValue(data[0]);
		int size = ByteConverterUtils.byteToInt(data, 1);
		int startIndex = BYTE_SIZE + INTEGER_SIZE;
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
