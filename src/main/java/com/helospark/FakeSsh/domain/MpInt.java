package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import com.helospark.FakeSsh.ByteConverterUtils;

public class MpInt {
	private static final int INTEGER_SIZE = 4;
	private BigInteger integer;

	public MpInt(byte[] data) {
		integer = new BigInteger(data);
	}

	public MpInt(BigInteger data) {
		this.integer = data;
	}

	public MpInt(ByteArrayInputStream byteStream) throws IOException {
		deserialize(byteStream);
	}

	public byte[] serialize() throws IOException {
		if (integer.compareTo(BigInteger.ZERO) < 0) {
			throw new RuntimeException("MpInt cannot be less than zero");
		}
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] integerBytes = integer.toByteArray();
		byteStream.write(ByteConverterUtils.intToByte(integerBytes.length));
		byteStream.write(integerBytes);
		return byteStream.toByteArray();
	}

	private void deserialize(ByteArrayInputStream byteStream) throws IOException {
		byte[] sizeBytes = new byte[4];
		byteStream.read(sizeBytes);
		int size = ByteConverterUtils.byteToInt(sizeBytes);
		byte[] data = new byte[size];
		byteStream.read(data);
		integer = new BigInteger(data);
	}

	public BigInteger getBigInteger() {
		return integer;
	}
}
