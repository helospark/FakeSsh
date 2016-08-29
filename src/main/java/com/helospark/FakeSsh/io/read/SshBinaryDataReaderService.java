package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Reads a binary packet from the given connection.
 * Orchastrating and delegating to other services
 * @author helospark
 */
@Component
public class SshBinaryDataReaderService {
	private BinaryPacketReader binaryPacketReader;
	private PacketMacValidator packetMacValidator;
	private PacketDecryptionService packetDecryptionService;
	private PacketPayloadExtractor packetPayloadExtractor;
	private MacExtractorService macExtractorService;

	@Autowired
	public SshBinaryDataReaderService(BinaryPacketReader binaryPacketReader, PacketMacValidator packetMacValidator,
			PacketDecryptionService packetDecryptionService, PacketPayloadExtractor packetPayloadExtractor,
			MacExtractorService macExtractorService) {
		this.binaryPacketReader = binaryPacketReader;
		this.packetMacValidator = packetMacValidator;
		this.packetDecryptionService = packetDecryptionService;
		this.packetPayloadExtractor = packetPayloadExtractor;
		this.macExtractorService = macExtractorService;
	}

	/**
	 * Reads a packet from the given connection.
	 * @param connection to read packet from
	 * @return decrypted bytes for the packet
	 * @throws IOException
	 */
	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] packet = readRawPacket(connection);
		return processPacket(connection, packet);
	}

	/**
	 * Peeks the packet without reading.
	 * @param connection to read packet from
	 * @return decrypted bytes for the packet
	 * @throws IOException
	 */
	public byte[] peekPacket(SshConnection connection) throws IOException {
		byte[] packet = readRawPacket(connection);
		byte[] result = processPacket(connection, packet);
		connection.getConnection().getInputStream().unread(packet);
		return result;
	}

	private byte[] processPacket(SshConnection connection, byte[] packet) throws IOException {
		byte[] dataWithoutMac = macExtractorService.extractPacketWithoutMac(packet, connection.getClientToServerMac());
		byte[] decryptedPacket = packetDecryptionService.decryptPacket(connection.getClientToServerCipher(), dataWithoutMac);
		// TODO: Problem with multiple package on one read
		//		System.out.println("TYPE = " + decryptedPacket[5]);
		//		LoggerSupport l = new LoggerSupport();
		//		l.dumpByteArrayInHex(packet, "Encrypted");
		//		l.dumpByteArrayInHex(decryptedPacket, "Decrypted");
		//		int size = ByteConverterUtils.byteArrayToInt(decryptedPacket);
		//		int leftOver = packet.length - size - macExtractorService.numberOfMacBytes(connection.getClientToServerMac()) - 4;
		//		if (leftOver > 0) {
		//			byte[] leftOverBytes = Arrays.copyOfRange(packet, packet.length - leftOver, packet.length);
		//			connection.getConnection().getInputStream().unread(leftOverBytes);
		//			System.out.println("Wrong packet size " + leftOver);
		//		}
		validateMac(connection, packet, decryptedPacket);
		byte[] result = packetPayloadExtractor.extractResult(decryptedPacket);
		connection.incrementNumberOfReceivedPackages();
		return result;
	}

	private void validateMac(SshConnection connection, byte[] packet, byte[] decryptedPacket) throws IOException {
		byte[] mac = macExtractorService.extractMac(connection.getClientToServerMac(), packet, decryptedPacket);
		int size = ByteConverterUtils.byteArrayToInt(decryptedPacket);
		byte[] correctedData = Arrays.copyOfRange(decryptedPacket, 0, size + 4);
		System.out.println(PacketType.fromValue(decryptedPacket[5]));
		if (!isMacValid(connection, correctedData, mac)) {
			throw new RuntimeException("MAC is not valid");
		}
		System.out.println("VALID MAC");
	}

	private byte[] readRawPacket(SshConnection connection) throws IOException {
		InputStream inputStream = connection.getConnection().getInputStream();
		byte[] packet = binaryPacketReader.readPacket(connection.getClientToServerCipher(), inputStream);
		return packet;
	}

	private boolean isMacValid(SshConnection connection, byte[] decryptedPacket, byte[] mac) throws IOException {
		return packetMacValidator.isMacValid(connection.getClientToServerMac(), decryptedPacket, mac, connection.getNumberOfReceivedPackages());
	}

}
