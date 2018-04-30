package com.helospark.FakeSsh.hmac.implementation;

import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.hmac.AbstractMessageDigestBasedHmacFactory;
import com.helospark.FakeSsh.hmac.SshMac;

@Component
@Order(0)
public class Sha1HmacFactory extends AbstractMessageDigestBasedHmacFactory {
	private static final String HMAC_ALGORITHM_SIGNATURE = "hmac-sha1";
	private static final String MESSAGE_DIGEST_ALGORITM_NAME = "SHA-1";
	private static final int SHA1_BLOCK_SIZE = 64;
	private static final int SHA1_RESULT_SIZE = 20;
	private static final int KEY_SIZE = 20;

	@Override
	public SshMac createMac(byte[] key) {
		return super.createMessageDigestBackedHMac(MESSAGE_DIGEST_ALGORITM_NAME, SHA1_BLOCK_SIZE, SHA1_RESULT_SIZE, key);
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
