package com.helospark.FakeSsh.io.read;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.util.LoggerSupport;

/**
 * Proxy around BinaryPacketReaderService to add logging.
 * @author helospark
 */
@Component("loggingBinaryPackerReaderServiceProxy")
public class LoggingBinaryPackerReaderServiceProxy implements BinaryPacketReaderService {
	private BinaryPacketReaderService proxiedBinaryPacketReaderService;
	private LoggerSupport loggerSupport;

	public LoggingBinaryPackerReaderServiceProxy(@Qualifier("binaryPacketReaderServiceCompressionProxy") BinaryPacketReaderService proxiedBinaryPacketReaderService, LoggerSupport loggerSupport) {
		this.proxiedBinaryPacketReaderService = proxiedBinaryPacketReaderService;
		this.loggerSupport = loggerSupport;
	}

	@Override
	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] readPacket = proxiedBinaryPacketReaderService.readPacket(connection);
		PacketType packetType = PacketType.fromValue(readPacket[0]);
		loggerSupport.logDebugString("Received packet " + packetType);
		loggerSupport.dumpByteArrayInHex(readPacket, "Decrypted packet payload");
		return readPacket;
	}

}
