package com.helospark.FakeSsh.hmac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * SSH HMAC that uses MessageDigest as the hash function implementation.
 * @author helospark
 */
public class MessageDigestBackedSshHmac implements SshMac {
	private MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm;

	public MessageDigestBackedSshHmac(MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm) {
		this.messageDigestBackedHMacAlgorithm = messageDigestBackedHMacAlgorithm;
	}

	@Override
	public byte[] createMac(byte[] message, int packageNumber) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByteArray(packageNumber));
		byteStream.write(message);
		return messageDigestBackedHMacAlgorithm.createMac(byteStream.toByteArray());
	}

	@Override
	public boolean checkMac(byte[] message, byte[] mac, int packageNumber) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByteArray(packageNumber));
		byteStream.write(message);
		return messageDigestBackedHMacAlgorithm.checkMac(byteStream.toByteArray(), mac);
	}

	@Override
	public int getMacLength() {
		return messageDigestBackedHMacAlgorithm.getMacLength();
	}

}
