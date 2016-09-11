package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.hmac.SshMac;

/**
 * Reads the MAC from the input stream.
 * Precondition of this class is that the inputStream should stand right before the MAC
 * aka. the packet has to have been already read.
 * @author helospark
 */
@Component
public class MacReader {
	private InputStreamRawDataReader inputStreamRawDataReader;

	@Autowired
	public MacReader(InputStreamRawDataReader inputStreamRawDataReader) {
		this.inputStreamRawDataReader = inputStreamRawDataReader;
	}

	public byte[] readMac(InputStream inputStream, Optional<SshMac> optionalMac) throws IOException {
		int macLength = optionalMac.map(mac -> mac.getMacLength()).orElse(0);
		return inputStreamRawDataReader.readDataWithSize(inputStream, macLength);
	}
}
