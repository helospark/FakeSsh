package com.helospark.FakeSsh.hmac.implementation;

import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.ApplicationConstants;
import com.helospark.FakeSsh.hmac.AbstractMessageDigestBasedHmacFactory;
import com.helospark.FakeSsh.hmac.SshMac;

@Component
@Order(4)
public class Md596HmacFactory extends AbstractMessageDigestBasedHmacFactory {
	private static final String HMAC_ALGORITHM_SIGNATURE = "hmac-md5-96";
	private static final String MESSAGE_DIGEST_ALGORITM_NAME = "MD5";
	private static final int MD5_BLOCK_SIZE = 64;
	private static final int MD5_RESULT_SIZE = 96 / ApplicationConstants.BIT_PER_BYTES;
	private static final int KEY_SIZE = 16;

	@Override
	public SshMac createMac(byte[] key) {
		return super.createMessageDigestBackedHMac(MESSAGE_DIGEST_ALGORITM_NAME, MD5_BLOCK_SIZE, MD5_RESULT_SIZE, key);
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
