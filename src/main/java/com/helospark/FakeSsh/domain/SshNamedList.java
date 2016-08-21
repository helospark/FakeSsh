package com.helospark.FakeSsh.domain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SshNamedList {
	private static final String LIST_SEPARATOR = ",";
	private List<String> elements;

	public SshNamedList(List<String> elements) {
		this.elements = elements;
	}

	public SshNamedList(String elements) {
		this.elements = Arrays.asList(elements.split(LIST_SEPARATOR));
	}

	public List<String> getElements() {
		return elements;
	}

	byte[] serialize() throws IOException {
		String string = elements.stream().collect(Collectors.joining(LIST_SEPARATOR));
		return new SshString(string).serialize();
	}
}
