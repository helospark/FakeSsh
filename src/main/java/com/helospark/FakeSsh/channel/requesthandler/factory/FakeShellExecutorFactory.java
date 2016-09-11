package com.helospark.FakeSsh.channel.requesthandler.factory;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.FakeShellExecutor;
import com.helospark.FakeSsh.fakeos.commands.shell.FakeShell;

@Component
public class FakeShellExecutorFactory {
	private FakeShell fakeShell;

	public FakeShellExecutorFactory(FakeShell fakeShell) {
		this.fakeShell = fakeShell;
	}

	public FakeShellExecutor createAndExecute(FakeOs fakeOs) {
		FakeShellExecutor fakeShellExecutor = new FakeShellExecutor(fakeOs, fakeShell);
		fakeShellExecutor.execute();
		return fakeShellExecutor;
	}
}
