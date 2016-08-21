package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

/**
 * Service to generate the keys using based on RFC4253.
 * The following method is used: HASH(K || H || identifier || session_id)
 * If the hash is not long enough it will be extended by concatenating additional
 * hashes.
 * @author helospark
 */
@Component
public class DiffieHellmanHashService {

	/**
	 * Generate the hash.
	 * @param connection to get the Key, hash, hanhFunction and session_id from.
	 * @param identifier the character concatenated
	 * @param hashSize minimum number of bytes to generate
	 * @return hashSize long generated hash
	 */
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
		SshHash sshHash = connection.getHashFunction();
		return sshHash.hash(byteArray);
	}
}
