package com.helospark.FakeSsh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;

@Component
public class PublicKeyProvider implements InitializingBean {
	private static final String FALLBACK_PUBLIC_KEY_PATH = "defaultKey.pub";
	private String publicKeyPath;
	private SshString publicKey;
	private SshString signature;

	@Autowired
	public PublicKeyProvider(@Value("${PUBLIC_KEY_PATH:}") String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] data = readPublicKeyFromFile();
		String base64EncodedPublicKey = extractBase64EncodedKey(data);
		ByteArrayInputStream byteStream = new ByteArrayInputStream(base64Decode(base64EncodedPublicKey));
		signature = new SshString(byteStream);
		MpInt p = new MpInt(byteStream);
		MpInt q = new MpInt(byteStream);
		MpInt g = new MpInt(byteStream);
		MpInt y = new MpInt(byteStream);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(signature.serialize());
		outputStream.write(p.serialize());
		outputStream.write(q.serialize());
		outputStream.write(g.serialize());
		outputStream.write(y.serialize());
		publicKey = new SshString(outputStream.toByteArray());
	}

	private byte[] readPublicKeyFromFile() throws IOException, FileNotFoundException {
		File file = getFile();
		return readRawData(file);
	}

	private File getFile() throws IOException {
		File file = new File(publicKeyPath);
		if (!file.exists()) {
			file = new ClassPathResource(FALLBACK_PUBLIC_KEY_PATH).getFile();
		}
		return file;
	}

	private byte[] readRawData(File file) throws IOException, FileNotFoundException {
		byte[] data;
		try (FileInputStream fis = new FileInputStream(file)) {
			data = new byte[(int) file.length()];
			fis.read(data);
		}
		return data;
	}

	private String extractBase64EncodedKey(byte[] data) throws UnsupportedEncodingException {
		String content = new String(data, ApplicationConstants.SSH_PROTOCOL_STRING_ENCODING_CHARSET);
		String[] publicKeyParts = content.split(" ");
		if (publicKeyParts.length != 3) {
			throw new RuntimeException("Public key does not conform to OpenSSH format");
		}
		return publicKeyParts[1];
	}

	private byte[] base64Decode(String base64EncodedKey) throws UnsupportedEncodingException {
		byte[] publicKeyBytes = Base64.getDecoder().decode(base64EncodedKey);
		return publicKeyBytes;
	}

	public SshString provide() {
		return publicKey;
	}

	public SshString provideSignature() {
		return signature;
	}

}
