package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.ByteConverterUtils;

/**
 * Domain object to represent a String in the SSH protocol.
 * Based on the RFC string is serialized with 4 starting bytes as length in network format
 * followed by that number of raw bytes. The byte-sequence contains no special character
 * @author helospark
 */
public class SshString {
	private byte[] payload = new byte[0];

	/**
	 * Builds empty string.
	 */
	public SshString() {

	}

	/**
	 * Build string from InputStream and advances it.
	 * @param byteArrayInputStream to read from
	 * @throws IOException on reading errors
	 */
	public SshString(ByteArrayInputStream byteArrayInputStream) throws IOException {
		deserialize(byteArrayInputStream);
	}

	/**
	 * Builds string from Java's String
	 * @param payload to build from
	 * @throws UnsupportedEncodingException if UTF-8 is not supported
	 */
	public SshString(String payload) throws UnsupportedEncodingException {
		this.payload = payload.getBytes(ApplicationConstants.SSH_CHARSET);
	}

	/**
	 * Builds string from raw byte data.
	 * @param payload to build from
	 */
	public SshString(byte[] payload) {
		this.payload = payload;
	}

	/**
	 * Serializes the string to a byte array.
	 * Based on the RFC string is serialized with 4 starting bytes as length in network format
	 * followed by that number of raw bytes. The byte-sequence contains no special character
	 * @return serialized string
	 */
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByteArray(payload.length));
		byteStream.write(payload);
		return byteStream.toByteArray();
	}

	/**
	 * Deserializes string from stream.
	 * Based on the RFC string is serialized with 4 starting bytes as length in network format
	 * followed by that number of raw bytes. The byte-sequence contains no special character
	 * @param byteArrayInputStream to read from
	 */
	public void deserialize(ByteArrayInputStream byteArrayInputStream) throws IOException {
		byte[] sizeBytes = new byte[4];
		byteArrayInputStream.read(sizeBytes);
		int size = ByteConverterUtils.byteArrayToInt(sizeBytes);
		if (size > ApplicationConstants.MAX_STRING_SIZE) {
			throw new RuntimeException("String too long");
		}
		byte[] data = new byte[size];
		byteArrayInputStream.read(data);
		payload = data;
	}

	/**
	 * Get raw data.
	 * @return raw data
	 */
	public byte[] getData() {
		return payload;
	}

	/**
	 * Get data is UTF-8 string.
	 * @return
	 */
	public String getAsUtf8String() {
		try {
			return new String(payload, ApplicationConstants.SSH_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported", e);
		}
	}

	@Override
	public String toString() {
		return "SshString [payload=" + getAsUtf8String() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(payload);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SshString other = (SshString) obj;
		if (!Arrays.equals(payload, other.payload))
			return false;
		return true;
	}

}
