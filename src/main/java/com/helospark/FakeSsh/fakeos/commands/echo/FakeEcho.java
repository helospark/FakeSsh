package com.helospark.FakeSsh.fakeos.commands.echo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.CommandArguments;
import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.FakeProgram;
import com.helospark.FakeSsh.fakeos.OsOutputStream;
import com.helospark.FakeSsh.fakeos.OsStream;

@Component
public class FakeEcho extends FakeProgram {
	private Map<String, String> escapeCharacters = new HashMap<>();

	public FakeEcho() {
		escapeCharacters.put("\\b", "\b");
		escapeCharacters.put("\\", "\\");
		escapeCharacters.put("\\a", "\u0007");
		escapeCharacters.put("\\b", "\b");
		escapeCharacters.put("\\e", "\u001b");
		escapeCharacters.put("\\f", "\f");
		escapeCharacters.put("\\n", "\n");
		escapeCharacters.put("\\r", "\r");
		escapeCharacters.put("\\t", "\t");
		escapeCharacters.put("\\v", "\000b");
	}

	@Override
	public int executeProgram(FakeOs os, OsStream osStream, CommandArguments arguments) {
		OsOutputStream standardOutputStream = osStream.getStandardOutput();
		arguments.getParameters().stream()
				.map(parameter -> escapeParameterIfRequired(arguments, parameter))
				.forEach(parameter -> standardOutputStream.writeLine(parameter));
		if (!arguments.getShortNamedArguments().containsKey('n')) {
			osStream.getStandardOutput().writeNewline();
		}
		return 0;
	}

	private String escapeParameterIfRequired(CommandArguments arguments, String parameter) {
		boolean isEscapeSequenceActivated = arguments.getShortNamedArguments().containsKey('e');

		if (isEscapeSequenceActivated) {
			for (Map.Entry<String, String> escapeChar : escapeCharacters.entrySet()) {
				parameter = parameter.replaceAll(escapeChar.getKey(), escapeChar.getValue());
			}
			// TODO: \0, \x, \c
		}
		return parameter;
	}

	@Override
	public boolean canHandle(String programName) {
		return programName.equals("echo");
	}
}
