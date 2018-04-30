package com.helospark.FakeSsh.io.write;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.SshConnection;

/**
 * Sends a binary packet to the given connection.
 * @author helospark
 */
@Component
public class SshBinaryPacketSenderService implements BinaryPacketSenderService {
	private NonEncryptedPacketBuilder nonEncryptedPacketBuilder;
	private PacketEncryptionService packetEncryptionService;
	private HmacCalculator mmacCalculator;

	@Autowired
	public SshBinaryPacketSenderService(NonEncryptedPacketBuilder nonEncryptedPacketBuilder, PacketEncryptionService packetEncryptionService, HmacCalculator mmacCalculator) {
		this.nonEncryptedPacketBuilder = nonEncryptedPacketBuilder;
		this.packetEncryptionService = packetEncryptionService;
		this.mmacCalculator = mmacCalculator;
	}

	/* (non-Javadoc)
	 * @see com.helospark.FakeSsh.io.write.BinaryPacketSenderService#sendPacket(com.helospark.FakeSsh.SshConnection, byte[])
	 */
	@Override
	public void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		byte[] nonEncryptedPacket = nonEncryptedPacketBuilder.createNonEncryptedPacket(bytesToSend, connection.getServerToClientCipher());
		byte[] encryptedPacket = packetEncryptionService.encryptPacket(nonEncryptedPacket, connection.getServerToClientCipher());
		byte[] macBytes = mmacCalculator.calculateHmac(nonEncryptedPacket, connection.getServerToClientMac(), connection.getNumberOfSentPackages());
		byte[] bytes = composePacket(encryptedPacket, macBytes);
		sendBuiltPacket(connection, bytes);
		connection.incrementNumberOfSentPackages();
	}

	private byte[] composePacket(byte[] encryptedPacket, byte[] macBytes) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(encryptedPacket);
		byteStream.write(macBytes);
		byte[] bytes = byteStream.toByteArray();
		return bytes;
	}

	private void sendBuiltPacket(SshConnection connection, byte[] bytes) throws IOException {
		connection.getConnection().getOutputStream().write(bytes);
	}

}
