package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ConnectionClosedException;
import com.helospark.FakeSsh.SshConnection;

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
	private InputStreamDataAvailableWaiter inputStreamDataAvailableWaiter;

	@Autowired
	public SshBinaryDataReaderService(BinaryPacketReader binaryPacketReader, PacketMacValidator packetMacValidator,
			PacketDecryptionService packetDecryptionService, PacketPayloadExtractor packetPayloadExtractor,
			MacExtractorService macExtractorService, InputStreamDataAvailableWaiter inputStreamDataAvailableWaiter) {
		this.binaryPacketReader = binaryPacketReader;
		this.packetMacValidator = packetMacValidator;
		this.packetDecryptionService = packetDecryptionService;
		this.packetPayloadExtractor = packetPayloadExtractor;
		this.macExtractorService = macExtractorService;
		this.inputStreamDataAvailableWaiter = inputStreamDataAvailableWaiter;
	}

	/**
	 * Reads a packet from the given connection.
	 * @param connection to read packet from
	 * @return decrypted bytes for the packet
	 * @throws ConnectionClosedException if connection was closed while waiting for packets
	 */
	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] packet = readRawPacket(connection);
		byte[] dataWithoutMac = macExtractorService.extractPacketWithoutMac(packet, connection.getClientToServerMac());
		byte[] decryptedPacket = packetDecryptionService.decryptPacket(connection.getClientToServerCipher(), dataWithoutMac);
		validateMac(connection, packet, decryptedPacket);
		byte[] result = packetPayloadExtractor.extractResult(decryptedPacket);
		connection.incrementNumberOfReceivedPackages();
		return result;
	}

	private void validateMac(SshConnection connection, byte[] packet, byte[] decryptedPacket) throws IOException {
		byte[] mac = macExtractorService.extractMac(connection.getClientToServerMac(), packet);
		if (!isMacValid(connection, decryptedPacket, mac)) {
			throw new RuntimeException("MAC is not valid");
		}
	}

	private byte[] readRawPacket(SshConnection connection) throws IOException {
		InputStream inputStream = connection.getConnection().getInputStream();
		//		waitForDataOrTimeout(connection, inputStream);
		byte[] packet = binaryPacketReader.readPacket(connection.getClientToServerCipher(), inputStream);
		return packet;
	}

	private boolean isMacValid(SshConnection connection, byte[] decryptedPacket, byte[] mac) throws IOException {
		return packetMacValidator.isMacValid(connection.getClientToServerMac(), decryptedPacket, mac, connection.getNumberOfReceivedPackages());
	}

	private void waitForDataOrTimeout(SshConnection connection, InputStream inputStream) throws IOException {
		boolean timeoutOccurred = inputStreamDataAvailableWaiter.waitForData(inputStream);
		if (timeoutOccurred) {
			connection.setConnectionClosed(true);
			throw new ConnectionClosedException();
		}
	}

}
