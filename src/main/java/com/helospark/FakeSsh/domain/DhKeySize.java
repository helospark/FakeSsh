package com.helospark.FakeSsh.domain;

import com.helospark.FakeSsh.ByteConverterUtils;

public class DhKeySize {
	private int minimumLength;
	private int preferredLength;
	private int maximumLength;

	public int getMinimumLength() {
		return minimumLength;
	}

	public int getPreferredLength() {
		return preferredLength;
	}

	public int getMaximumLength() {
		return maximumLength;
	}

	public void deserialize(byte[] data) {
		this.minimumLength = ByteConverterUtils.byteToInt(data, 0);
		this.preferredLength = ByteConverterUtils.byteToInt(data, 4);
		this.maximumLength = ByteConverterUtils.byteToInt(data, 8);
	}
}
