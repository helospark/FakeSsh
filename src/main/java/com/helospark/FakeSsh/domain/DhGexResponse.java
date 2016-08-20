package com.helospark.FakeSsh.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class DhGexResponse {
	private PacketType type = PacketType.SSH_MSG_KEX_DH_GEX_GROUP;
	private MpInt safePrimeP;
	private MpInt generatorG;

	public PacketType getType() {
		return type;
	}

	public MpInt getSafePrimeP() {
		return safePrimeP;
	}

	public void setSafePrimeP(MpInt safePrimeP) {
		this.safePrimeP = safePrimeP;
	}

	public MpInt getGeneratorG() {
		return generatorG;
	}

	public void setGeneratorG(MpInt generatorQ) {
		this.generatorG = generatorQ;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(type.getValue());
		byteStream.write(safePrimeP.serialize());
		byteStream.write(generatorG.serialize());
		return byteStream.toByteArray();
	}
}
