package com.helospark.FakeSsh.io.write;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshMac;

/**
 * Calculates the HMAC for the given packet.
 * @author helospark
 */
@Component
public class HmacCalculator {
	public byte[] calculateHmac(byte[] bytesToSend, Optional<SshMac> optionalMac, int numberOfPacketSend) throws IOException {
		if (optionalMac.isPresent()) {
			return optionalMac.get().createMac(bytesToSend, numberOfPacketSend);
		} else {
			return new byte[0];
		}
	}
}
