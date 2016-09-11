package com.helospark.FakeSsh.kex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.domain.DhGexInit;
import com.helospark.FakeSsh.domain.DhGexResponse;
import com.helospark.FakeSsh.domain.DhKeySize;
import com.helospark.FakeSsh.domain.DiffieHellmanKey;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.util.ByteConverterUtils;

/**
 * Calculates 'H' parameter for the key exchange.
 * H is the hash, the first calculated H is the session identifier
 * @author helospark
 */
@Component
public class SshExchangeHashCalculator {

	public byte[] calculateHash(SshConnection connection, DhKeySize dhKeySize, DhGexResponse dhGexResponse, DhGexInit dhGexInit, DiffieHellmanKey diffieHellmanKey, SshString publicKey) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(connection.getRemoteIdentificationMessage().serialize());
		byteStream.write(connection.getLocaleIdentificationMessage().serialize());
		byteStream.write(connection.getRemoteKexMessage().serialize());
		byteStream.write(connection.getLocaleKexMessage().serialize());
		byteStream.write(publicKey.serialize());
		byteStream.write(ByteConverterUtils.intToByteArray(dhKeySize.getMinimumLength()));
		byteStream.write(ByteConverterUtils.intToByteArray(dhKeySize.getPreferredLength()));
		byteStream.write(ByteConverterUtils.intToByteArray(dhKeySize.getMaximumLength()));
		byteStream.write(dhGexResponse.getSafePrimeP().serialize());
		byteStream.write(dhGexResponse.getGeneratorG().serialize());
		byteStream.write(dhGexInit.getE().serialize());
		byteStream.write(diffieHellmanKey.getF().serialize());
		byteStream.write(diffieHellmanKey.getK().serialize());
		byte[] bytesToHash = byteStream.toByteArray();
		byte[] firstHash = connection.getHashFunction().hash(bytesToHash);
		connection.setSessionId(firstHash);
		return firstHash;
	}
}
