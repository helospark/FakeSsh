package com.helospark.FakeSsh;

import java.util.Arrays;

public enum PacketType {
	SSH_MSG_DISCONNECT((byte) 1),
	SSH_MSG_IGNORE((byte) 2),
	SSH_MSG_UNIMPLEMENTED((byte) 3),
	SSH_MSG_DEBUG((byte) 4),
	SSH_MSG_SERVICE_REQUEST((byte) 5),
	SSH_MSG_SERVICE_ACCEPT((byte) 6),
	SSH_MSG_KEXINIT((byte) 20),
	SSH_MSG_NEWKEYS((byte) 21),
	SSH_MSG_KEX_DH_GEX_REQUEST_OLD((byte) 30),
	SSH_MSG_KEX_DH_GEX_GROUP((byte) 31),
	SSH_MSG_KEX_DH_GEX_INIT((byte) 32),
	SSH_MSG_KEX_DH_GEX_REPLY((byte) 33),
	SSH_MSG_KEX_DH_GEX_REQUEST((byte) 34),
	SSH_MSG_USERAUTH_REQUEST((byte) 50),
	SSH_MSG_USERAUTH_FAILURE((byte) 51),
	SSH_MSG_USERAUTH_SUCCESS((byte) 52),
	SSH_MSG_USERAUTH_BANNER((byte) 53),
	SSH_MSG_GLOBAL_REQUEST((byte) 80),
	SSH_MSG_REQUEST_SUCCESS((byte) 81),
	SSH_MSG_REQUEST_FAILURE((byte) 82),
	SSH_MSG_CHANNEL_OPEN((byte) 90),
	SSH_MSG_CHANNEL_OPEN_CONFIRMATION((byte) 91),
	SSH_MSG_CHANNEL_OPEN_FAILURE((byte) 92),
	SSH_MSG_CHANNEL_WINDOW_ADJUST((byte) 93),
	SSH_MSG_CHANNEL_DATA((byte) 94),
	SSH_MSG_CHANNEL_EXTENDED_DATA((byte) 95),
	SSH_MSG_CHANNEL_EOF((byte) 96),
	SSH_MSG_CHANNEL_CLOSE((byte) 97),
	SSH_MSG_CHANNEL_REQUEST((byte) 98),
	SSH_MSG_CHANNEL_SUCCESS((byte) 99),
	SSH_MSG_CHANNEL_FAILURE((byte) 100);

	byte value;

	PacketType(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public static PacketType fromValue(byte type) {
		return Arrays.stream(PacketType.values())
				.filter(packetType -> packetType.getValue() == type)
				.findFirst()
				.get();
	}
}
