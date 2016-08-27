package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Extracts payload from a decrypted SSH packet.
 * @author helospark
 */
@Component
public class PacketPayloadExtractor {

	public byte[] extractResult(byte[] decryptedPacket) throws IOException {
		ByteArrayInputStream inputByteStream = new ByteArrayInputStream(decryptedPacket);
		byte[] packetSizeAsByteArray = readBytes(inputByteStream, 4);
		int packageSize = ByteConverterUtils.byteArrayToInt(packetSizeAsByteArray);
		byte paddingSize = (byte) inputByteStream.read();
		return readBytes(inputByteStream, packageSize - paddingSize - 1);
	}

	private byte[] readBytes(InputStream inputStream, int size) throws IOException {
		byte[] dataRead = new byte[size];
		inputStream.read(dataRead);
		return dataRead;
	}
}
