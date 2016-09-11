package com.helospark.FakeSsh.io;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.io.read.BinaryPacketReaderService;
import com.helospark.FakeSsh.io.read.StringReaderService;
import com.helospark.FakeSsh.io.write.BinaryPacketSenderService;
import com.helospark.FakeSsh.io.write.StringSenderService;

/**
 * Send and receive data from the client.
 * Delegates to respective services
 * @author helospark
 */
@Component
public class SshDataExchangeService {
	private BinaryPacketSenderService sshBinaryPacketSenderService;
	private StringSenderService stringSenderService;

	private BinaryPacketReaderService sshBinaryDataReaderService;
	private StringReaderService stringReaderService;

	@Autowired
	public SshDataExchangeService(@Qualifier("loggingBinaryPackerSenderServiceProxy") BinaryPacketSenderService sshBinaryPacketSenderService,
			@Qualifier("loggingBinaryPackerReaderServiceProxy") BinaryPacketReaderService sshBinaryDataReaderService,
			StringReaderService stringReaderService,
			StringSenderService stringSenderService) {
		this.sshBinaryPacketSenderService = sshBinaryPacketSenderService;
		this.stringSenderService = stringSenderService;
		this.sshBinaryDataReaderService = sshBinaryDataReaderService;
		this.stringReaderService = stringReaderService;
	}

	public void sendString(SshConnection connection, String stringToSend) throws IOException {
		stringSenderService.sendString(connection, stringToSend);
	}

	public void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		sshBinaryPacketSenderService.sendPacket(connection, bytesToSend);
	}

	public byte[] readPacket(SshConnection connection) throws IOException {
		return sshBinaryDataReaderService.readPacket(connection);
	}

	public void sendPacketWithoutAnyPayload(SshConnection connection, PacketType packetType) throws IOException {
		sendPacket(connection, new byte[] { packetType.getValue() });
	}

	public String readStringUntilDelimiter(SshConnection connection, char delimiter) throws IOException {
		return stringReaderService.readStringUntilDelimiter(connection, delimiter);
	}
}
