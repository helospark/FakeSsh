package com.helospark.FakeSsh.io.write;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.compression.SshCompression;

/**
 * Proxy around BinaryPacketSenderService to compress the payload.
 * @author helospark
 */
@Component("sshBinaryPacketSenderServiceCompressionProxy")
public class SshBinaryPacketSenderServiceCompressionProxy implements BinaryPacketSenderService {
	private BinaryPacketSenderService proxiedBinaryPacketSender;

	@Autowired
	public SshBinaryPacketSenderServiceCompressionProxy(@Qualifier("sshBinaryPacketSenderService") BinaryPacketSenderService proxiedBinaryPacketSender) {
		this.proxiedBinaryPacketSender = proxiedBinaryPacketSender;
	}

	@Override
	public void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		byte[] compressedData = compressData(connection, bytesToSend);
		proxiedBinaryPacketSender.sendPacket(connection, compressedData);
	}

	private byte[] compressData(SshConnection connection, byte[] bytesToSend) {
		Optional<SshCompression> optionalCompression = connection.getServerToClientCompression();
		return optionalCompression
				.map(compression -> compression.compress(bytesToSend))
				.orElse(bytesToSend);
	}

}
