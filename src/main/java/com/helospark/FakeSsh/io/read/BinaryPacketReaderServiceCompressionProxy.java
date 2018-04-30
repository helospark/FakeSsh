package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.util.Optional;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.compression.SshCompression;
import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;
import com.helospark.lightdi.annotation.Qualifier;

@Component("binaryPacketReaderServiceCompressionProxy")
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
