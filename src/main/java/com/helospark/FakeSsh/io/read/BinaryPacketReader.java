package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.bouncycastle.util.Arrays;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.ConnectionClosedException;
import com.helospark.FakeSsh.cipher.SshCipher;
import com.helospark.FakeSsh.hmac.SshMac;
import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Reads a binary packet from stream.
 * @author helospark
 */
@Component
public class BinaryPacketReader {

	/**
	 * Reads a packet.
	 * @param cipher the used cipher
	 * @param mac the used mac
	 * @param inputStream to read data from
	 * @return read decrypted packet
	 * @throws IOException
	 */
	public byte[] readPacket(Optional<SshCipher> cipher, Optional<SshMac> mac, InputStream inputStream) throws IOException {
		byte[] firstBlock = readFirstBlock(inputStream, cipher);
		byte[] decryptedFirstBlock = decryptData(cipher, firstBlock);

		byte[] remainingData = readRemainingData(inputStream, decryptedFirstBlock);
		byte[] decryptedRemainingData = decryptData(cipher, remainingData);

		byte[] macBytes = readMac(inputStream, mac);

		return reconstuctNonEncryptedPacket(decryptedFirstBlock, decryptedRemainingData, macBytes);
	}

	private byte[] readRemainingData(InputStream inputStream, byte[] decryptedFirstBlock) throws IOException {
		int messageSize = ByteConverterUtils.byteArrayToInt(decryptedFirstBlock);
		assertMessageTooLarge(messageSize);
		return readDataWithSize(inputStream, messageSize + 4 - decryptedFirstBlock.length);
	}

	private byte[] readMac(InputStream inputStream, Optional<SshMac> mac) throws IOException {
		int macLength = mac.map(m -> m.getMacLength()).orElse(0);
		return readDataWithSize(inputStream, macLength);
	}

	private byte[] readFirstBlock(InputStream inputStream, Optional<SshCipher> cipher) throws IOException {
		int firstBlockSize = cipher.map(c -> c.getBlockSize()).orElse(4);
		return readDataWithSize(inputStream, firstBlockSize);
	}

	private byte[] reconstuctNonEncryptedPacket(byte[] firstBlock, byte[] remainingData, byte[] mac) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(firstBlock);
		byteStream.write(remainingData);
		byteStream.write(mac);
		return byteStream.toByteArray();
	}

	private void assertMessageTooLarge(int messageSize) {
		if (messageSize > ApplicationConstants.MAX_PACKET_SIZE) {
			throw new RuntimeException("Too large packet");
		}
	}

	private byte[] decryptData(Optional<SshCipher> cipher, byte[] data) {
		byte[] decryptedData;
		if (cipher.isPresent()) {
			decryptedData = cipher.get().decrypt(data);
		} else {
			decryptedData = data;
		}
		return decryptedData;
	}

	private byte[] readDataWithSize(InputStream inputStream, int messageSizeToRead) throws IOException {
		if (messageSizeToRead == 0) {
			return new byte[0];
		}
		byte[] readData = new byte[messageSizeToRead];
		int readResult = inputStream.read(readData);
		if (readResult == -1) {
			throw new ConnectionClosedException();
		}
		return Arrays.copyOfRange(readData, 0, readResult);
	}
}
