package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ByteConverterUtils;
import com.helospark.FakeSsh.SshCipher;

/**
 * Reads a binary packet from stream.
 * @author helospark
 */
@Component
public class BinaryPacketReader {
	private static final int MAXIMUM_PACKET_SIZE = 65000;

	public byte[] readPacket(Optional<SshCipher> cipher, InputStream inputStream) throws IOException {
		if (cipher.isPresent()) {
			return readEncryptedData(inputStream);
		} else {
			return readNonEncryptedData(inputStream);
		}
	}

	/**
	 * Based on the RFC, when the data is encrypted all data to one direction
	 * can be considered part of the same packet.
	 * @param inputStream to read from
	 * @return read data
	 */
	private byte[] readEncryptedData(InputStream inputStream) throws IOException {
		byte[] packet = new byte[MAXIMUM_PACKET_SIZE];
		int readBytes = inputStream.read(packet);
		return Arrays.copyOfRange(packet, 0, readBytes);
	}

	/**
	 * When data is not encrypted, read the number of bytes from the beginning and use
	 * that to determine where the packet will end. If done the same way as an encrypted
	 * packet, sometimes multiple packets may be read with one call.
	 * @param inputStream to read from
	 * @return read data
	 */
	private byte[] readNonEncryptedData(InputStream inputStream) throws IOException {
		byte[] packetSizeBytes = readPacketSize(inputStream);
		int packetSize = convertPacketSizeToInt(packetSizeBytes);
		byte[] packet = readPacket(inputStream, packetSize);
		return reconstructOriginalPacket(packetSizeBytes, packet);
	}

	private byte[] readPacketSize(InputStream inputStream) throws IOException {
		byte[] packetSizeBytes = new byte[4];
		inputStream.read(packetSizeBytes);
		return packetSizeBytes;
	}

	private int convertPacketSizeToInt(byte[] packetSizeBytes) {
		return ByteConverterUtils.byteArrayToInt(packetSizeBytes);
	}

	private byte[] readPacket(InputStream inputStream, int numberOfBytes) throws IOException {
		byte[] packet = new byte[numberOfBytes];
		inputStream.read(packet);
		return packet;
	}

	private byte[] reconstructOriginalPacket(byte[] packetSizeBytes, byte[] packet) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(packetSizeBytes);
		byteStream.write(packet);
		return byteStream.toByteArray();
	}
}
