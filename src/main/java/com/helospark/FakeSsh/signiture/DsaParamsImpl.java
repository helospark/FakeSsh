package com.helospark.FakeSsh.signiture;

import java.math.BigInteger;
import java.security.interfaces.DSAParams;

public class DsaParamsImpl implements DSAParams {
	private BigInteger p, q, g;

	public DsaParamsImpl(BigInteger p, BigInteger q, BigInteger g) {
		this.p = p;
		this.q = q;
		this.g = g;
	}

	@Override
	public BigInteger getP() {
		return p;
	}

	@Override
	public BigInteger getQ() {
		return q;
	}

	@Override
	public BigInteger getG() {
		return g;
	}

}
