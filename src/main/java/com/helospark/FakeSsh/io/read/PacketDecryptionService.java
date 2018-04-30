package com.helospark.FakeSsh.io.read;

import java.util.Optional;

import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.cipher.SshCipher;

@Component
public class PacketDecryptionService {

	public byte[] decryptPacket(Optional<SshCipher> optionalCipher, byte[] bytes) {
		return optionalCipher.map(cipher -> cipher.decrypt(bytes)).orElse(bytes);
	}
}
