package com.helospark.FakeSsh.kex;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.cipher.SshCipher;
import com.helospark.FakeSsh.cipher.SshCipherProvider;
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
public class MacAndCipherPopulator {
	private DiffieHellmanHashService diffieHellmanHashService;
	private SshCipherProvider sshCipherProvider;
	private SshMacProvider sshMacProvider;
	private LoggerSupport loggerSupport;

	@Autowired
	public MacAndCipherPopulator(DiffieHellmanHashService diffieHellmanHashService, SshCipherProvider sshCipherProvider, SshMacProvider sshMacProvider, LoggerSupport loggerSupport) {
		this.diffieHellmanHashService = diffieHellmanHashService;
		this.sshCipherProvider = sshCipherProvider;
		this.sshMacProvider = sshMacProvider;
		this.loggerSupport = loggerSupport;
	}

	public void populateMacAndCipherOnConnection(SshConnection connection) throws IOException, NoSuchAlgorithmException {
		NegotiatedAlgorithmList negotiatedAlgorithms = connection.getNegotiatedAlgoritms();
		populateCipher(connection, negotiatedAlgorithms);
		populateMac(connection, negotiatedAlgorithms);
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

}
