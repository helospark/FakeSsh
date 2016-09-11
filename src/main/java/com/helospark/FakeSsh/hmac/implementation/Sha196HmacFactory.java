package com.helospark.FakeSsh.hmac.implementation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.hmac.AbstractMessageDigestBasedHmacFactory;
import com.helospark.FakeSsh.hmac.SshMac;

/**
 * Factory to create HMAC for 'hmac-sha1-96' type.
 * @author helospark
 */
@Component
@Order(1)
public class Sha196HmacFactory extends AbstractMessageDigestBasedHmacFactory {
	private static final String HMAC_ALGORITHM_SIGNATURE = "hmac-sha1-96";
	private static final String MESSAGE_DIGEST_ALGORITM_NAME = "SHA-1";
	private static final int SHA1_BLOCK_SIZE = 64;
	private static final int RESULT_SIZE = 96 / ApplicationConstants.BIT_PER_BYTES;
	private static final int KEY_SIZE = 20;

	@Override
	public SshMac createMac(byte[] key) {
		return super.createMessageDigestBackedHMac(MESSAGE_DIGEST_ALGORITM_NAME, SHA1_BLOCK_SIZE, RESULT_SIZE, key);
	}

	@Override
	public int getKeyLength() {
		return KEY_SIZE;
	}

	@Override
	public String getName() {
		return HMAC_ALGORITHM_SIGNATURE;
	}

}
