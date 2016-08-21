package com.helospark.FakeSsh;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.AlgorithmNegotiationNameList;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.domain.SshString;

@Component
public class SshSupportedAlgorithmExchange {
	private SshDataExchangeService dataExchangeService;
	private RandomNumberGenerator randomNumberGenerator;
	private NegotitatedAlgorithmExtractor negotitatedAlgorithmExtractor;
	private SshHashFactory sshHashFactory;
	private DiffieHellmanExchange next;

	@Autowired
	public SshSupportedAlgorithmExchange(SshDataExchangeService dataExchangeService, RandomNumberGenerator randomNumberGenerator,
			DiffieHellmanExchange diffieHellmanExchange, NegotitatedAlgorithmExtractor negotitatedAlgorithmExtractor,
			SshHashFactory sshHashFactory) {
		this.dataExchangeService = dataExchangeService;
		this.randomNumberGenerator = randomNumberGenerator;
		this.next = diffieHellmanExchange;
		this.negotitatedAlgorithmExtractor = negotitatedAlgorithmExtractor;
		this.sshHashFactory = sshHashFactory;
	}

	public void negotiateAlgorithm(SshConnection connection) {
		try {
			AlgorithmNegotiationList algorithmNegotiationList = createAlgorithmNegotiationList();
			byte[] localeKexMessage = algorithmNegotiationList.serialize();
			dataExchangeService.sendPacket(connection, localeKexMessage);
			byte[] remoteKexMessage = dataExchangeService.readPacket(connection);
			connection.setLocaleKexMessage(new SshString(localeKexMessage));
			connection.setRemoteKexMessage(new SshString(remoteKexMessage));
			AlgorithmNegotiationList remoteAlgorithmNegotiationList = new AlgorithmNegotiationList();
			remoteAlgorithmNegotiationList.deserialize(remoteKexMessage);
			NegotiatedAlgorithmList negotiatedAlgoritms = negotitatedAlgorithmExtractor.extract(algorithmNegotiationList, remoteAlgorithmNegotiationList);
			connection.setNegotiatedAlgorithms(negotiatedAlgoritms);
			connection.setHashFunction(sshHashFactory.createForKeyExchangeMethod(negotiatedAlgoritms.getKexAlgorithm()));
			next.process(connection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private AlgorithmNegotiationList createAlgorithmNegotiationList() {
		AlgorithmNegotiationList algorithmNegotiationList = new AlgorithmNegotiationList();
		algorithmNegotiationList.type = PacketType.SSH_MSG_KEXINIT;
		algorithmNegotiationList.cookie = randomNumberGenerator.generateRandomBytes(16);
		algorithmNegotiationList.reserved = 0;
		algorithmNegotiationList.firstKeyPacketFollow = (byte) 0;
		algorithmNegotiationList.kexAlgorithms = new AlgorithmNegotiationNameList(Collections.singletonList("diffie-hellman-group-exchange-sha1"));
		algorithmNegotiationList.serverHostKeyAlgorithms = new AlgorithmNegotiationNameList(Collections.singletonList("ssh-dss"));
		algorithmNegotiationList.encryptionAlgorithmsClientToServer = new AlgorithmNegotiationNameList(Arrays.asList("aes128-ctr"));
		algorithmNegotiationList.encryptionAlgorithmsServerToClient = new AlgorithmNegotiationNameList(Arrays.asList("aes128-ctr"));
		algorithmNegotiationList.macAlgorithmsClientToServer = new AlgorithmNegotiationNameList(Collections.singletonList("hmac-sha1"));
		algorithmNegotiationList.macAlgorithmsServerToClient = new AlgorithmNegotiationNameList(Collections.singletonList("hmac-sha1"));
		algorithmNegotiationList.compressionAlgorithmsClientToServer = new AlgorithmNegotiationNameList(Arrays.asList("none"));
		algorithmNegotiationList.compressionAlgorithmsServerToClient = new AlgorithmNegotiationNameList(Arrays.asList("none"));
		algorithmNegotiationList.languagesClientToServer = new AlgorithmNegotiationNameList(Collections.<String> emptyList());
		algorithmNegotiationList.languagesServerToClient = new AlgorithmNegotiationNameList(Collections.<String> emptyList());
		return algorithmNegotiationList;
	}
}
