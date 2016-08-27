package com.helospark.FakeSsh.io.read;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.hmac.SshMac;

/**
 * Check whether the received packet came with the correct mac bytes.
 * @author helospark
 */
@Component
public class PacketMacValidator {

	// TODO! Too many parameters
	public boolean isMacValid(Optional<SshMac> optionalMac, byte[] decryptedPacket, byte[] macBytes, int numberOfReceivedPackages) throws IOException {
		if (optionalMac.isPresent()) {
			SshMac mac = optionalMac.get();
			return mac.checkMac(decryptedPacket, macBytes, numberOfReceivedPackages);
		}
		return true;
	}
}
