package com.helospark.FakeSsh.fakeos;

public abstract class FakeProgram {
	public abstract int executeProgram(FakeOs os, OsStream osStream, CommandArguments arguments);

	public abstract boolean canHandle(String programName);
}
