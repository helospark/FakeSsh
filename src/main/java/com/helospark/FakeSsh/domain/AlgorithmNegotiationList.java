package com.helospark.FakeSsh.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.helospark.FakeSsh.ByteConverterUtils;
import com.helospark.FakeSsh.PacketType;

public class AlgorithmNegotiationList {
	private static final int COOKIE_SIZE = 16;
	public PacketType type;
	public byte[] cookie;
	public SshNamedList kexAlgorithms;
	public SshNamedList serverHostKeyAlgorithms;
	public SshNamedList encryptionAlgorithmsClientToServer;
	public SshNamedList encryptionAlgorithmsServerToClient;
	public SshNamedList macAlgorithmsClientToServer;
	public SshNamedList macAlgorithmsServerToClient;
	public SshNamedList compressionAlgorithmsClientToServer;
	public SshNamedList compressionAlgorithmsServerToClient;
	public SshNamedList languagesClientToServer;
	public SshNamedList languagesServerToClient;
	public byte firstKeyPacketFollow;
	public int reserved;

	public AlgorithmNegotiationList() {

	}

	public AlgorithmNegotiationList(byte[] kexMessage) throws IOException {
		deserialize(kexMessage);
	}

	public void deserialize(byte[] data) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		byte typeAsByte = (byte) byteArrayInputStream.read();
		type = PacketType.fromValue(typeAsByte);
		cookie = new byte[COOKIE_SIZE];
		byteArrayInputStream.read(cookie);
		kexAlgorithms = readNamedListFromStream(byteArrayInputStream);
		serverHostKeyAlgorithms = readNamedListFromStream(byteArrayInputStream);
		encryptionAlgorithmsClientToServer = readNamedListFromStream(byteArrayInputStream);
		encryptionAlgorithmsServerToClient = readNamedListFromStream(byteArrayInputStream);
		macAlgorithmsClientToServer = readNamedListFromStream(byteArrayInputStream);
		macAlgorithmsServerToClient = readNamedListFromStream(byteArrayInputStream);
		compressionAlgorithmsClientToServer = readNamedListFromStream(byteArrayInputStream);
		compressionAlgorithmsServerToClient = readNamedListFromStream(byteArrayInputStream);
		languagesClientToServer = readNamedListFromStream(byteArrayInputStream);
		languagesServerToClient = readNamedListFromStream(byteArrayInputStream);

		firstKeyPacketFollow = (byte) byteArrayInputStream.read();

		byte[] reservedAsByte = new byte[4];
		byteArrayInputStream.read(reservedAsByte);
		reserved = ByteConverterUtils.byteArrayToInt(reservedAsByte);
	}

	private SshNamedList readNamedListFromStream(ByteArrayInputStream byteArrayInputStream) throws IOException {
		return new SshNamedList(new SshString(byteArrayInputStream).getAsUtf8String());
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
