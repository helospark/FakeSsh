package com.helospark.FakeSsh.hostkey;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Creates ServerHostKeyAlgorithm based on it's name or throws exception.
 * @author helospark
 */
@Component
public class ServerHostKeyAlgorithmProvider {
	private List<ServerHostKeyAlgorithm> algorithms;

	public ServerHostKeyAlgorithmProvider(List<ServerHostKeyAlgorithm> algorithms) {
		this.algorithms = algorithms;
	}

	public String getJoinedSupportedAlgorithmList() {
		return algorithms.stream()
				.map(algorithm -> algorithm.getSignatureName())
				.collect(Collectors.joining(","));
	}

	public ServerHostKeyAlgorithm provideHostKeyAlgorithm(String serverHostKeyAlgorithm) throws Exception {
		return algorithms.stream()
				.filter(algorithm -> algorithm.getSignatureName().equals(serverHostKeyAlgorithm))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No server host key algorithm named " + serverHostKeyAlgorithm));
	}

}
