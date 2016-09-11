package com.helospark.FakeSsh.hostkey.rsa.domain;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 * Implementation of {@link RSAPrivateKey} that stores the values in fields.
 * @author helospark
 */
public class RsaPrivateKeyImpl implements RSAPrivateKey {
	private BigInteger modulus;
	private BigInteger privateExponent;

	public RsaPrivateKeyImpl(BigInteger modulus, BigInteger privateExponent) {
		this.modulus = modulus;
		this.privateExponent = privateExponent;
	}

	@Override
	public String getAlgorithm() {
		return "RSA";
	}

	@Override
	public String getFormat() {
		return null;
	}

	@Override
	public byte[] getEncoded() {
		return null;
	}

	@Override
	public BigInteger getModulus() {
		return modulus;
	}

	@Override
	public BigInteger getPrivateExponent() {
		return privateExponent;
	}

}
