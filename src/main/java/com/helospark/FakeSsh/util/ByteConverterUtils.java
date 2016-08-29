package com.helospark.FakeSsh.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.helospark.FakeSsh.ApplicationConstants;

/**
 * Utility class for working with bytes.
 * @author helospark
 */
public class ByteConverterUtils {

	/**
	 * Converts the given integer to a byte array using the network endianness.
	 * @param number to convert
	 * @return result
	 */
	public static byte[] intToByteArray(int number) {
		return ByteBuffer.allocate(ApplicationConstants.INTEGER_LENGTH_IN_BYTES).putInt(number).array();
	}

	/**
	 * Convert a byte array in network endianness to an integer.
	 * @param number to convert
	 * @return converted int
	 */
	public static int byteArrayToInt(byte[] number) {
		return ByteBuffer.wrap(number).getInt();
	}

	/**
	 * Convert a byte array in network endianness to an integer using an offset.
	 * @param number to convert
	 * @param startIndex the index in the array from where to get the bytes
	 * @return converted int
	 */
	public static int byteArrayToInt(byte[] number, int startIndex) {
		byte[] fourByteNumber = new byte[ApplicationConstants.INTEGER_LENGTH_IN_BYTES];
		System.arraycopy(number, startIndex, fourByteNumber, 0, ApplicationConstants.INTEGER_LENGTH_IN_BYTES);
		return ByteBuffer.wrap(fourByteNumber).getInt();
	}

	public static int readNextInt(ByteArrayInputStream byteStream) throws IOException {
		byte[] data = new byte[4];
		byteStream.read(data);
		return ByteConverterUtils.byteArrayToInt(data);
	}
}
