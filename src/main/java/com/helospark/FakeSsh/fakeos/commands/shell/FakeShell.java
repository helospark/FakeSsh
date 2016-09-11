package com.helospark.FakeSsh.fakeos.commands.shell;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.OsStream;

@Component
public class FakeShell {
	private PipedCommandRunner pipedCommandRunner;
	private WildcardResolver wildcardResolver;
	private EnvironmentVariableResolver environmentVariableResolver;

	public FakeShell(PipedCommandRunner pipedCommandRunner, WildcardResolver wildcardResolver, EnvironmentVariableResolver environmentVariableResolver) {
		this.pipedCommandRunner = pipedCommandRunner;
		this.wildcardResolver = wildcardResolver;
		this.environmentVariableResolver = environmentVariableResolver;
	}

	public int executeProgram(FakeOs os, OsStream osStream) {
		osStream.getStandardOutput().writeLine(os.getCurrentUser() + "@" + os.getCurrentDomain() + ":" + os.getFileSystem().getCurrentDirectory().toFullyQualifiedString() + "$ ");
		String line = osStream.getStandardInput().readLine();
		if (line.equals("")) {
			osStream.getStandardOutput().writeNewline();
			return 0;
		}
		executeCommand(os, line);
		return 0;
	}

	private void executeCommand(FakeOs os, String line) {
		List<String> commands = Arrays.asList(line.split(";"));
		commands.stream()
				.map(command -> environmentVariableResolver.resolveVariables(os, command))
				.map(command -> wildcardResolver.expandWildcards(os, command))
				.forEach(command -> pipedCommandRunner.runCommand(os, command));
	}
}
