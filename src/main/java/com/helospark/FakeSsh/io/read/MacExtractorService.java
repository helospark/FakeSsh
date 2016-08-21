package com.helospark.FakeSsh.io.read;

import java.util.Optional;

import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshMac;

/**
 * Separates the message and the HMAC.
 * @author helospark
 */
@Component
public class MacExtractorService {

	public byte[] extractPacketWithoutMac(byte[] packet, Optional<SshMac> optionalMac) {
		return Arrays.copyOfRange(packet, 0, packet.length - numberOfMacBytes(optionalMac));
	}

	public byte[] extractMac(Optional<SshMac> optionalMac, byte[] packet) {
		if (optionalMac.isPresent()) {
			int macLength = numberOfMacBytes(optionalMac);
			byte[] macBytes = new byte[macLength];
			System.arraycopy(packet, packet.length - macLength, macBytes, 0, macLength);
			return macBytes;
		}
		return new byte[0];
	}

	private int numberOfMacBytes(Optional<SshMac> optionalMac) {
		return optionalMac.map(mac -> mac.getMacLength())
				.orElse(0);
	}
}
