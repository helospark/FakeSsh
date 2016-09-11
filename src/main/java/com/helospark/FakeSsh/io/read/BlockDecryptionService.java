package com.helospark.FakeSsh.io.read;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.cipher.SshCipher;

/**
 * Decrypts the given block.
 * The given data should have a size that is multiple of the cipher's blocksize
 * @author helospark
 */
@Component
public class BlockDecryptionService {

	public byte[] decryptData(Optional<SshCipher> cipher, byte[] data) {
		byte[] decryptedData;
		if (cipher.isPresent()) {
			decryptedData = cipher.get().decrypt(data);
		} else {
			decryptedData = data;
		}
		return decryptedData;
	}
}
