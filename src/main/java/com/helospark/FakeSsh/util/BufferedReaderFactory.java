package com.helospark.FakeSsh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Builds {@link BufferedReader} from a file or filename.
 * @author helospark
 */
@Component
public class BufferedReaderFactory {

	public BufferedReader createClasspathBufferedReader(String fileName) throws IOException {
		Resource resource = new ClassPathResource(fileName);
		return new BufferedReader(new InputStreamReader(resource.getInputStream()));
	}

	public BufferedReader createFromFile(File file) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}
}
