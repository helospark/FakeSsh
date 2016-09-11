package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ConnectionClosedException;

/**
 * Reads given number of bytes from the inputstream.
 * @author helospark
 */
@Component
public class InputStreamRawDataReader {

	/**
	 * Reads given number of bytes from the inputstream.
	 * @param inputStream to read data from
	 * @param messageSizeToRead number if bytes to read
	 * @return read bytes, empty array for 0 messageSizeToRead
	 * @throws IOException on IO errors
	 * @throws ConnectionClosedException when connection was unexpectely closed
	 */
	public byte[] readDataWithSize(InputStream inputStream, int messageSizeToRead) throws IOException {
		if (messageSizeToRead == 0) {
			return new byte[0];
		}
		int numberOfReadBytes = 0;
		byte[] readData = new byte[messageSizeToRead];
		do {
			int numberOfBytesLeft = messageSizeToRead - numberOfReadBytes;
			int currentReadBytes = inputStream.read(readData, numberOfReadBytes, numberOfBytesLeft);
			if (currentReadBytes == -1) {
				throw new ConnectionClosedException();
			}
			numberOfReadBytes += currentReadBytes;
		} while (numberOfReadBytes < messageSizeToRead);
		return readData;
	}
}
