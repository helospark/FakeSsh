package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessageDigestBackedSshHmac implements SshMac {
	private MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm;

	public MessageDigestBackedSshHmac(MessageDigestBackedHMacAlgorithm messageDigestBackedHMacAlgorithm) {
		this.messageDigestBackedHMacAlgorithm = messageDigestBackedHMacAlgorithm;
	}

	@Override
	public byte[] createMac(byte[] message, int packageNumber) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByte(packageNumber));
		byteStream.write(message);
		return messageDigestBackedHMacAlgorithm.createMac(byteStream.toByteArray());
	}

	@Override
	public boolean checkMac(byte[] message, byte[] mac, int packageNumber) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByte(packageNumber));
		byteStream.write(message);
		return messageDigestBackedHMacAlgorithm.checkMac(byteStream.toByteArray(), mac);
	}

	@Override
	public int getMacLength() {
		return messageDigestBackedHMacAlgorithm.getMacLength();
	}

}
