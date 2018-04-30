package com.helospark.FakeSsh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.helospark.lightdi.annotation.Component;

/**
 * Builds {@link BufferedReader} from a file or filename.
 * @author helospark
 */
@Component
public class BufferedReaderFactory {

    public BufferedReader createClasspathBufferedReader(String fileName) throws IOException {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public BufferedReader createFromFile(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }
}
