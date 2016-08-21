package com.helospark.FakeSsh;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.AlgorithmNegotiationNameList;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;

@Component
public class NegotitatedAlgorithmExtractor {

	public NegotiatedAlgorithmList extract(AlgorithmNegotiationList serverList, AlgorithmNegotiationList clientList) {
		String clientToServerEncryption = extractFirstMatch(serverList.encryptionAlgorithmsClientToServer, clientList.encryptionAlgorithmsClientToServer, true);
		String serverToClientEncryption = extractFirstMatch(serverList.encryptionAlgorithmsServerToClient, clientList.encryptionAlgorithmsServerToClient, true);
		String languageClientToServer = extractFirstMatch(serverList.languagesClientToServer, clientList.languagesClientToServer, false);
		String languageServerToClient = extractFirstMatch(serverList.languagesServerToClient, clientList.languagesServerToClient, false);
		String macClientToServer = extractFirstMatch(serverList.macAlgorithmsClientToServer, clientList.macAlgorithmsClientToServer, true);
		String macServerToClient = extractFirstMatch(serverList.macAlgorithmsServerToClient, clientList.macAlgorithmsServerToClient, true);
		String compressionClientToServer = extractFirstMatch(serverList.compressionAlgorithmsClientToServer, clientList.compressionAlgorithmsClientToServer, false);
		String compressionServerToClient = extractFirstMatch(serverList.compressionAlgorithmsServerToClient, clientList.compressionAlgorithmsServerToClient, false);
		String kexAlgorithm = extractFirstMatch(serverList.kexAlgorithms, clientList.kexAlgorithms, true);

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
				.build();

	}

	private String extractFirstMatch(AlgorithmNegotiationNameList serverList, AlgorithmNegotiationNameList clientList, boolean required) {
		for (String clientElement : serverList.getElements()) {
			if (clientList.getElements().indexOf(clientElement) != -1) {
				return clientElement;
			}
		}
		if (required) {
			throw new RuntimeException("No common algorithm found");
		}
		return null;
	}
}
