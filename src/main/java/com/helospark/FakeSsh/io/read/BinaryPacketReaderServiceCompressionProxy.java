package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.compression.SshCompression;

@Component
@Qualifier("binaryPacketReaderServiceCompressionProxy")
public class BinaryPacketReaderServiceCompressionProxy implements BinaryPacketReaderService {
	private BinaryPacketReaderService proxiedBinaryPacketReader;

	@Autowired
	public BinaryPacketReaderServiceCompressionProxy(@Qualifier("sshBinaryPacketReaderService") BinaryPacketReaderService proxiedBinaryPacketReader) {
		this.proxiedBinaryPacketReader = proxiedBinaryPacketReader;
	}

	@Override
	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] readBytes = proxiedBinaryPacketReader.readPacket(connection);
		return uncompressBytes(connection, readBytes);
	}

	private byte[] uncompressBytes(SshConnection connection, byte[] readBytes) {
		Optional<SshCompression> optionalCompression = connection.getClientToServerCompression();
		return optionalCompression
				.map(compression -> compression.decompress(readBytes))
				.orElse(readBytes);
	}
}
