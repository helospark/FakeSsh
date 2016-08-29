package com.helospark.FakeSsh.io;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.io.read.SshBinaryDataReaderService;
import com.helospark.FakeSsh.io.read.StringReaderService;
import com.helospark.FakeSsh.io.write.SshBinaryPacketSenderService;
import com.helospark.FakeSsh.io.write.StringSenderService;

/**
 * Send and receive data from the client.
 * Delegates to respective services
 * @author helospark
 */
@Component
public class SshDataExchangeService {
	private SshBinaryPacketSenderService sshBinaryPacketSenderService;
	private StringSenderService stringSenderService;

	private SshBinaryDataReaderService sshBinaryDataReaderService;
	private StringReaderService stringReaderService;

	@Autowired
	public SshDataExchangeService(SshBinaryPacketSenderService sshBinaryPacketSenderService, StringSenderService stringSenderService, SshBinaryDataReaderService sshBinaryDataReaderService, StringReaderService stringReaderService) {
		this.sshBinaryPacketSenderService = sshBinaryPacketSenderService;
		this.stringSenderService = stringSenderService;
		this.sshBinaryDataReaderService = sshBinaryDataReaderService;
		this.stringReaderService = stringReaderService;
	}

	public void sendString(SshConnection connection, String stringToSend) throws IOException {
		stringSenderService.sendString(connection, stringToSend);
	}

	public void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		System.out.println(PacketType.fromValue(bytesToSend[0]));
		sshBinaryPacketSenderService.sendPacket(connection, bytesToSend);
	}

	public byte[] readPacket(SshConnection connection) throws IOException {
		return sshBinaryDataReaderService.readPacket(connection);
	}

	public void sendPacket(SshConnection connection, PacketType packetType) throws IOException {
		sendPacket(connection, new byte[] { packetType.getValue() });
	}

	public String readStringUntilDelimiter(SshConnection connection, char delimiter) throws IOException {
		return stringReaderService.readStringUntilDelimiter(connection, delimiter);
	}
}
