package com.helospark.FakeSsh.fakeos;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.stream.ConsoleOsInputStream;
import com.helospark.FakeSsh.fakeos.stream.ConsoleOsOutputStream;

@Component
public class OsStreamExtractor {

	public OsStream extractStreamsForPipedCommand(FakeOs os, MutableCommand mutableCommand, OsOutputStream lastStream) {
		// TODO: resolve pipes, and IO redirections
		return OsStream.builder()
				.withErrorOutput(new ConsoleOsOutputStream(os.getConsolePipedOutput()))
				.withStandardInput(new ConsoleOsInputStream(os.getConsolePipedInput(), os.getConsolePipedOutput()))
				.withStandardOutput(new ConsoleOsOutputStream(os.getConsolePipedOutput()))
				.build();
	}

	private int findCharacterNotInString(String command, char charToSearch) {
		int i = command.length() - 1;
		boolean inString = false;
		char stringCharacter = '0';
		while (i > 0) {
			if (!inString && isStringTerminationCharacterAt(command, i)) {
				inString = true;
				stringCharacter = command.charAt(i);
			} else if (inString && command.charAt(i) == stringCharacter) {
				inString = false;
			} else if (!inString && command.charAt(i) == charToSearch) {
				return i;
			}
			--i;
		}
		return -1;
	}

	private boolean isStringTerminationCharacterAt(String command, int i) {
		return command.charAt(i) == '\'' || command.charAt(i) == '"';
	}

}
