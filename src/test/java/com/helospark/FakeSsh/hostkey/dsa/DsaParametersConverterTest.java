package com.helospark.FakeSsh.hostkey.dsa;

import static org.testng.Assert.assertEquals;

import java.math.BigInteger;

import org.bouncycastle.crypto.params.DSAParameters;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.FakeSsh.hostkey.dsa.DsaParametersConverter;
import com.helospark.FakeSsh.hostkey.dsa.domain.DsaParamsImpl;
import com.helospark.FakeSsh.hostkey.dsa.domain.DsaPrivateKeyImpl;

public class DsaParametersConverterTest {
	private DsaParametersConverter underTest;

	@BeforeMethod
	public void setUp() {
		underTest = new DsaParametersConverter();
	}

	@Test
	public void testConvertShouldReturnExpectedResponse() {
		// GIVEN
		DsaParamsImpl dsaParamsImpl = new DsaParamsImpl(new BigInteger("1"), new BigInteger("2"), new BigInteger("3"));
		DsaPrivateKeyImpl dsaPrivateKey = new DsaPrivateKeyImpl(dsaParamsImpl, new BigInteger("4"));

		// WHEN
		DSAParameters result = underTest.convert(dsaPrivateKey);

		// THEN
		assertEquals(result.getG(), new BigInteger("3"));
		assertEquals(result.getQ(), new BigInteger("2"));
		assertEquals(result.getP(), new BigInteger("1"));
	}
}
