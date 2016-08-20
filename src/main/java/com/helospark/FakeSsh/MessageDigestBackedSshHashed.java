package com.helospark.FakeSsh;

import java.security.MessageDigest;

public class MessageDigestBackedSshHashed implements SshHash {
	private MessageDigest messageDigest;

	public MessageDigestBackedSshHashed(MessageDigest messageDigest) {
		this.messageDigest = messageDigest;
	}

	@Override
	public byte[] hash(byte[] byteArray) {
		synchronized (messageDigest) {
			return messageDigest.digest(byteArray);
		}
	}

}
