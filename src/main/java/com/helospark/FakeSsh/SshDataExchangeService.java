package com.helospark.FakeSsh;

import static com.helospark.FakeSsh.ApplicationConstants.SSH_PROTOCOL_STRING_ENCODING_CHARSET;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.bouncycastle.util.Arrays;
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
		byte[] nonEncryptedPacket = createNonncryptedPacket(connection, bytesToSend);
		byte[] encryptedPacket = encryptPacket(connection, nonEncryptedPacket);
		byte[] macBytes = calculateMac(connection, nonEncryptedPacket);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(encryptedPacket);
		byteStream.write(macBytes);
		byte[] bytes = byteStream.toByteArray();

		connection.getConnection().getOutputStream().write(bytes);

		connection.incrementNumberOfSentPackages();
	}

	private byte[] createNonncryptedPacket(SshConnection connection, byte[] bytesToSend) throws IOException {
		int fullSizeWithoutPadding = PACKET_LENGTH_FIELD_SIZE + PADDING_LENGTH_FIELD_SIZE + bytesToSend.length;
		byte paddingSize = calculatePaddingSize(fullSizeWithoutPadding);
		int packetLength = PADDING_LENGTH_FIELD_SIZE + bytesToSend.length + paddingSize;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(ByteConverterUtils.intToByte(packetLength));
		byteStream.write(paddingSize);
		byteStream.write(bytesToSend);
		byteStream.write(randomNumberGenerator.generateRandomBytes(paddingSize));
		return byteStream.toByteArray();
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
		byte[] packet = new byte[65000];
		InputStream inputStream = connection.getConnection().getInputStream();
		while (inputStream.available() <= 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return new byte[0];
			}
		}
		int readBytes = inputStream.read(packet);
		byte[] decryptedPacket = decryptPacket(connection, packet, readBytes);
		byte[] macBytes = extractMac(connection, packet, readBytes);
		if (!isMacValid(connection, decryptedPacket, macBytes)) {
			throw new RuntimeException("MAC is not valid");
		}

		ByteArrayInputStream inputByteStream = new ByteArrayInputStream(decryptedPacket);
		byte[] packetSizeAsByteArray = readBytes(inputByteStream, 4);
		int packageSize = ByteConverterUtils.byteToInt(packetSizeAsByteArray);
		byte paddingSize = (byte) inputByteStream.read();
		byte[] result = readBytes(inputByteStream, packageSize - paddingSize - 1);
		connection.incrementNumberOfReceivedPackages();
		return result;
	}

	private byte[] extractMac(SshConnection connection, byte[] packet, int readBytes) {
		Optional<SshMac> optionalMac = connection.getClientToServerMac();
		if (optionalMac.isPresent()) {
			int macLength = optionalMac.get().getMacLength();
			byte[] macBytes = new byte[macLength];
			System.arraycopy(packet, readBytes - macLength, macBytes, 0, macLength);
			return macBytes;
		}
		return new byte[0];
	}

	private byte[] readBytes(InputStream inputStream, int size) throws IOException {
		byte[] dataRead = new byte[size];
		inputStream.read(dataRead);
		return dataRead;
	}

	private byte[] decryptPacket(SshConnection connection, byte[] bytes, int numberOfReadBytes) {
		Optional<SshCipher> optionalCipher = connection.getClientToServerCipher();
		byte[] subArray = Arrays.copyOfRange(bytes, 0, numberOfReadBytes - numberOfMacBytes(connection));
		return optionalCipher.map(cipher -> cipher.decrypt(subArray)).orElse(subArray);
	}

	private int numberOfMacBytes(SshConnection connection) {
		return connection.getClientToServerMac()
				.map(mac -> mac.getMacLength())
				.orElse(0);
	}

	private boolean isMacValid(SshConnection connection, byte[] decryptedPacket, byte[] macBytes) throws IOException {
		Optional<SshMac> optionalMac = connection.getClientToServerMac();
		if (optionalMac.isPresent()) {
			SshMac mac = optionalMac.get();
			return mac.checkMac(decryptedPacket, macBytes, connection.getNumberOfReceivedPackages());
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
