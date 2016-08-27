package com.helospark.FakeSsh.hostkey.rsa;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.stereotype.Component;

/**
 * Creates {@link DsaSigner} with DSA private key.
 * @author helospark
 */
@Component
public class RsaSignerFactory {

	public Signature createRsaSigner(RSAPrivateKey rsaPrivateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
		Signature signer = Signature.getInstance("SHA1WithRSA");
		signer.initSign(rsaPrivateKey);
		return signer;
	}
}
