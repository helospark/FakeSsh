package com.helospark.FakeSsh.io.write;

import static com.helospark.FakeSsh.ApplicationConstants.PACKET_LENGTH_FIELD_SIZE;
import static com.helospark.FakeSsh.ApplicationConstants.PADDING_LENGTH_FIELD_SIZE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ByteConverterUtils;
import com.helospark.FakeSsh.RandomNumberGenerator;
import com.helospark.FakeSsh.SshCipher;

/**
 * Builds a non encrypted packet from the given bytes.
 * @author helospark
 */
@Component
public class NonEncryptedPacketBuilder {
	private PaddingSizeCalculatorService paddingSizeCalculatorService;
	private RandomNumberGenerator randomNumberGenerator;

	@Autowired
	public NonEncryptedPacketBuilder(PaddingSizeCalculatorService paddingSizeCalculatorService, RandomNumberGenerator randomNumberGenerator) {
		this.paddingSizeCalculatorService = paddingSizeCalculatorService;
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public byte[] createNonEncryptedPacket(byte[] bytesToSend, Optional<SshCipher> optionalCipher) throws IOException {
		int fullSizeWithoutPadding = PACKET_LENGTH_FIELD_SIZE + PADDING_LENGTH_FIELD_SIZE + bytesToSend.length;
		byte paddingSize = paddingSizeCalculatorService.calculatePaddingSize(fullSizeWithoutPadding, optionalCipher);
		int packetLength = PADDING_LENGTH_FIELD_SIZE + bytesToSend.length + paddingSize;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByteArray(packetLength));
		byteStream.write(paddingSize);
		byteStream.write(bytesToSend);
		byteStream.write(randomNumberGenerator.generateRandomBytes(paddingSize));
		return byteStream.toByteArray();
	}

}
