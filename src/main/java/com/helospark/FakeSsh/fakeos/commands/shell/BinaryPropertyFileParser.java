package com.helospark.FakeSsh.fakeos.commands.shell;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class BinaryPropertyFileParser {

	public Map<String, String> parse(String content) {
		Map<String, String> result = new HashMap<>();
		for (String line : content.split("\n")) {
			if (!line.startsWith("#")) {
				String[] keyValue = line.split("=");
				if (keyValue.length != 2) {
					throw new RuntimeException("Unable to parse file");
				}
				result.put(keyValue[0], keyValue[1]);
			}
		}
		return result;
	}
}
