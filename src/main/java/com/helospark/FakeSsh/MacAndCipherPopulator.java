package com.helospark.FakeSsh;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;

/**
 * Populates the MAC and Cipher fields on the SshConnection.
 * @author helospark
 */
@Component
public class MacAndCipherPopulator {
	private DiffieHellmanHashService diffieHellmanHashService;
	private SshCipherFactory sshCipherFactory;
	private SshMacFactory sshMacFactory;
	private LoggerSupport loggerSupport;

	@Autowired
	public MacAndCipherPopulator(DiffieHellmanHashService diffieHellmanHashService, SshCipherFactory sshCipherFactory, SshMacFactory sshMacFactory, LoggerSupport loggerSupport) {
		this.diffieHellmanHashService = diffieHellmanHashService;
		this.sshCipherFactory = sshCipherFactory;
		this.sshMacFactory = sshMacFactory;
		this.loggerSupport = loggerSupport;
	}

	public void populateMacAndCipherOnConnection(SshConnection connection) throws IOException, NoSuchAlgorithmException {
		NegotiatedAlgorithmList negotiatedAlgorithms = connection.getNegotiatedAlgoritms();
		byte[] ivClientToServer = diffieHellmanHashService.hash(connection, 'A', 16);
		byte[] ivServerToClient = diffieHellmanHashService.hash(connection, 'B', 16);
		byte[] keyClientToServer = diffieHellmanHashService.hash(connection, 'C', 16);
		byte[] keyServerToClient = diffieHellmanHashService.hash(connection, 'D', 16);
		byte[] integrityKeyClientToServer = diffieHellmanHashService.hash(connection, 'E', 20);
		byte[] integrityKeyServerToClient = diffieHellmanHashService.hash(connection, 'F', 20);

		SshCipher serverToClientCipher = sshCipherFactory.createCipher(negotiatedAlgorithms.getEncryptionAlgorithmsServerToClient(), keyServerToClient, ivServerToClient);
		SshCipher clientToServerCipher = sshCipherFactory.createCipher(negotiatedAlgorithms.getEncryptionAlgorithmsClientToServer(), keyClientToServer, ivClientToServer);
		SshMac clientToServerMac = sshMacFactory.createMac(negotiatedAlgorithms.getMacAlgorithmsClientToServer(), integrityKeyClientToServer);
		SshMac serverToClientMac = sshMacFactory.createMac(negotiatedAlgorithms.getMacAlgorithmsServerToClient(), integrityKeyServerToClient);

		loggerSupport.dumpByteArrayInHex(ivClientToServer, "Hash 'A'");
		loggerSupport.dumpByteArrayInHex(ivServerToClient, "Hash 'B'");
		loggerSupport.dumpByteArrayInHex(keyClientToServer, "Hash 'C'");
		loggerSupport.dumpByteArrayInHex(keyServerToClient, "Hash 'D'");
		loggerSupport.dumpByteArrayInHex(integrityKeyClientToServer, "Hash 'E'");
		loggerSupport.dumpByteArrayInHex(integrityKeyServerToClient, "Hash 'F'");

		connection.setServerToClientCipher(serverToClientCipher);
		connection.setClientToServerCipher(clientToServerCipher);
		connection.setClientToServerMac(clientToServerMac);
		connection.setServerToClientMac(serverToClientMac);
	}

}
