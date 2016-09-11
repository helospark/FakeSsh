package com.helospark.FakeSsh.fakeos.commands.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.CommandArguments;
import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.FakeProgram;
import com.helospark.FakeSsh.fakeos.OsStream;
import com.helospark.FakeSsh.fakeos.filesystem.DirectoryEntry;

@Component
public class OsCommandInterpreter {
	private List<FakeProgram> commandList = new ArrayList<>();
	private BinaryPropertyFileParser binaryPropertyFileParser;
	private CommandArgumentParser commandArgumentParser;

	@Autowired
	public OsCommandInterpreter(List<FakeProgram> commandList, BinaryPropertyFileParser binaryPropertyFileParser,
			CommandArgumentParser commandArgumentParser) {
		this.commandList = commandList;
		this.binaryPropertyFileParser = binaryPropertyFileParser;
		this.commandArgumentParser = commandArgumentParser;
	}

	public void interpret(FakeOs os, String command, OsStream osStream) {
		String programFileName = extractProgramName(command);
		String programName = findProgram(os, programFileName);

		CommandArguments argument = extractArguments(command);
		Optional<FakeProgram> program = findProgramThatCanHandle(programName);
		if (program.isPresent()) {
			program.get().executeProgram(os, osStream, argument);
		}
	}

	private Optional<FakeProgram> findProgramThatCanHandle(String programName) {
		return commandList.stream()
				.filter(program -> program.canHandle(programName))
				.findFirst();
	}

	private String findProgram(FakeOs os, String programName) {
		Optional<DirectoryEntry> result = findAbsoluteOrRelative(os, programName);
		if (!result.isPresent()) {
			result = tryFindInPathLocations(os, programName);
		}
		if (result.isPresent()) {
			String content = result.get().getContent();
			Map<String, String> properties = binaryPropertyFileParser.parse(content);
			Optional<String> referencedCommand = Optional.ofNullable(properties.get("referenced_command"));
			return referencedCommand.orElseThrow(() -> new RuntimeException("Referencedcommand is not found"));
		}
		return "";
	}

	private Optional<DirectoryEntry> tryFindInPathLocations(FakeOs os, String programName) {
		Optional<DirectoryEntry> result;
		String[] pathSegments = os.getEnvironmentVariables().get("PATH").split(":");
		for (String fullyQualifierProgramName : pathSegments) {
			if (!fullyQualifierProgramName.endsWith("/")) {
				fullyQualifierProgramName += "/";
			}
			fullyQualifierProgramName += programName;
			result = findAbsoluteOrRelative(os, fullyQualifierProgramName);
			if (result.isPresent()) {
				return result;
			}
		}
		return Optional.empty();
	}

	private Optional<DirectoryEntry> findAbsoluteOrRelative(FakeOs os, String programName) {
		return os.getFileSystem().findFile(programName);
	}

	private String extractProgramName(String command) {
		return command.split(" ")[0];
	}

	private CommandArguments extractArguments(String command) {
		return commandArgumentParser.parseArguments(command);
	}
}
