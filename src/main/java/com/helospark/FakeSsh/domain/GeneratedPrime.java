package com.helospark.FakeSsh.domain;

import java.math.BigInteger;

public class GeneratedPrime {
	private int bitLength;
	private BigInteger generator;
	private BigInteger prime;

	public GeneratedPrime(int bitLength, BigInteger generator, BigInteger prime) {
		this.bitLength = bitLength;
		this.generator = generator;
		this.prime = prime;
	}

	public int getBitLength() {
		return bitLength;
	}

	public void setBitLength(int bitLength) {
		this.bitLength = bitLength;
	}

	public BigInteger getGenerator() {
		return generator;
	}

	public void setGenerator(BigInteger generator) {
		this.generator = generator;
	}

	public BigInteger getPrime() {
		return prime;
	}

	public void setPrime(BigInteger prime) {
		this.prime = prime;
	}

}
