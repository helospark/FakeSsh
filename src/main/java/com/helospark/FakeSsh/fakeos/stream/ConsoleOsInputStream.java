package com.helospark.FakeSsh.fakeos.stream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.fakeos.OsInputStream;

public class ConsoleOsInputStream extends OsInputStream {
	private InputStream inputStream;
	private OutputStream outputStream;

	public ConsoleOsInputStream(InputStream consoleInputStream, OutputStream outputStream) {
		this.inputStream = consoleInputStream;
		this.outputStream = outputStream;
	}

	@Override
	public String readLine() {
		return readDataUntilEnter(true);
	}

	private String readDataUntilEnter(boolean writeBack) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			while (true) {
				int readByte = inputStream.read();
				if (readByte == 13) {
					if (writeBack) {
						outputStream.write("\r\n".getBytes());
						outputStream.flush();
					}
					break;
				}
				if (writeBack) {
					outputStream.write((byte) readByte);
					outputStream.flush();
				}
				byteStream.write((byte) readByte);
			}
			return new String(byteStream.toByteArray(), ApplicationConstants.SSH_CHARSET);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
