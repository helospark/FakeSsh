package com.helospark.FakeSsh.hostkey.dsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.DSAPrivateKey;

import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.signers.DSASigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.hostkey.BigIntegerBitLengthNormalizer;

/**
 * Used to sign data with the server's private key.
 * @author helospark
 */
@Component
public class DssSignatureService {
	private static final int SIGNATURE_S_INDEX = 1;
	private static final int SIGNATURE_R_INDEX = 0;
	private static final int DSA_SIGNATURE_LENGTH = 20;
	private DsaParametersConverter dsaParametersConverter;
	private DsaSignerFactory dsaSignerFactory;
	private Sha1HashFunction sha1HashFunction;
	private BigIntegerBitLengthNormalizer bigIntegerBitLengthNormalizer;

	@Autowired
	public DssSignatureService(DsaParametersConverter dsaParametersConverter,
			DsaSignerFactory dsaSignerFactory, Sha1HashFunction sha1HashFunction,
			BigIntegerBitLengthNormalizer bigIntegerBitLengthNormalizer) {
		this.dsaParametersConverter = dsaParametersConverter;
		this.dsaSignerFactory = dsaSignerFactory;
		this.sha1HashFunction = sha1HashFunction;
		this.bigIntegerBitLengthNormalizer = bigIntegerBitLengthNormalizer;
	}

	public byte[] sign(byte[] data, DSAPrivateKey dsaPrivateKey) throws Exception {
		byte[] hashedData = new SshString(sha1HashFunction.hash(data)).getData(); // TODO! Not sure why another hashing is needed
		DSASigner signer = createDsaSignerWithServerPrivateKey(dsaPrivateKey);
		return signData(signer, hashedData);
	}

	private DSASigner createDsaSignerWithServerPrivateKey(DSAPrivateKey dsaPrivateKey) {
		DSAParameters params = dsaParametersConverter.convert(dsaPrivateKey);
		return dsaSignerFactory.createDsaSigner(dsaPrivateKey, params);
	}

	private byte[] signData(DSASigner signer, byte[] data) throws IOException {
		BigInteger[] signature = signer.generateSignature(data);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(bigIntegerBitLengthNormalizer.normalizeToLength(signature[SIGNATURE_R_INDEX].toByteArray(), DSA_SIGNATURE_LENGTH));
		byteStream.write(bigIntegerBitLengthNormalizer.normalizeToLength(signature[SIGNATURE_S_INDEX].toByteArray(), DSA_SIGNATURE_LENGTH));
		return byteStream.toByteArray();
	}

}