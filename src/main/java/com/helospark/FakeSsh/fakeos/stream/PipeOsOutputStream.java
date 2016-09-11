package com.helospark.FakeSsh.fakeos.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.fakeos.OsOutputStream;

public class PipeOsOutputStream extends OsOutputStream {
	private ByteArrayOutputStream byteStream;

	public PipeOsOutputStream() {
		byteStream = new ByteArrayOutputStream();
	}

	@Override
	public void writeLine(byte[] data) {
		try {
			byteStream.write(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
