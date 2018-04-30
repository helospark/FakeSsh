package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.SshConnection;

/**
 * Reads plain strings from the stream.
 * @author helospark
 */
@Component
public class StringReaderService {

	public String readStringUntilDelimiter(SshConnection connection, char delimiter) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		InputStream inputStream = connection.getConnection().getInputStream();
		int readByte = 0;
		while (readByte != -1) {
			readByte = inputStream.read();
			if (readByte == -1 || (byte) readByte == delimiter) {
				break;
			} else {
				byteStream.write((byte) readByte);
			}
		}
		return new String(byteStream.toByteArray(), ApplicationConstants.SSH_CHARSET);
	}
}
