package com.helospark.FakeSsh;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.util.Base64;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.signiture.DsaParamsImpl;
import com.helospark.FakeSsh.signiture.DsaPrivateKeyImpl;

/**
 * Service to provides the server's DSA public and private keys.
 * @author helospark
 */
@Component
public class DsaKeyProvider implements InitializingBean {
	private static final String SSH_DSS_NAME = "ssh-dss";
	private static final String COMMENT_START_CHARACTERS = "--";
	private String privateKeyName;
	private BufferedReaderFactory bufferedReaderFactory;
	private LoggerSupport loggerSupport;
	private DSAPrivateKey dsaPrivateKey;
	private SshString publicKey;

	@Autowired
	public DsaKeyProvider(@Value("${PRIVATE_KEY_PATH}") String privateKeyName, BufferedReaderFactory bufferedReaderFactory,
			LoggerSupport loggerSupport) {
		this.privateKeyName = privateKeyName;
		this.bufferedReaderFactory = bufferedReaderFactory;
		this.loggerSupport = loggerSupport;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] data = getRawDataFromPrivateKeyFile();
		initializeFromData(data);
		logDsaKeys(dsaPrivateKey, publicKey);
	}

	private byte[] getRawDataFromPrivateKeyFile() throws IOException {
		BufferedReader stream = bufferedReaderFactory.createClasspathBufferedReader(privateKeyName);
		String content = readEntireContent(stream);
		return Base64.getDecoder().decode(content);
	}

	private String readEntireContent(BufferedReader stream) throws IOException {
		String content = new String();
		while (stream.ready()) {
			String line = stream.readLine();
			if (!line.startsWith(COMMENT_START_CHARACTERS)) {
				content += line;
			}
		}
		return content;
	}

	private void initializeFromData(byte[] data) throws IOException {
		ASN1InputStream input = new ASN1InputStream(data);

		ASN1Sequence asnSequence = createAsnInputStream(input);
		DSAParams dsaParams = readDsaParams(asnSequence);

		BigInteger privateKeyInteger = readPrivateKey(asnSequence, dsaParams);
		dsaPrivateKey = initializePrivateKey(privateKeyInteger, dsaParams);

		BigInteger publicKeyInteger = readPublicKeyInteger(asnSequence);
		publicKey = initializePublicKey(publicKeyInteger, dsaPrivateKey);

		input.close();
	}

	private ASN1Sequence createAsnInputStream(ASN1InputStream input) throws IOException {
		ASN1Primitive primitive = input.readObject();
		ASN1Sequence asnSequence = ASN1Sequence.getInstance(primitive);
		return asnSequence;
	}

	private DSAParams readDsaParams(ASN1Sequence asnSequence) {
		ASN1Integer pValue = ASN1Integer.getInstance(asnSequence.getObjectAt(1));
		ASN1Integer qValue = ASN1Integer.getInstance(asnSequence.getObjectAt(2));
		ASN1Integer gValue = ASN1Integer.getInstance(asnSequence.getObjectAt(3));
		DSAParams dsaParams = new DsaParamsImpl(pValue.getPositiveValue(), qValue.getPositiveValue(), gValue.getPositiveValue());
		return dsaParams;
	}

	private BigInteger readPublicKeyInteger(ASN1Sequence asnSequence) {
		return ASN1Integer.getInstance(asnSequence.getObjectAt(4)).getPositiveValue();
	}

	private SshString initializePublicKey(BigInteger publicKeyInteger, DSAPrivateKey dsaPrivateKey) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(new SshString(SSH_DSS_NAME).serialize());
		outputStream.write(new MpInt(dsaPrivateKey.getParams().getP()).serialize());
		outputStream.write(new MpInt(dsaPrivateKey.getParams().getQ()).serialize());
		outputStream.write(new MpInt(dsaPrivateKey.getParams().getG()).serialize());
		outputStream.write(new MpInt(publicKeyInteger).serialize());
		return new SshString(outputStream.toByteArray());
	}

	private BigInteger readPrivateKey(ASN1Sequence asnSequence, DSAParams dsaParams) {
		return ASN1Integer.getInstance(asnSequence.getObjectAt(5)).getPositiveValue();
	}

	private DSAPrivateKey initializePrivateKey(BigInteger privateKeyInteger, DSAParams dsaParams) {
		return new DsaPrivateKeyImpl(dsaParams, privateKeyInteger);
	}

	private void logDsaKeys(DSAPrivateKey dsaPrivateKey, SshString publicKey) {
		try {
			loggerSupport.dumpBigIntegerInHex(dsaPrivateKey.getParams().getP(), "P");
			loggerSupport.dumpBigIntegerInHex(dsaPrivateKey.getParams().getQ(), "Q");
			loggerSupport.dumpBigIntegerInHex(dsaPrivateKey.getParams().getG(), "G");
			loggerSupport.dumpBigIntegerInHex(dsaPrivateKey.getX(), "Private key");
			loggerSupport.dumpByteArrayInHex(publicKey.serialize(), "Public key");
		} catch (IOException e) {
			throw new RuntimeException("Unable to log data", e);
		}
	}

	public SshString providePublicKey() {
		return publicKey;
	}

	public DSAPrivateKey providePrivateKey() {
		return dsaPrivateKey;
	}
}
