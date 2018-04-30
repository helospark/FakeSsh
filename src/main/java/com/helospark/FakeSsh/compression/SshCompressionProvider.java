package com.helospark.FakeSsh.compression;

import java.util.List;
import java.util.stream.Collectors;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

@Component
public class SshCompressionProvider {
	private List<SshCompressionFactory> compressionFactories;

	@Autowired
	public SshCompressionProvider(List<SshCompressionFactory> compressionFactories) {
		this.compressionFactories = compressionFactories;
	}

	public SshCompression provideCompressionAlgorithm(String name) {
		return compressionFactories.stream()
				.filter(compressionAlgorithm -> compressionAlgorithm.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No compression algorithm found by " + name))
				.create();
	}

	public List<String> provideSupportedAlgorithms() {
		return compressionFactories.stream()
				.map(compressionAlgorithm -> compressionAlgorithm.getName())
				.collect(Collectors.toList());
	}

}
