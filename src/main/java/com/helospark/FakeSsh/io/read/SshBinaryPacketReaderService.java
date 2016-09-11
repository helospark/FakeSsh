package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;

/**
 * Reads a binary packet from the given connection.
 * Orchastrating and delegating to other services
 * @author helospark
 */
@Component
public class SshBinaryPacketReaderService implements BinaryPacketReaderService {
	private DecryptingPacketReader decryptingPacketReader;
	private MacValidator macValidator;
	private PacketPayloadExtractor packetPayloadExtractor;
	private MacReader macReader;

	@Autowired
	public SshBinaryPacketReaderService(DecryptingPacketReader decryptingPacketReader, MacValidator macValidator,
			PacketPayloadExtractor packetPayloadExtractor,
			MacReader macReader) {
		this.decryptingPacketReader = decryptingPacketReader;
		this.macValidator = macValidator;
		this.packetPayloadExtractor = packetPayloadExtractor;
		this.macReader = macReader;
	}

	@Override
	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] packet = readDecryptedPacket(connection);
		byte[] mac = readMac(connection);
		assertMacValid(connection, packet, mac);
		byte[] result = packetPayloadExtractor.extractResult(packet);
		connection.incrementNumberOfReceivedPackages();
		return result;
	}

	private void assertMacValid(SshConnection connection, byte[] decryptedPacket, byte[] mac) throws IOException {
		if (!isMacValid(connection, decryptedPacket, mac)) {
			throw new RuntimeException("MAC is not valid");
		}
	}

	private byte[] readDecryptedPacket(SshConnection connection) throws IOException {
		InputStream inputStream = connection.getConnection().getInputStream();
		return decryptingPacketReader.readPacket(inputStream, connection.getClientToServerCipher());
	}

	private byte[] readMac(SshConnection connection) throws IOException {
		InputStream inputStream = connection.getConnection().getInputStream();
		return macReader.readMac(inputStream, connection.getClientToServerMac());
	}

	private boolean isMacValid(SshConnection connection, byte[] decryptedPacket, byte[] mac) throws IOException {
		return macValidator.isMacValid(connection.getClientToServerMac(), decryptedPacket, mac, connection.getNumberOfReceivedPackages());
	}

}
