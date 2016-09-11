package com.helospark.FakeSsh.fakeos.commands.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.MutableCommand;
import com.helospark.FakeSsh.fakeos.OsOutputStream;
import com.helospark.FakeSsh.fakeos.OsStream;
import com.helospark.FakeSsh.fakeos.OsStreamExtractor;

@Component
public class PipedCommandRunner {
	private OsStreamExtractor osStreamExtractor;
	private OsCommandInterpreter osCommandInterpreter;

	@Autowired
	public PipedCommandRunner(OsStreamExtractor osStreamExtractor, OsCommandInterpreter osCommandInterpreter) {
		this.osStreamExtractor = osStreamExtractor;
		this.osCommandInterpreter = osCommandInterpreter;
	}

	public Integer runCommand(FakeOs os, String command) {
		String[] pipedCommands = command.split("\\|");
		OsOutputStream osStream = null;
		runPipedCommand(os, osStream, pipedCommands[0]);
		for (int i = 1; i < pipedCommands.length; ++i) {
			runPipedCommand(os, osStream, pipedCommands[i]);
		}
		return 0; // TODO!
	}

	private OsOutputStream runPipedCommand(FakeOs os, OsOutputStream lastStream, String command) {
		MutableCommand mutableCommand = new MutableCommand(command);
		OsStream osStream = osStreamExtractor.extractStreamsForPipedCommand(os, mutableCommand, lastStream);
		osCommandInterpreter.interpret(os, mutableCommand.getCommand(), osStream);
		return osStream.getStandardOutput();
	}
}
