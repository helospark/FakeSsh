package com.helospark.FakeSsh.signiture;

import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;

/**
 * Implemented DSAPrivateKey that store the parameter in fields.
 * @author helospark
 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dsaParams == null) ? 0 : dsaParams.hashCode());
		result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DsaPrivateKeyImpl other = (DsaPrivateKeyImpl) obj;
		if (dsaParams == null) {
			if (other.dsaParams != null)
				return false;
		} else if (!dsaParams.equals(other.dsaParams))
			return false;
		if (privateKey == null) {
			if (other.privateKey != null)
				return false;
		} else if (!privateKey.equals(other.privateKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DsaPrivateKeyImpl [dsaParams=" + dsaParams + ", privateKey=" + privateKey + "]";
	}

}
