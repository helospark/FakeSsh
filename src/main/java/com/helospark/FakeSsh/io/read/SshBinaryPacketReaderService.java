package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.util.Arrays;
import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Reads a binary packet from the given connection.
 * Orchastrating and delegating to other services
 * @author helospark
 */
@Component
public class SshBinaryPacketReaderService implements BinaryPacketReaderService {
	private BinaryPacketReader binaryPacketReader;
	private PacketMacValidator packetMacValidator;
	private PacketPayloadExtractor packetPayloadExtractor;
	private MacExtractorService macExtractorService;

	@Autowired
	public SshBinaryPacketReaderService(BinaryPacketReader binaryPacketReader, PacketMacValidator packetMacValidator,
			PacketPayloadExtractor packetPayloadExtractor,
			MacExtractorService macExtractorService) {
		this.binaryPacketReader = binaryPacketReader;
		this.packetMacValidator = packetMacValidator;
		this.packetPayloadExtractor = packetPayloadExtractor;
		this.macExtractorService = macExtractorService;
	}

	@Override
	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] packet = readFullDecryptedPacket(connection);
		return processPacket(connection, packet);
	}

	private byte[] processPacket(SshConnection connection, byte[] packet) throws IOException {
		byte[] dataWithoutMac = macExtractorService.extractPacketWithoutMac(packet, connection.getClientToServerMac());
		validateMac(connection, packet, dataWithoutMac);
		byte[] result = packetPayloadExtractor.extractResult(dataWithoutMac);
		connection.incrementNumberOfReceivedPackages();
		return result;
	}

	private void validateMac(SshConnection connection, byte[] packet, byte[] decryptedPacket) throws IOException {
		byte[] mac = macExtractorService.extractMac(connection.getClientToServerMac(), packet, decryptedPacket);
		int size = ByteConverterUtils.byteArrayToInt(decryptedPacket);
		byte[] correctedData = Arrays.copyOfRange(decryptedPacket, 0, size + 4);
		if (!isMacValid(connection, correctedData, mac)) {
			throw new RuntimeException("MAC is not valid");
		}
	}

	private byte[] readFullDecryptedPacket(SshConnection connection) throws IOException {
		InputStream inputStream = connection.getConnection().getInputStream();
		byte[] packet = binaryPacketReader.readPacket(connection.getClientToServerCipher(), connection.getClientToServerMac(), inputStream);
		return packet;
	}

	private boolean isMacValid(SshConnection connection, byte[] decryptedPacket, byte[] mac) throws IOException {
		return packetMacValidator.isMacValid(connection.getClientToServerMac(), decryptedPacket, mac, connection.getNumberOfReceivedPackages());
	}

}
