package com.helospark.FakeSsh.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.helospark.FakeSsh.util.ByteConverterUtils;

public class ByteConverterUtilsTest {

	@Test
	public void testIntToByteArrayShouldReturnExpectedResponse() {
		// GIVEN
		byte[] expected = new byte[] { 0, 0, 0, 4 };

		// WHEN
		byte[] actual = ByteConverterUtils.intToByteArray(4);

		// THEN
		assertEquals(actual, expected);
	}

	@Test
	public void testIntToByteArrayShouldReturnExpectedResponseForLargeValues() {
		// GIVEN
		byte[] expected = new byte[] { 0, 0, 1, 0 };

		// WHEN
		byte[] actual = ByteConverterUtils.intToByteArray(256);

		// THEN
		assertEquals(actual, expected);
	}

	@Test
	public void testByteArrayToIntShouldReturnExpectedResult() {
		// GIVEN

		// WHEN
		int actual = ByteConverterUtils.byteArrayToInt(new byte[] { 0, 0, 1, 0 });

		// THEN
		assertEquals(actual, 256);
	}

	@Test
	public void testByteArrayToIntWithOffserShouldReturnExpectedResult() {
		// GIVEN

		// WHEN
		int actual = ByteConverterUtils.byteArrayToInt(new byte[] { 3, 0, 0, 1, 0 }, 1);

		// THEN
		assertEquals(actual, 256);
	}
}
