package com.helospark.FakeSsh.io.read;

import java.util.Optional;

import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.hmac.SshMac;
import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Separates the message and the HMAC.
 * @author helospark
 */
@Component
public class MacExtractorService {

	public byte[] extractPacketWithoutMac(byte[] packet, Optional<SshMac> optionalMac) {
		return Arrays.copyOfRange(packet, 0, packet.length - numberOfMacBytes(optionalMac));
	}

	public byte[] extractMac(Optional<SshMac> optionalMac, byte[] packet, byte[] decryptedPacket) {
		if (optionalMac.isPresent()) {
			int macLength = numberOfMacBytes(optionalMac);
			int size = ByteConverterUtils.byteArrayToInt(decryptedPacket) + 4;
			byte[] macBytes = new byte[macLength];
			System.arraycopy(packet, size, macBytes, 0, macLength);
			return macBytes;
		}
		return new byte[0];
	}

	public int numberOfMacBytes(Optional<SshMac> optionalMac) {
		return optionalMac.map(mac -> mac.getMacLength())
				.orElse(0);
	}
}
