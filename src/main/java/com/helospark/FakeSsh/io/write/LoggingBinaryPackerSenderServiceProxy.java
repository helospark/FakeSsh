package com.helospark.FakeSsh.io.write;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.util.LoggerSupport;

@Component("loggingBinaryPackerSenderServiceProxy")
public class LoggingBinaryPackerSenderServiceProxy implements BinaryPacketSenderService {
	private BinaryPacketSenderService proxiedBinaryPacketSenderService;
	private LoggerSupport loggerSupport;

	@Autowired
	public LoggingBinaryPackerSenderServiceProxy(@Qualifier("sshBinaryPacketSenderServiceCompressionProxy") BinaryPacketSenderService proxiedBinaryPacketSenderService, LoggerSupport loggerSupport) {
		this.proxiedBinaryPacketSenderService = proxiedBinaryPacketSenderService;
		this.loggerSupport = loggerSupport;
	}

	@Override
	public void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		PacketType packetType = PacketType.fromValue(bytesToSend[0]);
		loggerSupport.logDebugString("Sending packet " + packetType);
		loggerSupport.dumpByteArrayInHex(bytesToSend, "Nonencrypted packet payload");
		proxiedBinaryPacketSenderService.sendPacket(connection, bytesToSend);
	}

}
