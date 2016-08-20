package com.helospark.FakeSsh;

public class Packet {
	private byte[] data;

	public Packet(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
