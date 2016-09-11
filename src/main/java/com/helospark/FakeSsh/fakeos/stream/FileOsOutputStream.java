package com.helospark.FakeSsh.fakeos.stream;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.fakeos.OsOutputStream;
import com.helospark.FakeSsh.fakeos.filesystem.FakeFilesystem;

public class FileOsOutputStream extends OsOutputStream {
	private FakeFilesystem fakeFilesystem;
	private int fileHandle;

	public FileOsOutputStream(FakeFilesystem fakeFilesystem, SshConnection connection, String fileName) {
		this.fakeFilesystem = fakeFilesystem;
		fileHandle = fakeFilesystem.createFile(fileName);
	}

	@Override
	public void writeLine(byte[] data) {
		fakeFilesystem.appendToFile(fileHandle, data);
	}

}
