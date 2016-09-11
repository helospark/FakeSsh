package com.helospark.FakeSsh.fakeos;

import com.helospark.FakeSsh.ApplicationConstants;

public abstract class OsOutputStream {
	public void writeNewline() {
		writeLine("\r\n");
	}

	public void writeLine(String string) {
		try {
			writeLine(string.getBytes(ApplicationConstants.SSH_CHARSET));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract void writeLine(byte[] data);
}
