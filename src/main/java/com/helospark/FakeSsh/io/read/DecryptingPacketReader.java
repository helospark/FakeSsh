package com.helospark.FakeSsh.io.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.cipher.SshCipher;
import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Reads a binary packet from stream and decrypts it.
 * @author helospark
 */
@Component
public class DecryptingPacketReader {
	private InputStreamRawDataReader inputStreamRawDataReader;
	private BlockDecryptionService blockDecryptionService;

	@Autowired
	public DecryptingPacketReader(InputStreamRawDataReader inputStreamRawDataReader, BlockDecryptionService blockDecryptionService) {
		this.inputStreamRawDataReader = inputStreamRawDataReader;
		this.blockDecryptionService = blockDecryptionService;
	}

	/**
	 * Reads decrypted packet from the inputStream.
	 * To do this we read the first encryption block just to get the size of the packet,
	 * from that we know how much more to read.
	 * Note: Unfortunatelly we cannot read the data without decryption, because
	 * the packet size itself is encrypted, double decryption is also not possuble
	 * because of the CTR decryption mode. But it would be nicer, if these two conserns
	 * would be separated
	 * @param inputStream to read data from
	 * @param cipher the used cipher
	 * @return read decrypted packet
	 * @throws IOException
	 */
	public byte[] readPacket(InputStream inputStream, Optional<SshCipher> cipher) throws IOException {
		byte[] firstBlock = readFirstBlock(inputStream, cipher);
		byte[] decryptedFirstBlock = decryptData(cipher, firstBlock);

		byte[] remainingData = readRemainingData(inputStream, decryptedFirstBlock);
		byte[] decryptedRemainingData = decryptData(cipher, remainingData);

		return reconstuctNonEncryptedPacket(decryptedFirstBlock, decryptedRemainingData);
	}

	private byte[] readRemainingData(InputStream inputStream, byte[] decryptedFirstBlock) throws IOException {
		int messageSize = ByteConverterUtils.byteArrayToInt(decryptedFirstBlock);
		assertMessageTooLarge(messageSize);
		return inputStreamRawDataReader.readDataWithSize(inputStream, messageSize + 4 - decryptedFirstBlock.length);
	}

	private byte[] readFirstBlock(InputStream inputStream, Optional<SshCipher> cipher) throws IOException {
		int firstBlockSize = cipher.map(c -> c.getBlockSize()).orElse(4);
		return inputStreamRawDataReader.readDataWithSize(inputStream, firstBlockSize);
	}

	private byte[] decryptData(Optional<SshCipher> cipher, byte[] firstBlock) {
		return blockDecryptionService.decryptData(cipher, firstBlock);
	}

	private byte[] reconstuctNonEncryptedPacket(byte[] firstBlock, byte[] remainingData) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(firstBlock);
		byteStream.write(remainingData);
		return byteStream.toByteArray();
	}

	private void assertMessageTooLarge(int messageSize) {
		if (messageSize > ApplicationConstants.MAX_PACKET_SIZE) {
			throw new RuntimeException("Too large packet");
		}
	}

}
