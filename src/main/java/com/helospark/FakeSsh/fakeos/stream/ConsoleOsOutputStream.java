package com.helospark.FakeSsh.fakeos.stream;

import java.io.OutputStream;

import com.helospark.FakeSsh.fakeos.OsOutputStream;

public class ConsoleOsOutputStream extends OsOutputStream {
	private OutputStream outputStream;

	public ConsoleOsOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void writeLine(byte[] data) {
		try {
			outputStream.write(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
