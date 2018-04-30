package com.helospark.FakeSsh.hostkey;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.util.BufferedReaderFactory;

@Component
public class Base64PrivateKeyReader {
	private static final String COMMENT_START_CHARACTERS = "--";
	private BufferedReaderFactory bufferedReaderFactory;

	@Autowired
	public Base64PrivateKeyReader(BufferedReaderFactory bufferedReaderFactory) {
		this.bufferedReaderFactory = bufferedReaderFactory;
	}

	public byte[] read(String fileName) throws Exception {
		BufferedReader stream = bufferedReaderFactory.createClasspathBufferedReader(fileName);
		String content = readEntireContent(stream);
		return Base64.getDecoder().decode(content);
	}

	private String readEntireContent(BufferedReader stream) throws IOException {
		String content = new String();
		while (stream.ready()) {
			String line = stream.readLine();
			if (!line.startsWith(COMMENT_START_CHARACTERS)) {
				content += line;
			}
		}
		return content;
	}
}
