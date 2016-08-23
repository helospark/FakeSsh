package com.helospark.FakeSsh;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MessageDigestBackedHMacAlgorithmTest {

	@Test
	public void testHmacWithHiThereData() throws NoSuchAlgorithmException, IOException {
		// GIVEN
		MessageDigestBackedHMacAlgorithm underTest = createWithKey(new byte[] { 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b });
		byte[] expected = new byte[] { (byte) 0x92, (byte) 0x94, 0x72, 0x7a, 0x36, 0x38, (byte) 0xbb, 0x1c, 0x13, (byte) 0xf4, (byte) 0x8e, (byte) 0xf8, 0x15, (byte) 0x8b, (byte) 0xfc, (byte) 0x9d };

		// WHEN
		byte[] actual = underTest.createMac("Hi There".getBytes());

		// THEN
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testHmacWithLongerData() throws NoSuchAlgorithmException, IOException {
		// GIVEN
		MessageDigestBackedHMacAlgorithm underTest = createWithKey("Jefe".getBytes());
		byte[] expected = new byte[] { 0x75, 0x0c, 0x78, 0x3e, 0x6a, (byte) 0xb0, (byte) 0xb5, 0x03, (byte) 0xea, (byte) 0xa8, 0x6e, 0x31, 0x0a, 0x5d, (byte) 0xb7, 0x38 };

		// WHEN
		byte[] actual = underTest.createMac("what do ya want for nothing?".getBytes());

		// THEN
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testHmacWithLongDataAndLongKey() throws NoSuchAlgorithmException, IOException {
		// GIVEN
		MessageDigestBackedHMacAlgorithm underTest = createWithKey(new byte[] { (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa,
				(byte) 0xaa, (byte) 0xaa, (byte) 0xaa });
		byte[] expected = new byte[] { 0x56, (byte) 0xbe, 0x34, 0x52, 0x1d, 0x14, 0x4c, (byte) 0x88, (byte) 0xdb, (byte) 0xb8, (byte) 0xc7, 0x33, (byte) 0xf0, (byte) 0xe8, (byte) 0xb3, (byte) 0xf6 };

		// WHEN
		byte[] actual = underTest.createMac(new byte[] { (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd,
				(byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd,
				(byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd, (byte) 0xdd });

		// THEN
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testHmacValidationWithHiThereData() throws NoSuchAlgorithmException, IOException {
		// GIVEN
		MessageDigestBackedHMacAlgorithm underTest = createWithKey(new byte[] { 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b });
		byte[] toCompareWith = new byte[] { (byte) 0x92, (byte) 0x94, 0x72, 0x7a, 0x36, 0x38, (byte) 0xbb, 0x1c, 0x13, (byte) 0xf4, (byte) 0x8e, (byte) 0xf8, 0x15, (byte) 0x8b, (byte) 0xfc, (byte) 0x9d };

		// WHEN
		boolean result = underTest.checkMac("Hi There".getBytes(), toCompareWith);

		// THEN
		Assert.assertTrue(result);
	}

	@Test
	public void testHmacValidationWithHiThereDataShouldReturnFalseWhenDataIsDifferent() throws NoSuchAlgorithmException, IOException {
		// GIVEN
		MessageDigestBackedHMacAlgorithm underTest = createWithKey(new byte[] { 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b });
		byte[] toCompareWith = new byte[] { (byte) 0x91, (byte) 0x94, 0x72, 0x7a, 0x36, 0x38, (byte) 0xbb, 0x1c, 0x13, (byte) 0xf4, (byte) 0x8e, (byte) 0xf8, 0x15, (byte) 0x8b, (byte) 0xfc, (byte) 0x9d };

		// WHEN
		boolean result = underTest.checkMac("Hi There".getBytes(), toCompareWith);

		// THEN
		Assert.assertFalse(result);
	}

	private MessageDigestBackedHMacAlgorithm createWithKey(byte[] key) throws NoSuchAlgorithmException {
		return new MessageDigestBackedHMacAlgorithm(MessageDigest.getInstance("MD5"), key, 64);
	}
}
