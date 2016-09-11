package com.helospark.FakeSsh.fakeos.commands.shell;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.FakeOs;

@Component
public class EnvironmentVariableResolver {

	public String resolveVariables(FakeOs os, String command) {
		Map<String, String> environmentVariables = os.getEnvironmentVariables();
		for (Map.Entry<String, String> variable : environmentVariables.entrySet()) {
			command = replaceEnvironmentVariable(command, variable);
		}
		return command;
	}

	private String replaceEnvironmentVariable(String command, Entry<String, String> variable) {
		return command.replaceAll("$" + variable.getKey(), variable.getValue());
	}
}
