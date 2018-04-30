package com.helospark.FakeSsh.io.write;

import static com.helospark.FakeSsh.ApplicationConstants.DEFAULT_PACKET_LENGTH_ALIGNMENT;
import static com.helospark.FakeSsh.ApplicationConstants.MINIMUM_PADDING_SIZE;

import java.util.Optional;

import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.cipher.SshCipher;

/**
 * Calculates the padding size for a packet.
 * @author helospark
 */
@Component
public class PaddingSizeCalculatorService {

	public byte calculatePaddingSize(int sizeWithoutPadding, Optional<SshCipher> optionalCipher) {
		byte paddingSize = 0;
		int packetAlignment = optionalCipher.map(cipher -> cipher.getBlockSize()).orElse(DEFAULT_PACKET_LENGTH_ALIGNMENT);
		paddingSize = (byte) (packetAlignment - sizeWithoutPadding % packetAlignment);
		while (paddingSize < MINIMUM_PADDING_SIZE) {
			paddingSize += packetAlignment;
		}
		return paddingSize;
	}
}
