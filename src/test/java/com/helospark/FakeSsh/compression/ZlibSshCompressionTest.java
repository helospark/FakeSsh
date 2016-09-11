package com.helospark.FakeSsh.compression;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.FakeSsh.compression.implementation.ZlibSshCompression;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

public class ZlibSshCompressionTest {
	private ZlibSshCompression underTest;

	@BeforeMethod
	public void setUp() {
		Deflater deflater = new Deflater();
		deflater.init(6);
		Inflater inflater = new Inflater();
		inflater.inflateInit(JZlib.MAX_WBITS, JZlib.W_ZLIB);
		underTest = new ZlibSshCompression(deflater, inflater);
	}

	@Test
	public void testCompressThenDecompressShouldReturnSameResult() {
		// GIVEN
		byte[] dataToCompress = "Hello world!".getBytes();

		// WHEN
		byte[] compressed = underTest.compress(dataToCompress);
		byte[] result = underTest.decompress(compressed);

		// THEN
		assertEquals(result, dataToCompress);
	}

	@Test
	public void testCompressShouldReturnExpectedResponse() {
		// GIVEN
		byte[] dataToCompress = new byte[] { (byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x73, (byte) 0x73, (byte) 0x68, (byte) 0x2d, (byte) 0x75, (byte) 0x73, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x75, (byte) 0x74,
				(byte) 0x68 };
		byte[] expectedResult = new byte[] { (byte) 0x78, (byte) 0x9c, (byte) 0x62, (byte) 0x65, (byte) 0x60, (byte) 0x60, (byte) 0xe0, (byte) 0x29, (byte) 0x2e, (byte) 0xce, (byte) 0xd0, (byte) 0x2d, (byte) 0x2d, (byte) 0x4e, (byte) 0x2d, (byte) 0x4a,
				(byte) 0x2c, (byte) 0x2d, (byte) 0xc9, (byte) 0x00, (byte) 0x08 };

		// WHEN
		byte[] result = underTest.compress(dataToCompress);

		// THEN
		assertEquals(result, expectedResult);
	}
}
