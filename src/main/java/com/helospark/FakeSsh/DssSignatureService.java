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
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.signers.DSASigner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.signiture.DsaParamsImpl;
import com.helospark.FakeSsh.signiture.DsaPrivateKeyImpl;

@Component
public class DssSignatureService implements InitializingBean {
	private String privateKeyFileName;
	private BufferedReaderFactory bufferedReaderFactory;
	private DSAPrivateKey dsaPrivateKey;
	private BigInteger publicKey;

	@Autowired
	public DssSignatureService(@Value("${PRIVATE_KEY_PATH}") String privateKeyFileName, BufferedReaderFactory bufferedReaderFactory) {
		this.privateKeyFileName = privateKeyFileName;
		this.bufferedReaderFactory = bufferedReaderFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		readAsn1();
	}

	private void readAsn1() throws IOException {
		BufferedReader stream = bufferedReaderFactory.createClasspathBufferedReader(privateKeyFileName); // obviously need to supply real data here
		String content = "";
		while (stream.ready()) {
			String line = stream.readLine();
			if (!line.startsWith("--")) {
				content += line;
			}
		}
		byte[] data = Base64.getDecoder().decode(content);
		ASN1InputStream input = new ASN1InputStream(data);
		ASN1Primitive p = input.readObject();
		ASN1Sequence asn1 = ASN1Sequence.getInstance(p);
		ASN1Integer ignoredValue = ASN1Integer.getInstance(asn1.getObjectAt(0));
		ASN1Integer pValue = ASN1Integer.getInstance(asn1.getObjectAt(1));
		ASN1Integer qValue = ASN1Integer.getInstance(asn1.getObjectAt(2));
		ASN1Integer gValue = ASN1Integer.getInstance(asn1.getObjectAt(3));
		publicKey = ASN1Integer.getInstance(asn1.getObjectAt(4)).getPositiveValue();
		ASN1Integer privKey = ASN1Integer.getInstance(asn1.getObjectAt(5));
		DSAParams params = new DsaParamsImpl(pValue.getPositiveValue(), qValue.getPositiveValue(), gValue.getPositiveValue());
		dsaPrivateKey = new DsaPrivateKeyImpl(params, privKey.getPositiveValue());
		input.close();
	}

	private void writeBigDecimal(BigInteger presetP, String dsa) {
		byte[] array = presetP.toByteArray();
		writeBigIntegerFromBytes(array, dsa);
	}

	private void writeBigIntegerFromBytes(byte[] array, String dsa) {
		System.out.println(dsa);
		for (int i = 0; i < array.length; i += 4) {
			for (int j = i; j < i + 4; ++j) {
				if (j < array.length) {
					String character = String.format("%02X:", array[j]);
					System.out.print(character);
				}
			}
			if ((i + 0) % 16 == 0 && i != 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public byte[] sign(byte[] data) throws Exception {
		DSASigner signer = new DSASigner();
		DSAParameters params = new DSAParameters(
				dsaPrivateKey.getParams().getP(),
				dsaPrivateKey.getParams().getQ(),
				dsaPrivateKey.getParams().getG());

		writeBigDecimal(dsaPrivateKey.getParams().getP(), "P");
		writeBigDecimal(dsaPrivateKey.getParams().getQ(), "Q");
		writeBigDecimal(dsaPrivateKey.getParams().getG(), "G");
		writeBigDecimal(dsaPrivateKey.getX(), "priv");

		signer.init(true, new DSAPrivateKeyParameters(dsaPrivateKey.getX(), params));
		BigInteger[] sig = signer.generateSignature(data);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(expandIfLessThanMinimumBytes(sig[0].toByteArray(), 20));
		byteStream.write(expandIfLessThanMinimumBytes(sig[1].toByteArray(), 20));
		signer.init(false, new DSAPublicKeyParameters(publicKey, params));
		//		boolean valid = signer.verifySignature(data, sig[0], sig[1]);
		//		System.out.println("VALID: " + valid);
		return byteStream.toByteArray();
	}

	private byte[] expandIfLessThanMinimumBytes(byte[] bytes, int minimumBytes) throws IOException {
		byte[] result = bytes;
		if (bytes.length < minimumBytes) {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			for (int i = bytes.length; i < minimumBytes; ++i) {
				byteStream.write((byte) 0);
			}
			byteStream.write(bytes);
			result = byteStream.toByteArray();
		} else if (bytes.length > minimumBytes) {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			for (int i = 1; i < bytes.length; ++i) {
				byteStream.write(bytes[i]);
			}
			result = byteStream.toByteArray();
		}
		return result;
	}

	public SshString providePublicKey() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(new SshString("ssh-dss").serialize());
		outputStream.write(new MpInt(dsaPrivateKey.getParams().getP()).serialize());
		outputStream.write(new MpInt(dsaPrivateKey.getParams().getQ()).serialize());
		outputStream.write(new MpInt(dsaPrivateKey.getParams().getG()).serialize());
		outputStream.write(new MpInt(publicKey).serialize());
		return new SshString(outputStream.toByteArray());
	}
}