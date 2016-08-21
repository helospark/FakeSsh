package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

@Component
public class DiffieHellmanHashService {

	public byte[] hash(SshConnection connection, char identifier, int hashSize) throws IOException {
		byte[] hash = initialHash(connection, identifier);

		if (hash.length < hashSize) {
			hash = expandHash(connection, hashSize, hash);
		}
		byte[] result = new byte[hashSize];
		System.arraycopy(hash, 0, result, 0, hashSize);
		return result;
	}

	private byte[] expandHash(SshConnection connection, int minimumHashSize, byte[] hash) throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream outputHash = new ByteArrayOutputStream();
		outputHash.write(hash);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(connection.getKey().serialize());
		byteStream.write(connection.getHash());
		byte[] previousHash = hash;
		SshHash sshHash = connection.getHashFunction();
		while (outputHash.size() < minimumHashSize) {
			byteStream.write(previousHash);
			byte[] newHash = sshHash.hash(byteStream.toByteArray());
			outputHash.write(newHash);
			previousHash = newHash;
		}
		return outputHash.toByteArray();
	}

	private byte[] initialHash(SshConnection connection, char identifier) throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(connection.getKey().serialize());
		byteStream.write(connection.getHash());
		byteStream.write((byte) identifier);
		byteStream.write(connection.getSessionId());
		byte[] byteArray = byteStream.toByteArray();
		System.out.printf("[[[[[[[[[[[[[[[[[[[[[%c]]]]]]]]]]]]]]]]]]]]]", identifier);
		for (int i = 0; i < byteArray.length; ++i) {
			System.out.printf("%02x", byteArray[i]);
		}
		System.out.println();
		SshHash sshHash = connection.getHashFunction();
		return sshHash.hash(byteArray);
	}
}
