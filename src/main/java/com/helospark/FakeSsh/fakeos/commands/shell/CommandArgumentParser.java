package com.helospark.FakeSsh.fakeos.commands.shell;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.CommandArguments;

@Component
public class CommandArgumentParser {

	public CommandArguments parseArguments(String command) {
		CommandArguments result = new CommandArguments();
		String[] commandParts = command.split(" ");
		for (int i = 1; i < commandParts.length; ++i) {
			if (commandParts[i].startsWith("--")) {
				if (i < commandParts.length - 1) {
					result.addLongArgument(commandParts[i].substring(2), commandParts[i + 1]);
					++i;
				}
			} else if (commandParts[i].startsWith("-")) {
				if (i < commandParts.length - 1) {
					result.addShortArgument(commandParts[i].charAt(1), commandParts[i + 1]);
					++i;
				}
			} else {
				result.addParameter(commandParts[i]);
			}
		}
		return result;
	}
}
