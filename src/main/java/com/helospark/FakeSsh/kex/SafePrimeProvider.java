package com.helospark.FakeSsh.kex;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.helospark.FakeSsh.domain.GeneratedPrime;
import com.helospark.FakeSsh.util.BufferedReaderFactory;
import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;
import com.helospark.lightdi.annotation.Value;

/**
 * Provides sage primes according to according to RFC 4419.
 * This implementation uses OpenSSH's generated primes
 * @author helospark
 */
@Component
public class SafePrimeProvider {
    private static final char COMMENT_CHARACTER = '#';
    private static final String PRIME_LINE_SEPARATOR = " ";
    private static final int BIT_SIZE_INDEX = 4;
    private static final int GENERATOR_INDEX = 5;
    private static final int PRIME_INDEX = 6;
    private static final int NUMBER_OF_ELEMENTS_PER_LINE = 7;
    private static final int BASE_SIXTEEN = 16;
    private String fileName;
    private List<GeneratedPrime> generatedPrimes = new ArrayList<>();
    private BufferedReaderFactory bufferedReaderFactory;

    /**
     * Constructor.
     * @param fileName OpenSSH's safe prime database file
     * @param bufferedReaderFactory to read the above file
     */
    @Autowired
    public SafePrimeProvider(@Value("${PRIME_DATABASE}") String fileName, BufferedReaderFactory bufferedReaderFactory) {
        this.fileName = fileName;
        this.bufferedReaderFactory = bufferedReaderFactory;
    }

    @PostConstruct
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
            if (shouldSkipLine(line)) {
                continue;
            }
            processLine(line);
        }
    }

    private boolean shouldSkipLine(String line) {
        return isLineEmpty(line) || isLineComment(line);
    }

    private boolean isLineEmpty(String line) {
        return line.length() == 0;
    }

    private boolean isLineComment(String line) {
        return line.charAt(0) == COMMENT_CHARACTER;
    }

    private void processLine(String line) throws IOException {
        String[] lineParts = getLineParts(line);
        GeneratedPrime generatedPrime = createGeneratedPrime(lineParts);
        generatedPrimes.add(generatedPrime);
    }

    private String[] getLineParts(String line) throws IOException {
        String[] lineParts = line.split(PRIME_LINE_SEPARATOR);
        if (lineParts.length < NUMBER_OF_ELEMENTS_PER_LINE) {
            throw new RuntimeException(fileName + " is not in a valid format");
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
        List<GeneratedPrime> primes = filterNonUsablePrimes(minimumLength, maximumLength);
        if (primes.isEmpty()) {
            throw new RuntimeException("No primes satisfy the length limits");
        }
        primes.sort((p1, p2) -> p1.getBitLength() - p2.getBitLength());
        // TODO: The primes may need to be sorted by the closeness of preferred length 
        // but for performance reasons, choose a small one for now
        return primes.get(0);
    }

    private List<GeneratedPrime> filterNonUsablePrimes(int minimumLength, int maximumLength) {
        return generatedPrimes.stream()
                .filter(prime -> prime.getBitLength() >= minimumLength)
                .filter(prime -> prime.getBitLength() <= maximumLength)
                .collect(Collectors.toList());
    }
}
