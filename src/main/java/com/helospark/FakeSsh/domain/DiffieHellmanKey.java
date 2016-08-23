package com.helospark.FakeSsh.domain;

public class DiffieHellmanKey {
	MpInt f;
	MpInt k;

	public DiffieHellmanKey(MpInt f, MpInt k) {
		this.f = f;
		this.k = k;
	}

	public MpInt getF() {
		return f;
	}

	public MpInt getK() {
		return k;
	}

}
