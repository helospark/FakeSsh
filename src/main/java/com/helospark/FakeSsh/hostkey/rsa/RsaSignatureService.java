package com.helospark.FakeSsh.hostkey.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Used to sign data with the server's private key.
 * @author helospark
 */
@Component
public class RsaSignatureService {
	private RsaSignerFactory rsaSignerFactory;

	@Autowired
	public RsaSignatureService(RsaSignerFactory rsaSignerFactory) {
		this.rsaSignerFactory = rsaSignerFactory;
	}

	public byte[] sign(byte[] data, RSAPrivateKey rsaPrivateKey) throws Exception {
		Signature signer = rsaSignerFactory.createRsaSigner(rsaPrivateKey);
		return signData(signer, data);
	}

	private byte[] signData(Signature signer, byte[] data) throws IOException, SignatureException {
		signer.update(data);
		byte[] signedData = signer.sign();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(signedData);
		return byteStream.toByteArray();
	}

}