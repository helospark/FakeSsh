package com.helospark.FakeSsh;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.signiture.DsaParamsImpl;
import com.helospark.FakeSsh.signiture.DsaPrivateKeyImpl;

public class DsaKeyProviderTest {
	private final BigInteger p = new BigInteger(
			"127530963254964892592777602889889983528925239731462325355592668765565322094205655612046251573358614658991676790265473424951491913927397584927309019694128123982533148864649477315026884318886727631199882238859809935349693591885644809686276141172282437756739498324125720303192347297453953685168969314850636424159");
	private final BigInteger q = new BigInteger("896695903887663778152907726951020561561008720009");
	private final BigInteger g = new BigInteger(
			"7285878374644640978416608546708250537040409948961379223943273727329666540397190251231040165676600564883086865423007374152066851834064640491480724685398802828095670191349419469914225153906704920190594765058513347889151795620053604646576579854437826901945879812628035126810361365748236541189582336737434895616");
	private final BigInteger privateKeyValue = new BigInteger("874366633871273990755640624740907563267225894496");
	private final BigInteger publicKey = new BigInteger(
			"24243249267251792832435600843611747032684264938210026416652820571408233918482831059367294246395647083092670500530451660668568847885507999057895025685631458483922640656103620021033250279283506026120940904720562220401050531187019340421321758835029286065656620312399701881070646495391810078835386801389061879972");
	@Mock
	private BufferedReaderFactory bufferedReaderFactory;
	@Mock
	private LoggerSupport loggerSupport;
	@Mock
	private BufferedReader bufferedReader;

	private DsaKeyProvider underTest;

	@BeforeMethod
	public void setUp() throws IOException {
		initMocks(this);
		underTest = new DsaKeyProvider("privateKeyName", bufferedReaderFactory, loggerSupport);
		when(bufferedReaderFactory.createClasspathBufferedReader("privateKeyName")).thenReturn(bufferedReader);
		setFileForBufferedReader();
	}

	@Test
	public void testAfterPropertiesSetShouldCalculateExpectedPrivateKey() throws Exception {
		// GIVEN
		DsaPrivateKeyImpl dsaPrivateKey = createDsaPrivateKey();

		// WHEN
		underTest.afterPropertiesSet();

		// THEN
		assertEquals(underTest.providePrivateKey(), dsaPrivateKey);
	}

	@Test
	public void testAfterPropertiesSetShouldCalculateExpectedPublicKey() throws Exception {
		// GIVEN
		SshString publicKey = createExpectedPublicKey();

		// WHEN
		underTest.afterPropertiesSet();

		// THEN
		assertEquals(underTest.providePublicKey(), publicKey);
	}

	@Test
	public void testAfterPropertiesSetShouldLogData() throws Exception {
		// GIVEN
		SshString publicKey = createExpectedPublicKey();

		// WHEN
		underTest.afterPropertiesSet();

		// THEN
		verify(loggerSupport).dumpBigIntegerInHex(q, "Q");
		verify(loggerSupport).dumpBigIntegerInHex(p, "P");
		verify(loggerSupport).dumpBigIntegerInHex(g, "G");
		verify(loggerSupport).dumpBigIntegerInHex(privateKeyValue, "Private key");
		verify(loggerSupport).dumpByteArrayInHex(publicKey.serialize(), "Public key");
	}

	@Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Unable to log data")
	public void testAfterPropertiesSetShouldThrowExceptionWhenDataCannotBeLogged() throws Exception {
		// GIVEN
		doThrow(IOException.class).when(loggerSupport).dumpBigIntegerInHex(q, "Q");

		// WHEN
		underTest.afterPropertiesSet();

		// THEN throws
	}

	private SshString createExpectedPublicKey() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(new SshString("ssh-dss").serialize());
		outputStream.write(new MpInt(p).serialize());
		outputStream.write(new MpInt(q).serialize());
		outputStream.write(new MpInt(g).serialize());
		outputStream.write(new MpInt(publicKey).serialize());
		return new SshString(outputStream.toByteArray());
	}

	private DsaPrivateKeyImpl createDsaPrivateKey() {
		DsaParamsImpl dsaParams = createDsaParams();
		return new DsaPrivateKeyImpl(dsaParams, privateKeyValue);
	}

	private DsaParamsImpl createDsaParams() {
		return new DsaParamsImpl(p, q, g);
	}

	private void setFileForBufferedReader() throws IOException {
		when(bufferedReader.ready()).thenReturn(true, true, true, true, true, true,
				true, true, true, true, true, true, false);
		when(bufferedReader.readLine()).thenReturn(
				"-----BEGIN DSA PRIVATE KEY-----",
				"MIIBuwIBAAKBgQC1nDDS6AcHiIHj8AdaIAyyobXfRzJm6d48v5+KdAEB5t90jP8r",
				"i8CSQjYaw3Mah1pf7WXYRJiYrnJkd7UPoOaeZSuuE7hxWnIbOYwoVHEXOKcoW1DM",
				"tHmQq9ybHRy8q0eDMPODbx61xtvmZcSttt0VTt1H0WY6n3tq0dhIQHHj3wIVAJ0R",
				"PCeQRwxv4xFN8iTOzflVL8iJAoGACmAchYbkDg1lr9UldJmdmI1xjQn+yYLgDAbI",
				"yLZyfG82IZcaEokwiMdH2mtVJwCd/eHg7OylxEJN8THiKxb+SodBUtyvnvRbd71A",
				"d9vvHoR4/ILKIyb4BHdFNoC/u09eqz0mZH/AdnQwlsZEuZcl5Jb211JK3EUwSlC6",
				"tP9rhQACgYAihgZA6DM3VA0P8TyuQQq/FATsANjOO4F7iyFt4+Q0kM1D7bKksVoQ",
				"ESSlHVGMe9+Gcf/Udz0Uvk2EjTn4sfG9Q42HTWFEtW+ZBqy/n14LRPnGR03yEqWy",
				"H1phWCIFeHNlvdF+8B2WznwAoTV65U5ThfocBc85g4mgsfkIs5U4pAIVAJkn9LSo",
				"b7ujGyAcGUqj9splQT5g",
				"-----END DSA PRIVATE KEY-----");

	}
}
