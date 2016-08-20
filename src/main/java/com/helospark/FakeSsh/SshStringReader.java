package com.helospark.FakeSsh;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SshStringReader {

	public static String readString(ByteArrayInputStream byteArrayInputStream) throws IOException {
		byte[] sizeInBytes = new byte[4];
		byteArrayInputStream.read(sizeInBytes);
		int size = ByteConverterUtils.byteToInt(sizeInBytes);
		byte[] stringAsBytes = new byte[size];
		byteArrayInputStream.read(stringAsBytes);
		return new String(stringAsBytes, 0, stringAsBytes.length, ApplicationConstants.SSH_PROTOCOL_STRING_ENCODING_CHARSET);
	}
}
