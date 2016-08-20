package com.helospark.FakeSsh;

import java.nio.ByteBuffer;

public class ByteConverterUtils {
	private static final int INTEGER_BYTE_LENGTH_IN_BYTES = 4;

	public static byte[] intToByte(int number) {
		return ByteBuffer.allocate(INTEGER_BYTE_LENGTH_IN_BYTES).putInt(number).array();
	}

	public static int byteToInt(byte[] number) {
		return ByteBuffer.wrap(number).getInt();
	}

	public static int byteToInt(byte[] number, int startIndex) {
		byte[] fourByteNumber = new byte[INTEGER_BYTE_LENGTH_IN_BYTES];
		System.arraycopy(number, startIndex, fourByteNumber, 0, INTEGER_BYTE_LENGTH_IN_BYTES);
		return ByteBuffer.wrap(fourByteNumber).getInt();
	}
}
