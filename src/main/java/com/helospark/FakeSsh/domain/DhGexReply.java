package com.helospark.FakeSsh.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.helospark.FakeSsh.PacketType;

public class DhGexReply {
	private PacketType packetType;
	private SshString publicKey;
	private MpInt f;
	private SshString hash;

	public PacketType getPacketType() {
		return packetType;
	}

	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}

	public SshString getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(SshString publicKey) {
		this.publicKey = publicKey;
	}

	public MpInt getF() {
		return f;
	}

	public void setF(MpInt f) {
		this.f = f;
	}

	public SshString getHash() {
		return hash;
	}

	public void setHash(SshString hash) {
		this.hash = hash;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(packetType.getValue());
		byteStream.write(publicKey.serialize());
		byteStream.write(f.serialize());
		byteStream.write(hash.serialize());
		return byteStream.toByteArray();
	}
}
