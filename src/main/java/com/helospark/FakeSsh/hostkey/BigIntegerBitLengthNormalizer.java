package com.helospark.FakeSsh.hostkey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.lightdi.annotation.Component;

/** 
 * Normalizes the length of a BigInteger to the given amount of bytes.
 * BigInteger's toByteArray automatically appends a 0 byte when
 * with 160 bits representation it would be a negative number
 * @author helospark
 */
@Component
public class BigIntegerBitLengthNormalizer {

	public byte[] normalizeToLength(byte[] bytes, int numberOfBytes) throws IOException {
		byte[] result = bytes;
		if (bytes.length < numberOfBytes) {
			result = padWithZeroBytes(bytes, numberOfBytes);
		} else if (bytes.length > numberOfBytes) {
			result = removeFirstBytes(bytes, numberOfBytes);
		}
		return result;
	}

	private byte[] padWithZeroBytes(byte[] bytes, int minimumBytes) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		for (int i = bytes.length; i < minimumBytes; ++i) {
			byteStream.write(0x00);
		}
		byteStream.write(bytes);
		return byteStream.toByteArray();
	}

	private byte[] removeFirstBytes(byte[] bytes, int minimumBytes) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		for (int i = bytes.length - minimumBytes; i < bytes.length; ++i) {
			byteStream.write(bytes[i]);
		}
		return byteStream.toByteArray();
	}
}
