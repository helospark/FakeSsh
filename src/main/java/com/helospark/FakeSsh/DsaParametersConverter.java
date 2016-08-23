package com.helospark.FakeSsh;

import java.security.interfaces.DSAPrivateKey;

import org.bouncycastle.crypto.params.DSAParameters;
import org.springframework.stereotype.Component;

/**
 * Converts {@link DSAPrivateKey} to {@link DSAParameters}.
 * @author helospark
 */
@Component
public class DsaParametersConverter {

	public DSAParameters convert(DSAPrivateKey privateKey) {
		return new DSAParameters(
				privateKey.getParams().getP(),
				privateKey.getParams().getQ(),
				privateKey.getParams().getG());
	}
}
