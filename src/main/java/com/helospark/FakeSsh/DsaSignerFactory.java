package com.helospark.FakeSsh;

import java.security.interfaces.DSAPrivateKey;

import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.signers.DSASigner;
import org.springframework.stereotype.Component;

/**
 * Creates {@link DsaSigner} with DSA private key.
 * @author helospark
 */
@Component
public class DsaSignerFactory {

	public DSASigner createDsaSigner(DSAPrivateKey dsaPrivateKey, DSAParameters params) {
		DSASigner signer = new DSASigner();
		signer.init(true, new DSAPrivateKeyParameters(dsaPrivateKey.getX(), params));
		return signer;
	}
}
