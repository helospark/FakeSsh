package com.helospark.FakeSsh.hostkey.dsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;

import javax.annotation.PostConstruct;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;

import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.hostkey.Base64PrivateKeyReader;
import com.helospark.FakeSsh.hostkey.ServerHostKeyAlgorithm;
import com.helospark.FakeSsh.signiture.DsaParamsImpl;
import com.helospark.FakeSsh.signiture.DsaPrivateKeyImpl;
import com.helospark.FakeSsh.util.LoggerSupport;
import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;
import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Value;

/**
 * Service to provides the server's DSA public and private keys.
 * @author helospark
 */
@Component
@Order(1)
public class DsaServerHostKeyAlgorithm implements ServerHostKeyAlgorithm {
	private static final String SSH_DSS_NAME = "ssh-dss";
	private String privateKeyName;
	private Base64PrivateKeyReader base64PrivateKeyReader;
	private LoggerSupport loggerSupport;
	private DssSignatureService dssSignatureService;

	private SshString publicKey;
	private DSAPrivateKey dsaPrivateKey;

	@Autowired
	public DsaServerHostKeyAlgorithm(@Value("${DSA_PRIVATE_KEY}") String privateKeyName, Base64PrivateKeyReader base64PrivateKeyReader,
			LoggerSupport loggerSupport, DssSignatureService dssSignatureService) throws Exception {
		this.privateKeyName = privateKeyName;
		this.base64PrivateKeyReader = base64PrivateKeyReader;
		this.loggerSupport = loggerSupport;
		this.dssSignatureService = dssSignatureService;
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		byte[] data = base64PrivateKeyReader.read(privateKeyName);
		initializeFromData(data);
		logDsaKeys(dsaPrivateKey, publicKey);
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

	@Override
	public SshString providePublicKey() {
		return publicKey;
	}

	@Override
	public String getSignatureName() {
		return SSH_DSS_NAME;
	}

	@Override
	public byte[] sign(byte[] dataToSign) throws Exception {
		return dssSignatureService.sign(dataToSign, dsaPrivateKey);
	}

}
