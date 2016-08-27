package com.helospark.FakeSsh.io.write;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.cipher.SshCipher;

/**
 * Encrypts a packet with the given cipher, or leave nonencrypted.
 * @author helospark
 */
@Component
public class PacketEncryptionService {

	public byte[] encryptPacket(byte[] byteArray, Optional<SshCipher> optionalCipher) {
		return optionalCipher.map(cipher -> cipher.encrypt(byteArray)).orElse(byteArray);
	}
}
