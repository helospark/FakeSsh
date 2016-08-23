package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.DSAPrivateKey;

import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.signers.DSASigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshString;

/**
 * Used to sign data with the server's private key.
 * @author helospark
 */
@Component
public class DssSignatureService {
	private static final int SIGNATURE_S_INDEX = 1;
	private static final int SIGNATURE_R_INDEX = 0;
	private static final int DSA_SIGNATURE_LENGTH = 20;
	private DsaKeyProvider dsaKeyProvider;
	private DsaParametersConverter dsaParametersConverter;
	private DsaSignerFactory dsaSignerFactory;
	private Sha1HashFunction sha1HashFunction;

	@Autowired
	public DssSignatureService(DsaKeyProvider dsaKeyProvider, DsaParametersConverter dsaParametersConverter,
			DsaSignerFactory dsaSignerFactory, Sha1HashFunction sha1HashFunction) {
		this.dsaKeyProvider = dsaKeyProvider;
		this.dsaParametersConverter = dsaParametersConverter;
		this.dsaSignerFactory = dsaSignerFactory;
		this.sha1HashFunction = sha1HashFunction;
	}

	public byte[] sign(byte[] data) throws Exception {
		byte[] correctedHash = new SshString(sha1HashFunction.hash(data)).getData(); // TODO! Not sure why another hashing is needed
		DSASigner signer = createDsaSignerWithServerPrivateKey();
		return signData(correctedHash, signer);
	}

	private DSASigner createDsaSignerWithServerPrivateKey() {
		DSAPrivateKey dsaPrivateKey = dsaKeyProvider.providePrivateKey();
		DSAParameters params = dsaParametersConverter.convert(dsaPrivateKey);
		return dsaSignerFactory.createDsaSigner(dsaPrivateKey, params);
	}

	private byte[] signData(byte[] data, DSASigner signer) throws IOException {
		BigInteger[] signature = signer.generateSignature(data);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(normalizeToDsaByteLength(signature[SIGNATURE_R_INDEX].toByteArray(), DSA_SIGNATURE_LENGTH));
		byteStream.write(normalizeToDsaByteLength(signature[SIGNATURE_S_INDEX].toByteArray(), DSA_SIGNATURE_LENGTH));
		return byteStream.toByteArray();
	}

	private byte[] normalizeToDsaByteLength(byte[] bytes, int minimumBytes) throws IOException {
		byte[] result = bytes;
		if (bytes.length < minimumBytes) {
			result = padWithZeroBytes(bytes, minimumBytes);
		} else if (bytes.length > minimumBytes) {
			// BigInteger's toByteArray automatically appends a 0 byte when
			// with 160 bits representation it would be a negative number
			result = removeFirstBytes(bytes, minimumBytes);
		}
		return result;
	}

	private byte[] padWithZeroBytes(byte[] bytes, int minimumBytes) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		for (int i = bytes.length; i < minimumBytes; ++i) {
			byteStream.write(0x00);
		}
		byteStream.write(bytes);
		return byteStream.toByteArray();
	}

	private byte[] removeFirstBytes(byte[] bytes, int minimumBytes) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		for (int i = bytes.length - minimumBytes; i < bytes.length; ++i) {
			byteStream.write(bytes[i]);
		}
		return byteStream.toByteArray();
	}
}