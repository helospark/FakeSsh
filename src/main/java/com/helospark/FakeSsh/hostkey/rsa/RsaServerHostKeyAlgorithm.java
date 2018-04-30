package com.helospark.FakeSsh.hostkey.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

import javax.annotation.PostConstruct;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;

import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.RsaPrivateKeyImpl;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.hostkey.Base64PrivateKeyReader;
import com.helospark.FakeSsh.hostkey.ServerHostKeyAlgorithm;
import com.helospark.FakeSsh.util.LoggerSupport;
import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;
import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Value;

/**
 * RSA key algorithm.
 * @author helospark
 */
@Component
@Order(0)
public class RsaServerHostKeyAlgorithm implements ServerHostKeyAlgorithm {
	private static final String RSA_NAME = "ssh-rsa";
	private String privateKeyFileName;
	private RsaSignatureService rsaSignerService;
	private Base64PrivateKeyReader base64PrivateKeyReader;
	private LoggerSupport loggerSupport;

	private RSAPrivateKey rsaPrivateKey;
	private SshString publicKey;

	@Autowired
	public RsaServerHostKeyAlgorithm(@Value("${RSA_PRIVATE_KEY}") String privateKeyFileName, RsaSignerFactory rsaSignerFactory,
			Base64PrivateKeyReader base64PrivateKeyReader, LoggerSupport loggerSupport, RsaSignatureService rsaSignerService) {
		this.privateKeyFileName = privateKeyFileName;
		this.rsaSignerService = rsaSignerService;
		this.base64PrivateKeyReader = base64PrivateKeyReader;
		this.loggerSupport = loggerSupport;
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		byte[] data = base64PrivateKeyReader.read(privateKeyFileName);
		initializeFromData(data);
		logRsaKeys(rsaPrivateKey, publicKey);
	}

	private void logRsaKeys(RSAPrivateKey rsaPrivateKey, SshString publicKey) throws IOException {
		loggerSupport.dumpBigIntegerInHex(rsaPrivateKey.getModulus(), "N");
		loggerSupport.dumpBigIntegerInHex(rsaPrivateKey.getPrivateExponent(), "Private");
		loggerSupport.dumpByteArrayInHex(publicKey.serialize(), "Public");
	}

	private void initializeFromData(byte[] data) throws IOException {
		ASN1InputStream input = new ASN1InputStream(data);

		ASN1Sequence asnSequence = createAsnInputStream(input);

		BigInteger modulusN = ASN1Integer.getInstance(asnSequence.getObjectAt(1)).getPositiveValue();
		BigInteger publicExponentE = ASN1Integer.getInstance(asnSequence.getObjectAt(2)).getPositiveValue();
		BigInteger privateExponent = ASN1Integer.getInstance(asnSequence.getObjectAt(3)).getPositiveValue();

		rsaPrivateKey = new RsaPrivateKeyImpl(modulusN, privateExponent);
		publicKey = initializePublicKey(publicExponentE, modulusN);
		input.close();
	}

	private SshString initializePublicKey(BigInteger e, BigInteger n) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(new SshString(RSA_NAME).serialize());
		outputStream.write(new MpInt(e).serialize());
		outputStream.write(new MpInt(n).serialize());
		return new SshString(outputStream.toByteArray());
	}

	private ASN1Sequence createAsnInputStream(ASN1InputStream input) throws IOException {
		ASN1Primitive primitive = input.readObject();
		ASN1Sequence asnSequence = ASN1Sequence.getInstance(primitive);
		return asnSequence;
	}

	@Override
	public SshString providePublicKey() {
		return publicKey;
	}

	@Override
	public byte[] sign(byte[] hash) throws Exception {
		return rsaSignerService.sign(hash, rsaPrivateKey);
	}

	@Override
	public String getSignatureName() {
		return RSA_NAME;
	}

}
