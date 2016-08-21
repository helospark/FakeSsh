package com.helospark.FakeSsh;

import java.security.MessageDigest;

/**
 * Hash function that uses MessageDigest as the implementation for the hash function.
 * @author helospark
 *
 */
public class MessageDigestBackedSshHash implements SshHash {
	private MessageDigest messageDigest;

	public MessageDigestBackedSshHash(MessageDigest messageDigest) {
		this.messageDigest = messageDigest;
	}

	@Override
	public byte[] hash(byte[] byteArray) {
		synchronized (messageDigest) {
			return messageDigest.digest(byteArray);
		}
	}

}
