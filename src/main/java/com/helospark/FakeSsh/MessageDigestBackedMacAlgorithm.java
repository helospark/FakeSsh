package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

import org.springframework.beans.factory.InitializingBean;

public class MessageDigestBackedMacAlgorithm implements SshMac, InitializingBean {
	private MessageDigest messageDigest;
	private int blockSize;
	private byte[] key;
	private byte[] ipadXored;
	private byte[] opadXored;

	public MessageDigestBackedMacAlgorithm(MessageDigest messageDigest, byte[] key, int blockSize) {
		this.messageDigest = messageDigest;
		this.blockSize = blockSize;
		this.key = key;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyPadded = new byte[blockSize];
		System.arraycopy(key, 0, keyPadded, 0, key.length);
		ipadXored = new byte[blockSize];
		opadXored = new byte[blockSize];
		for (int i = 0; i < blockSize; ++i) {
			ipadXored[i] = (byte) (keyPadded[i] ^ (byte) 0x36);
			opadXored[i] = (byte) (keyPadded[i] ^ (byte) 0x36);
		}
	}

	@Override
	public byte[] createMac(byte[] message, int packageNumber) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByte(packageNumber));
		byteStream.write(message);
		byte[] messageToDigest = byteStream.toByteArray();
		byteStream.reset();
		byteStream.write(ipadXored);
		byteStream.write(messageToDigest);
		byte[] inner = byteStream.toByteArray();
		synchronized (messageDigest) {
			byte[] digestedInner = messageDigest.digest(inner);
			byteStream.reset();
			byteStream.write(opadXored);
			byteStream.write(digestedInner);
			return messageDigest.digest(byteStream.toByteArray());
		}
	}

	@Override
	public boolean checkMac(byte[] message, byte[] mac, int packageNumber) throws IOException {
		return Arrays.equals(mac, createMac(message, packageNumber));
	}

	@Override
	public int getMacLength() {
		return messageDigest.getDigestLength();
	}

}
