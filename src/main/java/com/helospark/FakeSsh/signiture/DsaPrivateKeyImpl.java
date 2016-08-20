package com.helospark.FakeSsh.signiture;

import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;

public class DsaPrivateKeyImpl implements DSAPrivateKey {
	private DSAParams dsaParams;
	private BigInteger privateKey;

	public DsaPrivateKeyImpl(DSAParams dsaParams, BigInteger privateKey) {
		this.dsaParams = dsaParams;
		this.privateKey = privateKey;
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
	public String getAlgorithm() {
		return "DSA";
	}

	@Override
	public DSAParams getParams() {
		return dsaParams;
	}

	@Override
	public BigInteger getX() {
		return privateKey;
	}

}
