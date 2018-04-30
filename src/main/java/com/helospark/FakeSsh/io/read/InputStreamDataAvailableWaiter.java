package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;

import com.helospark.lightdi.annotation.Component;

/**
 * Waits until data is available and returns whether it succeeded.
 * TODO: Seems a bit hacky, there might be (and should be) a better way for this
 * @author helospark
 */
@Component
public class InputStreamDataAvailableWaiter {
	private static final int WAIT_TIME_PER_ITERATION = 100;
	private static final int MAXIMUM_WAIT = 100000;

	public boolean waitForData(InputStream inputStream) {
		try {
			waitForDataWithTimeout(inputStream, MAXIMUM_WAIT);
			return inputStream.available() <= 0;
		} catch (IOException | InterruptedException e) {
			return false;
		}
	}

	private void waitForDataWithTimeout(InputStream inputStream, int timeout) throws IOException, InterruptedException {
		int waitTime = 0;
		while (inputStream.available() <= 0 && waitTime < timeout) {
			Thread.sleep(WAIT_TIME_PER_ITERATION);
			waitTime += WAIT_TIME_PER_ITERATION;
		}
	}
}
