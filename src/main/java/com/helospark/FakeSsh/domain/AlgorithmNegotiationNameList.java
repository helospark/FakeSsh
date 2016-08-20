package com.helospark.FakeSsh.domain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmNegotiationNameList {
	private static final int SIZE_OF_INTEGER = 4;
	private static final String LIST_SEPARATOR = ",";
	private List<String> elements;

	public AlgorithmNegotiationNameList(List<String> elements) {
		this.elements = elements;
	}

	public AlgorithmNegotiationNameList(String elements) {
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
