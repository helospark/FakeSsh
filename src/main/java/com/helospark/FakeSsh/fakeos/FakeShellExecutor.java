package com.helospark.FakeSsh.fakeos;

import com.helospark.FakeSsh.fakeos.commands.shell.FakeShell;
import com.helospark.FakeSsh.fakeos.stream.ConsoleOsInputStream;
import com.helospark.FakeSsh.fakeos.stream.ConsoleOsOutputStream;

public class FakeShellExecutor {
	private boolean isRunning = true;
	private FakeOs fakeOs;
	private FakeShell fakeShell;

	public FakeShellExecutor(FakeOs fakeOs, FakeShell fakeShell) {
		this.fakeOs = fakeOs;
		this.fakeShell = fakeShell;
	}

	public void execute() {
		new Thread(() -> run()).start();
	}

	private void run() {
		OsStream standardIOStream = createOsStream();
		while (isRunning) {
			fakeShell.executeProgram(fakeOs, standardIOStream);
		}
	}

	private OsStream createOsStream() {
		return OsStream.builder()
				.withErrorOutput(new ConsoleOsOutputStream(fakeOs.getConsolePipedOutput()))
				.withStandardOutput(new ConsoleOsOutputStream(fakeOs.getConsolePipedOutput()))
				.withStandardInput(new ConsoleOsInputStream(fakeOs.getConsolePipedInput(), fakeOs.getConsolePipedOutput()))
				.build();
	}
}
