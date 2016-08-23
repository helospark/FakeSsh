package com.helospark.FakeSsh;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.DiffieHellmanKey;
import com.helospark.FakeSsh.domain.GeneratedPrime;
import com.helospark.FakeSsh.domain.MpInt;

@Component
public class DiffieHellmanKeyCalculatorService {

	public DiffieHellmanKey calculateKey(GeneratedPrime prime, MpInt e) {
		BigInteger y = new BigInteger(prime.getBitLength() - 1, new SecureRandom());
		MpInt f = new MpInt(prime.getGenerator().modPow(y, prime.getPrime()));
		MpInt k = new MpInt(e.getBigInteger().modPow(y, prime.getPrime()));
		return new DiffieHellmanKey(f, k);
	}
}
