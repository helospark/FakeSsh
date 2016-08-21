package com.helospark.FakeSsh;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * Hash function that uses SHA-1.
 * @author helospark
 */
@Component
public class Sha1HashFunction implements HashFunction {

	@Override
	public byte[] hash(byte[] value) {
		return DigestUtils.getSha1Digest().digest(value);
	}
}
