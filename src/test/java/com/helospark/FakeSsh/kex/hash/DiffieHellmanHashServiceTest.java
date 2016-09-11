package com.helospark.FakeSsh.kex.hash;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.kex.hash.SshKeyHashGeneratorService;
import com.helospark.FakeSsh.kex.hash.SshHash;

public class DiffieHellmanHashServiceTest {
	@Mock
	private SshHash hashFunction;
	private SshKeyHashGeneratorService underTest;

	@BeforeMethod
	public void setUp() {
		initMocks(this);
		underTest = new SshKeyHashGeneratorService();
	}

	@Test
	public void testHashShouldReturnExpectedResultWhenTheGeneratedLengthIsLongerThanAskedLength() throws IOException {
		// GIVEN
		SshConnection connection = createSshConnection();
		when(hashFunction.hash(new byte[] { 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, (byte) 'A', 0x05, 0x06 })).thenReturn("HEY".getBytes());

		// WHEN
		byte[] actual = underTest.hash(connection, 'A', 2);

		// THEN
		assertEquals(actual, "HE".getBytes());
	}

	@Test
	public void testHashShouldReturnExpectedResultWhenTheGeneratedLengthIsShorterThanAskedLength() throws IOException {
		// GIVEN
		SshConnection connection = createSshConnection();
		when(hashFunction.hash(new byte[] { 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 'A', 0x05, 0x06 })).thenReturn("XYZ".getBytes());
		when(hashFunction.hash(new byte[] { 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 'X', 'Y', 'Z' })).thenReturn("123".getBytes());

		// WHEN
		byte[] actual = underTest.hash(connection, 'A', 4);

		// THEN
		assertEquals(actual, "XYZ1".getBytes());
	}

	private SshConnection createSshConnection() {
		SshConnection connection = new SshConnection();
		connection.setHashFunction(hashFunction);
		connection.setKey(new MpInt(new BigInteger("2")));
		connection.setHash(new byte[] { 0x03, 0x04 });
		connection.setSessionId(new byte[] { 0x05, 0x06 });
		return connection;
	}
}
