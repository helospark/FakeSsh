package com.helospark.FakeSsh;

/**
 * Common constants for this application.
 * @author helospark
 */
public class ApplicationConstants {
	public static final String SSH_CHARSET = "utf-8";
	public static final int INTEGER_LENGTH_IN_BYTES = 4;
	public static final int BYTE_SIZE = 1;
	public static final int PACKET_LENGTH_FIELD_SIZE = INTEGER_LENGTH_IN_BYTES;
	public static final int PADDING_LENGTH_FIELD_SIZE = BYTE_SIZE;
	public static final int MINIMUM_PADDING_SIZE = 4;
	public static final int DEFAULT_PACKET_LENGTH_ALIGNMENT = 8;
	public static final int MAX_PACKET_SIZE = 65000;
	public static final int MAX_STRING_SIZE = 10000;
}
