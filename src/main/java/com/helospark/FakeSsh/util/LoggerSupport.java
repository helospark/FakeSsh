package com.helospark.FakeSsh.util;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Service for common log operations.
 * @author helospark
 */
@Component
public class LoggerSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerSupport.class);
	private static final String NEW_LINE = "\n";

	/**
	 * Dump a byte array as hexadecimal string delimited by ':' 16 byte/row
	 * in debug level.
	 * @param data to dump
	 * @param title to show before the dump
	 */
	public void dumpByteArrayInHex(byte[] data, String title) {
		StringBuilder builder = new StringBuilder();
		builder.append(title);
		builder.append(NEW_LINE);
		for (int i = 0; i < data.length; ++i) {
			builder.append(String.format("%02x:", data[i]));
			if (i % 16 == 0 && i != 0) {
				builder.append(NEW_LINE);
			}
		}
		builder.append(NEW_LINE).append(NEW_LINE);
		logDebugString(builder.toString());
	}

	/**
	 * Dump {@link BigInteger} as a hex string.
	 * @see dumpByteArrayInHex
	 * @param data to dump
	 * @param title to show before the dump
	 */
	public void dumpBigIntegerInHex(BigInteger data, String title) {
		dumpByteArrayInHex(data.toByteArray(), title);
	}

	/**
	 * Log a String in debug level.
	 * @param string to log
	 */
	public void logDebugString(String string) {
		LOGGER.debug(string);
	}

	public void logInfoString(String string) {
		LOGGER.info(string);
	}

	public void logException(Exception e) {
		LOGGER.warn("Exception occurred", e);
	}
}
