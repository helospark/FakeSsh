package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.ByteConverterUtils;
import com.helospark.FakeSsh.ConnectionClosedException;
import com.helospark.FakeSsh.SshCipher;

/**
 * Reads a binary packet from stream.
 * @author helospark
 */
@Component
public class BinaryPacketReader {

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
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1000];
		do {
			int readBytes = inputStream.read(buffer);
			if (readBytes == -1) {
				throw new ConnectionClosedException();
			}
			byteStream.write(buffer, 0, readBytes);
		} while (inputStream.available() > 0 && byteStream.size() < ApplicationConstants.MAX_PACKET_SIZE);
		if (byteStream.size() > ApplicationConstants.MAX_PACKET_SIZE) {
			throw new RuntimeException("Too large packet");
		}
		return byteStream.toByteArray();
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
		if (packetSize > ApplicationConstants.MAX_PACKET_SIZE) {
			throw new RuntimeException("Too large packet");
		}
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
