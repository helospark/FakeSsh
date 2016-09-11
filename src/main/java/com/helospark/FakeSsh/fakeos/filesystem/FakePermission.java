package com.helospark.FakeSsh.fakeos.filesystem;

public class FakePermission {
	private boolean read, write, execute;

	public void setRead(boolean read) {
		this.read = read;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	public boolean hasReadPermission() {
		return read;
	}

	public boolean hasWritePermission() {
		return write;
	}

	public boolean hasExecutePermission() {
		return execute;
	}

}
