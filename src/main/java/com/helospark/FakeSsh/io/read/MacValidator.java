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
public class MacValidator {

	/**
	 * Checks whether the received packet came with the correct mac bytes.
	 * @param optionalMac the used MAC algorithm
	 * @param decryptedPacket the decrypted packet (inclusing padding and size)
	 * @param macBytes read MAC to check against
	 * @param numberOfReceivedPackages counter of the number of received packeges
	 * @return true if the MAC is valid
	 * @throws IOException
	 */
	// TODO! Too many parameters
	public boolean isMacValid(Optional<SshMac> optionalMac, byte[] decryptedPacket, byte[] macBytes, int numberOfReceivedPackages) throws IOException {
		if (optionalMac.isPresent()) {
			SshMac mac = optionalMac.get();
			return mac.checkMac(decryptedPacket, macBytes, numberOfReceivedPackages);
		}
		return true;
	}
}
