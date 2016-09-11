package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.ConnectionClosedException;
import com.helospark.FakeSsh.SshConnection;

/**
 * Reads plain strings from the stream.
 * @author helospark
 */
@Component
public class StringReaderService {

	/**
	 * Reads a string until the delimiter from the given connection.
	 * @param connection to read from
	 * @param delimiter to read until reached
	 * @return read string excluding the delimiter
	 * @throws IOException on IO error
	 * @throws ConnectionClosedException if the connection was unexpectedly closed
	 */
	public String readStringUntilDelimiter(SshConnection connection, char delimiter) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		InputStream inputStream = connection.getConnection().getInputStream();
		int readByte = 0;
		while (readByte != -1) {
			readByte = inputStream.read();
			if (readByte == -1) {
				throw new ConnectionClosedException();
			}
			if ((byte) readByte == delimiter) {
				break;
			} else {
				byteStream.write((byte) readByte);
			}
		}
		return new String(byteStream.toByteArray(), ApplicationConstants.SSH_CHARSET);
	}
}
