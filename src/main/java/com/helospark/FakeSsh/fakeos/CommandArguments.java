package com.helospark.FakeSsh.fakeos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandArguments {
	// arguments like --help
	private Map<String, String> longNamedArguments = new HashMap<>();
	// arguments like -h
	private Map<Character, String> shortNamedArguments = new HashMap<>();
	// Arguments without name, like the one you write after echo
	private List<String> parameters = new ArrayList<>();

	public Map<String, String> getLongNamedArguments() {
		return longNamedArguments;
	}

	public Map<Character, String> getShortNamedArguments() {
		return shortNamedArguments;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void addLongArgument(String key, String value) {
		longNamedArguments.put(key, value);
	}

	public void addShortArgument(Character key, String value) {
		shortNamedArguments.put(key, value);
	}

	public void addParameter(String value) {
		parameters.add(value);
	}

}
