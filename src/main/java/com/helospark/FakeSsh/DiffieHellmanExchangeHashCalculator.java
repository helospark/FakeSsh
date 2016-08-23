package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.DhGexInit;
import com.helospark.FakeSsh.domain.DhGexResponse;
import com.helospark.FakeSsh.domain.DhKeySize;
import com.helospark.FakeSsh.domain.DiffieHellmanKey;

@Component
public class DiffieHellmanExchangeHashCalculator {
	private DsaKeyProvider dsaKeyProvider;
	private Sha1HashFunction hashFunction;

	@Autowired
	public DiffieHellmanExchangeHashCalculator(DsaKeyProvider dsaKeyProvider, Sha1HashFunction hashFunction) {
		this.dsaKeyProvider = dsaKeyProvider;
		this.hashFunction = hashFunction;
	}

	public byte[] calculateHash(SshConnection connection, DhKeySize dhKeySize, DhGexResponse dhGexResponse, DhGexInit dhGexInit, DiffieHellmanKey diffieHellmanKey) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(connection.getRemoteIdentificationMessage().serialize());
		byteStream.write(connection.getLocaleIdentificationMessage().serialize());
		byteStream.write(connection.getRemoteKexMessage().serialize());
		byteStream.write(connection.getLocaleKexMessage().serialize());
		byteStream.write(dsaKeyProvider.providePublicKey().serialize());
		byteStream.write(ByteConverterUtils.intToByteArray(dhKeySize.getMinimumLength()));
		byteStream.write(ByteConverterUtils.intToByteArray(dhKeySize.getPreferredLength()));
		byteStream.write(ByteConverterUtils.intToByteArray(dhKeySize.getMaximumLength()));
		byteStream.write(dhGexResponse.getSafePrimeP().serialize());
		byteStream.write(dhGexResponse.getGeneratorG().serialize());
		byteStream.write(dhGexInit.getE().serialize());
		byteStream.write(diffieHellmanKey.getF().serialize());
		byteStream.write(diffieHellmanKey.getK().serialize());
		byte[] bytesToHash = byteStream.toByteArray();
		byte[] firstHash = hashFunction.hash(bytesToHash);
		connection.setSessionId(firstHash);
		return firstHash;
	}
}
