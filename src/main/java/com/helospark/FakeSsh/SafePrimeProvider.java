package com.helospark.FakeSsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.GeneratedPrime;

@Component
public class SafePrimeProvider implements InitializingBean {
	private static final String SEPARATOR = " ";
	private static final int BIT_SIZE_INDEX = 4;
	private static final int GENERATOR_INDEX = 5;
	private static final int PRIME_INDEX = 6;
	private static final int NUMBER_OF_ELEMENTS_PER_LINE = 7;
	private static final int BASE_SIXTEEN = 16;
	private String fileName;
	private List<GeneratedPrime> generatedPrimes = new ArrayList<>();
	private BufferedReaderFactory bufferedReaderFactory;

	@Autowired
	private SafePrimeProvider(@Value("${PRIME_DATABASE}") String fileName, BufferedReaderFactory bufferedReaderFactory) {
		this.fileName = fileName;
		this.bufferedReaderFactory = bufferedReaderFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		BufferedReader fileInputStream = bufferedReaderFactory.createClasspathBufferedReader(fileName);
		try {
			initializeFromFile(fileInputStream);
		} finally {
			fileInputStream.close();
		}
	}

	private void initializeFromFile(BufferedReader fileInputStream) throws IOException {
		while (fileInputStream.ready()) {
			String line = fileInputStream.readLine();
			if (line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}
			processLine(line);
		}
	}

	private void processLine(String line) throws IOException {
		String[] lineParts = getLineParts(line);
		GeneratedPrime generatedPrime = createGeneratedPrime(lineParts);
		generatedPrimes.add(generatedPrime);
	}

	private String[] getLineParts(String line) throws IOException {
		String[] lineParts = line.split(SEPARATOR);
		if (lineParts.length < NUMBER_OF_ELEMENTS_PER_LINE) {
			throw new RuntimeException(fileName + " is not valid");
		}
		return lineParts;
	}

	private GeneratedPrime createGeneratedPrime(String[] lineParts) {
		int bits = Integer.valueOf(lineParts[BIT_SIZE_INDEX]);
		BigInteger generator = new BigInteger(lineParts[GENERATOR_INDEX]);
		BigInteger prime = new BigInteger(lineParts[PRIME_INDEX], BASE_SIXTEEN);
		GeneratedPrime generatedPrime = new GeneratedPrime(bits, generator, prime);
		return generatedPrime;
	}

	public GeneratedPrime providePrime(int minimumLength, int preferredLength, int maximumLength) {
		return generatedPrimes.stream()
				.filter(prime -> prime.getBitLength() >= minimumLength)
				.filter(prime -> prime.getBitLength() <= maximumLength)
				.sorted((prime1, prime2) -> comparePrimes(prime1, prime2, preferredLength))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No prime fulfilling the criteria"));
	}

	private int comparePrimes(GeneratedPrime prime1, GeneratedPrime prime2, int preferredLength) {
		return Math.abs(prime1.getBitLength() - preferredLength) < Math.abs(prime2.getBitLength() - preferredLength) ? 1 : 0;
	}
}
