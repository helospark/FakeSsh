package com.helospark.FakeSsh.kex;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.cipher.SshCipher;
import com.helospark.FakeSsh.cipher.SshCipherProvider;
import com.helospark.FakeSsh.compression.SshCompression;
import com.helospark.FakeSsh.compression.SshCompressionProvider;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.hmac.SshMac;
import com.helospark.FakeSsh.hmac.SshMacProvider;
import com.helospark.FakeSsh.kex.hash.DiffieHellmanHashService;
import com.helospark.FakeSsh.util.LoggerSupport;

/**
 * Populates the MAC and Cipher fields on the SshConnection.
 * @author helospark
 */
@Component
public class NegotiatedAlgorithmPopulator {
	private DiffieHellmanHashService diffieHellmanHashService;
	private SshCipherProvider sshCipherProvider;
	private SshMacProvider sshMacProvider;
	private SshCompressionProvider sshCompressionProvider;
	private LoggerSupport loggerSupport;

	@Autowired
	public NegotiatedAlgorithmPopulator(DiffieHellmanHashService diffieHellmanHashService, SshCipherProvider sshCipherProvider,
			SshMacProvider sshMacProvider, SshCompressionProvider sshCompressionProvider, LoggerSupport loggerSupport) {
		this.diffieHellmanHashService = diffieHellmanHashService;
		this.sshCipherProvider = sshCipherProvider;
		this.sshMacProvider = sshMacProvider;
		this.loggerSupport = loggerSupport;
		this.sshCompressionProvider = sshCompressionProvider;
	}

	public void populateMacAndCipherOnConnection(SshConnection connection) throws IOException, NoSuchAlgorithmException {
		NegotiatedAlgorithmList negotiatedAlgorithms = connection.getNegotiatedAlgoritms();
		populateCipher(connection, negotiatedAlgorithms);
		populateMac(connection, negotiatedAlgorithms);
		populateCompression(connection, negotiatedAlgorithms);
	}

	private void populateMac(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) throws IOException, NoSuchAlgorithmException {
		String clientToServerMacAlgorithm = negotiatedAlgorithms.getMacAlgorithmsClientToServer();
		String serverToClientMacAlgorithm = negotiatedAlgorithms.getMacAlgorithmsServerToClient();
		byte[] integrityKeyClientToServer = diffieHellmanHashService.hash(connection, 'E', sshMacProvider.getKeyLength(clientToServerMacAlgorithm));
		byte[] integrityKeyServerToClient = diffieHellmanHashService.hash(connection, 'F', sshMacProvider.getKeyLength(serverToClientMacAlgorithm));
		SshMac clientToServerMac = sshMacProvider.createMac(clientToServerMacAlgorithm, integrityKeyClientToServer);
		SshMac serverToClientMac = sshMacProvider.createMac(serverToClientMacAlgorithm, integrityKeyServerToClient);

		loggerSupport.dumpByteArrayInHex(integrityKeyClientToServer, "Hash 'E'");
		loggerSupport.dumpByteArrayInHex(integrityKeyServerToClient, "Hash 'F'");

		connection.setClientToServerMac(clientToServerMac);
		connection.setServerToClientMac(serverToClientMac);
	}

	private void populateCipher(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) throws IOException {
		populateClientToServerCipher(connection, negotiatedAlgorithms);
		populateServerToClientCipher(connection, negotiatedAlgorithms);
	}

	private void populateServerToClientCipher(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) throws IOException {
		String serverToClientCipherName = negotiatedAlgorithms.getEncryptionAlgorithmsClientToServer();
		SshCipher serverToClientCipher = initializeCipherCipher(connection, serverToClientCipherName, 'B', 'D');
		connection.setServerToClientCipher(serverToClientCipher);
	}

	private void populateClientToServerCipher(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) throws IOException {
		String clientToServerCipherName = negotiatedAlgorithms.getEncryptionAlgorithmsServerToClient();
		SshCipher clientToServerCipher = initializeCipherCipher(connection, clientToServerCipherName, 'A', 'C');
		connection.setClientToServerCipher(clientToServerCipher);
	}

	private SshCipher initializeCipherCipher(SshConnection connection, String algorithmName, char ivHashChar, char keyHashChar) throws IOException {
		byte[] initializationVector = diffieHellmanHashService.hash(connection, ivHashChar, sshCipherProvider.getIvSize(algorithmName));
		byte[] cipherKey = diffieHellmanHashService.hash(connection, keyHashChar, sshCipherProvider.getKeySize(algorithmName));

		loggerSupport.dumpByteArrayInHex(initializationVector, "Hash " + ivHashChar);
		loggerSupport.dumpByteArrayInHex(cipherKey, "Hash " + keyHashChar);

		return sshCipherProvider.createCipher(algorithmName, cipherKey, initializationVector);
	}

	private void populateCompression(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) {
		populateClientToServerCompression(connection, negotiatedAlgorithms);
		populateServerToClientCompression(connection, negotiatedAlgorithms);

	}

	private void populateClientToServerCompression(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) {
		SshCompression compressionAlgorithm = sshCompressionProvider.provideCompressionAlgorithm(negotiatedAlgorithms.getCompressionAlgorithmsClientToServer());
		connection.setClientToServerCompression(compressionAlgorithm);
	}

	private void populateServerToClientCompression(SshConnection connection, NegotiatedAlgorithmList negotiatedAlgorithms) {
		SshCompression compressionAlgorithm = sshCompressionProvider.provideCompressionAlgorithm(negotiatedAlgorithms.getCompressionAlgorithmsServerToClient());
		connection.setServerToClientCompression(compressionAlgorithm);
	}
}
