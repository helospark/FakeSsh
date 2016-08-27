package com.helospark.FakeSsh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.domain.SshNamedList;
import com.helospark.FakeSsh.util.LoggerSupport;

/**
 * Extract the algorithm used from the server's and client's algorithm exchange messages.
 * The first algorithm is used that is both in the client and server list
 * @author helospark
 */
@Component
public class NegotitatedAlgorithmExtractor {
	private LoggerSupport loggerSupport;

	@Autowired
	public NegotitatedAlgorithmExtractor(LoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	public NegotiatedAlgorithmList extract(AlgorithmNegotiationList serverList, AlgorithmNegotiationList clientList) {
		NegotiatedAlgorithmList negotiatedAlgorithms = calculateAlgorithmToUse(serverList, clientList);
		loggerSupport.logDebugString(negotiatedAlgorithms.toString());
		return negotiatedAlgorithms;

	}

	/**
	 * Extract the algorithm used from the server's and client's algorithm exchange messages.
	 * @param serverList the server's algorithm exchange message
	 * @param clientList the client's algorithm exchange message
	 * @return the calculated algorithms
	 * @throws RuntimeException if no common algorithm found
	 */
	private NegotiatedAlgorithmList calculateAlgorithmToUse(AlgorithmNegotiationList serverList, AlgorithmNegotiationList clientList) {
		String clientToServerEncryption = extractFirstMatch(serverList.encryptionAlgorithmsClientToServer, clientList.encryptionAlgorithmsClientToServer, true);
		String serverToClientEncryption = extractFirstMatch(serverList.encryptionAlgorithmsServerToClient, clientList.encryptionAlgorithmsServerToClient, true);
		String languageClientToServer = extractFirstMatch(serverList.languagesClientToServer, clientList.languagesClientToServer, false);
		String languageServerToClient = extractFirstMatch(serverList.languagesServerToClient, clientList.languagesServerToClient, false);
		String macClientToServer = extractFirstMatch(serverList.macAlgorithmsClientToServer, clientList.macAlgorithmsClientToServer, true);
		String macServerToClient = extractFirstMatch(serverList.macAlgorithmsServerToClient, clientList.macAlgorithmsServerToClient, true);
		String compressionClientToServer = extractFirstMatch(serverList.compressionAlgorithmsClientToServer, clientList.compressionAlgorithmsClientToServer, false);
		String compressionServerToClient = extractFirstMatch(serverList.compressionAlgorithmsServerToClient, clientList.compressionAlgorithmsServerToClient, false);
		String kexAlgorithm = extractFirstMatch(serverList.kexAlgorithms, clientList.kexAlgorithms, true);
		String serverKeyExchangeAlgorithm = extractFirstMatch(serverList.serverHostKeyAlgorithms, clientList.serverHostKeyAlgorithms, true);

		return NegotiatedAlgorithmList.builder()
				.withCompressionAlgorithmsClientToServer(compressionClientToServer)
				.withCompressionAlgorithmsServerToClient(compressionServerToClient)
				.withEncryptionAlgorithmsClientToServer(clientToServerEncryption)
				.withEncryptionAlgorithmsServerToClient(serverToClientEncryption)
				.withKexAlgorithms(kexAlgorithm)
				.withLanguagesClientToServer(languageClientToServer)
				.withLanguagesServerToClient(languageServerToClient)
				.withMacAlgorithmsClientToServer(macClientToServer)
				.withMacAlgorithmsServerToClient(macServerToClient)
				.withServerKeyExchangeAlgorithm(serverKeyExchangeAlgorithm)
				.build();
	}

	private String extractFirstMatch(SshNamedList serverList, SshNamedList clientList, boolean required) {
		for (String serverElement : serverList.getElements()) {
			if (clientList.getElements().indexOf(serverElement) != -1) {
				return serverElement;
			}
		}
		if (required) {
			throw new RuntimeException("No common algorithm found");
		}
		return null;
	}
}
