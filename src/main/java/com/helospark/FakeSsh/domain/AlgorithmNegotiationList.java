package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.helospark.FakeSsh.ByteConverterUtils;
import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshStringReader;

public class AlgorithmNegotiationList {
	private static final int COOKIE_SIZE = 16;
	public PacketType type;
	public byte[] cookie;
	public AlgorithmNegotiationNameList kexAlgorithms;
	public AlgorithmNegotiationNameList serverHostKeyAlgorithms;
	public AlgorithmNegotiationNameList encryptionAlgorithmsClientToServer;
	public AlgorithmNegotiationNameList encryptionAlgorithmsServerToClient;
	public AlgorithmNegotiationNameList macAlgorithmsClientToServer;
	public AlgorithmNegotiationNameList macAlgorithmsServerToClient;
	public AlgorithmNegotiationNameList compressionAlgorithmsClientToServer;
	public AlgorithmNegotiationNameList compressionAlgorithmsServerToClient;
	public AlgorithmNegotiationNameList languagesClientToServer;
	public AlgorithmNegotiationNameList languagesServerToClient;
	public byte firstKeyPacketFollow;
	public int reserved;

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		byte typeAsByte = (byte) byteArrayInputStream.read();
		type = PacketType.fromValue(typeAsByte);
		cookie = new byte[COOKIE_SIZE];
		byteArrayInputStream.read(cookie);
		String tmp;
		tmp = SshStringReader.readString(byteArrayInputStream);
		kexAlgorithms = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		serverHostKeyAlgorithms = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		encryptionAlgorithmsClientToServer = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		encryptionAlgorithmsServerToClient = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		macAlgorithmsClientToServer = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		macAlgorithmsServerToClient = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		compressionAlgorithmsClientToServer = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		compressionAlgorithmsServerToClient = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		languagesClientToServer = new AlgorithmNegotiationNameList(tmp);

		tmp = SshStringReader.readString(byteArrayInputStream);
		languagesServerToClient = new AlgorithmNegotiationNameList(tmp);

		firstKeyPacketFollow = (byte) byteArrayInputStream.read();

		byte[] reservedAsByte = new byte[4];
		byteArrayInputStream.read(reservedAsByte);
		reserved = ByteConverterUtils.byteToInt(reservedAsByte);
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(cookie);
		byteStream.write(kexAlgorithms.serialize());
		byteStream.write(serverHostKeyAlgorithms.serialize());
		byteStream.write(encryptionAlgorithmsClientToServer.serialize());
		byteStream.write(encryptionAlgorithmsServerToClient.serialize());
		byteStream.write(macAlgorithmsClientToServer.serialize());
		byteStream.write(macAlgorithmsServerToClient.serialize());
		byteStream.write(compressionAlgorithmsClientToServer.serialize());
		byteStream.write(compressionAlgorithmsServerToClient.serialize());
		byteStream.write(languagesClientToServer.serialize());
		byteStream.write(languagesServerToClient.serialize());
		byteStream.write(firstKeyPacketFollow);
		byteStream.write(ByteBuffer.allocate(4).putInt(reserved).array());
		return byteStream.toByteArray();
	}
}
