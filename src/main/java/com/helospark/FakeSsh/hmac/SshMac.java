package com.helospark.FakeSsh.hmac;

import java.io.IOException;

/**
 * Calculates and validates the HMAC according to RFC 4253.
 * Calculated mac is MAC(key, sequence_number || unencrypted_packet)
 * @param message to calculate HMAC for
 * @param sequenceNumber the sequence_number
 * @return calculated HMAC
 */
public interface SshMac {
	/**
	 * Calculates HMAC according to RFC 4253.
	 * Calculated mac is MAC(key, sequence_number || unencrypted_packet)
	 * @param message to calculate HMAC for
	 * @param sequenceNumber the sequence_number
	 * @return calculated HMAC
	 */
	public byte[] createMac(byte[] message, int sequenceNumber) throws IOException;

	/**
	 * Check if the given message's HMAC is the same as the given mac.
	 * @param message to check HMAC for
	 * @param mac the HMAC to check against
	 * @param sequenceNumber package index
	 * @return true if the message's HMAC is the same as mac, false otherwise
	 */
	public boolean checkMac(byte[] message, byte[] mac, int sequenceNumber) throws IOException;

	/**
	 * Hmac length in bytes.
	 * @return hmac length in bytes
	 */
	public int getMacLength();
}
