package com.helospark.FakeSsh.hostkey.dsa.domain;

import java.math.BigInteger;
import java.security.interfaces.DSAParams;

/**
 * Implemented DSAParams that store the parameters in fields.
 * @author helospark
 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((g == null) ? 0 : g.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((q == null) ? 0 : q.hashCode());
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
		DsaParamsImpl other = (DsaParamsImpl) obj;
		if (g == null) {
			if (other.g != null)
				return false;
		} else if (!g.equals(other.g))
			return false;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DsaParamsImpl [p=" + p + ", q=" + q + ", g=" + g + "]";
	}

}
