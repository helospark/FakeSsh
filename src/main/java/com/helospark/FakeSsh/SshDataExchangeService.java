package com.helospark.FakeSsh;

import static com.helospark.FakeSsh.ApplicationConstants.SSH_PROTOCOL_STRING_ENCODING_CHARSET;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SshDataExchangeService {
	private static final int MINIMUM_PADDING_SIZE = 4;
	private static final int PACKET_LENGTH_ALIGNMENT = 8;
	private static final int MAX_DATA_SIZE = 65000;
	private static final int PACKET_LENGTH_FIELD_SIZE = 4;
	private static final int PADDING_LENGTH_FIELD_SIZE = 1;
	private final int BEGINNING_OF_ARRAY = 0;
	private RandomNumberGenerator randomNumberGenerator;

	@Autowired
	public SshDataExchangeService(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public void sendString(SshConnection connection, String stringToSend) throws IOException {
		ReadWriteSocketConnection readWriteSocket = connection.getConnection();
		byte[] bytes = stringToSend.getBytes(SSH_PROTOCOL_STRING_ENCODING_CHARSET);
		readWriteSocket.getOutputStream().write(bytes);
	}

	public String readStringUntil(SshConnection connection, char delimiter) throws IOException {
		byte[] data = new byte[MAX_DATA_SIZE];
		InputStream inputStream = connection.getConnection().getInputStream();
		StringBuilder stringBuilder = new StringBuilder();
		int byteIndex = 0;
		while (true) {
			int b = inputStream.read();
			if (b == -1 || (byte) b == delimiter) {
				addData(data, stringBuilder, byteIndex);
				break;
			}
			data[byteIndex++] = (byte) b;
			if (byteIndex >= data.length) {
				addData(data, stringBuilder, byteIndex);
			}
		}
		return stringBuilder.toString();
	}

	private int addData(byte[] data, StringBuilder stringBuilder, int byteIndex) throws UnsupportedEncodingException {
		String readString = new String(data, BEGINNING_OF_ARRAY, byteIndex, SSH_PROTOCOL_STRING_ENCODING_CHARSET);
		stringBuilder.append(readString);
		return byteIndex;
	}

	public void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		byte[] encryptedPacket = createEncryptedPacket(connection, bytesToSend);
		byte[] macBytes = calculateMac(connection, bytesToSend);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(encryptedPacket);
		byteStream.write(macBytes);
		byte[] bytes = byteStream.toByteArray();

		connection.getConnection().getOutputStream().write(bytes);

		connection.incrementNumberOfSentPackages();
	}

	private byte[] createEncryptedPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		int fullSizeWithoutPadding = PACKET_LENGTH_FIELD_SIZE + PADDING_LENGTH_FIELD_SIZE + bytesToSend.length;
		byte paddingSize = calculatePaddingSize(fullSizeWithoutPadding);
		int packetLength = PADDING_LENGTH_FIELD_SIZE + bytesToSend.length + paddingSize;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByte(packetLength));
		byteStream.write(paddingSize);
		byteStream.write(bytesToSend);
		byteStream.write(randomNumberGenerator.generateRandomBytes(paddingSize));
		return encryptPacket(connection, byteStream.toByteArray());
	}

	private byte[] encryptPacket(SshConnection connection, byte[] byteArray) {
		Optional<SshCipher> optionalCipher = connection.getServerToClientCipher();
		return optionalCipher.map(cipher -> cipher.encrypt(byteArray)).orElse(byteArray);
	}

	private byte[] calculateMac(SshConnection connection, byte[] bytesToSend) throws IOException {
		Optional<SshMac> optionalMac = connection.getServerToClientMac();
		if (optionalMac.isPresent()) {
			return optionalMac.get().createMac(bytesToSend, connection.getNumberOfSentPackages());
		} else {
			return new byte[0];
		}
	}

	public byte[] readPacket(SshConnection connection) throws IOException {
		byte[] packetSizeAsByteArray = new byte[4];
		InputStream inputStream = connection.getConnection().getInputStream();
		inputStream.read(packetSizeAsByteArray);
		int availableBytes = inputStream.available();
		int packageSize = ByteConverterUtils.byteToInt(packetSizeAsByteArray);
		byte[] readBytes = new byte[packageSize];
		inputStream.read(readBytes);

		byte[] decryptedPacket = decryptPacket(connection, readBytes);

		byte paddingSize = decryptedPacket[0];
		byte[] result = new byte[decryptedPacket.length - paddingSize - 1];
		System.arraycopy(decryptedPacket, 1, result, 0, result.length);

		if (!isMacValid(connection, decryptedPacket)) {
			throw new RuntimeException("MAC is not valid");
		}
		connection.incrementNumberOfReceivedPackages();
		return result;
	}

	private byte[] decryptPacket(SshConnection connection, byte[] readBytes) {
		Optional<SshCipher> optionalCipher = connection.getClientToServerCipher();
		return optionalCipher.map(cipher -> cipher.decrypt(readBytes)).orElse(readBytes);
	}

	private boolean isMacValid(SshConnection connection, byte[] decryptedPacket) throws IOException {
		Optional<SshMac> optionalMac = connection.getClientToServerMac();
		if (optionalMac.isPresent()) {
			SshMac mac = optionalMac.get();
			int macLength = mac.getMacLength();
			byte[] macBytes = new byte[macLength];
			System.arraycopy(decryptedPacket, decryptedPacket.length - macLength - 1, macBytes, 0, macLength);
			byte[] packetBytes = new byte[decryptedPacket.length - macLength];
			System.arraycopy(decryptedPacket, 0, packetBytes, 0, decryptedPacket.length - macLength);
			return mac.checkMac(packetBytes, macBytes, connection.getNumberOfReceivedPackages());
		}
		return true;
	}

	private byte calculatePaddingSize(int sizeWithoutPadding) {
		byte paddingSize = 0;
		paddingSize = (byte) (PACKET_LENGTH_ALIGNMENT - sizeWithoutPadding % PACKET_LENGTH_ALIGNMENT);
		while (paddingSize < MINIMUM_PADDING_SIZE) {
			paddingSize += PACKET_LENGTH_ALIGNMENT;
		}
		return paddingSize;
	}
}
