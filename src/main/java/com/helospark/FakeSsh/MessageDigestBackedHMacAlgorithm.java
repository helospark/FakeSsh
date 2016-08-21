package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

public class MessageDigestBackedHMacAlgorithm {
	private MessageDigest messageDigest;
	private byte[] ipadXored;
	private byte[] opadXored;

	public MessageDigestBackedHMacAlgorithm(MessageDigest messageDigest, byte[] key, int blockSize) {
		this.messageDigest = messageDigest;
		initializeParameters(key, blockSize);
	}

	private void initializeParameters(byte[] key, int blockSize) {
		byte[] keyPadded = new byte[blockSize];
		if (key.length > blockSize) {
			key = messageDigest.digest(key);
		}
		System.arraycopy(key, 0, keyPadded, 0, key.length);
		ipadXored = new byte[blockSize];
		opadXored = new byte[blockSize];
		for (int i = 0; i < blockSize; ++i) {
			ipadXored[i] = (byte) (keyPadded[i] ^ (byte) 0x36);
			opadXored[i] = (byte) (keyPadded[i] ^ (byte) 0x5c);
		}
	}

	public byte[] createMac(byte[] message) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ipadXored);
		byteStream.write(message);
		byte[] inner = byteStream.toByteArray();
		synchronized (messageDigest) {
			byte[] digestedInner = messageDigest.digest(inner);
			byteStream.reset();
			byteStream.write(opadXored);
			byteStream.write(digestedInner);
			return messageDigest.digest(byteStream.toByteArray());
		}
	}

	public boolean checkMac(byte[] message, byte[] mac) throws IOException {
		return Arrays.equals(mac, createMac(message));
	}

	public int getMacLength() {
		return messageDigest.getDigestLength();
	}

}
