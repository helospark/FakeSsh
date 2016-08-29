package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.hostkey.ServerHostKeyAlgorithmProvider;
import com.helospark.FakeSsh.io.SshDataExchangeService;
import com.helospark.FakeSsh.kex.hash.SshHashFactory;

/**
 * Ssh state that exchange supported algorithms with the client.
 * @author helospark
 */
@Component(StateNames.SUPPORTED_ALGORITHM_EXCHANGE_STATE)
public class SshSupportedAlgorithmExchange implements SshState {
	private SshDataExchangeService dataExchangeService;
	private OurSupportedAlgorithmNegotiationListFactory ourSupportedAlgorithmNegotiationListFactory;
	private NegotitatedAlgorithmExtractor negotitatedAlgorithmExtractor;
	private SshHashFactory sshHashFactory;
	private ServerHostKeyAlgorithmProvider serverHostKeyAlgorithmProvider;

	@Autowired
	public SshSupportedAlgorithmExchange(SshDataExchangeService dataExchangeService, OurSupportedAlgorithmNegotiationListFactory ourSupportedAlgorithmNegotiationListFactory,
			NegotitatedAlgorithmExtractor negotitatedAlgorithmExtractor,
			SshHashFactory sshHashFactory, ServerHostKeyAlgorithmProvider serverHostKeyAlgorithmProvider) {
		this.dataExchangeService = dataExchangeService;
		this.ourSupportedAlgorithmNegotiationListFactory = ourSupportedAlgorithmNegotiationListFactory;
		this.negotitatedAlgorithmExtractor = negotitatedAlgorithmExtractor;
		this.sshHashFactory = sshHashFactory;
		this.serverHostKeyAlgorithmProvider = serverHostKeyAlgorithmProvider;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPackage) {
		try {
			AlgorithmNegotiationList remoteAlgorithmNegotiationList = readRemoteSupportedAlgorithmList(connection, previousPackage);
			AlgorithmNegotiationList ourAlgorithmNegotiationList = ourSupportedAlgorithmNegotiationListFactory.createAlgorithmNegotiationList();
			sendOurSupportedAlgorithmList(connection, ourAlgorithmNegotiationList);
			populateConnectionWithNegotiatedAlgorithms(connection, ourAlgorithmNegotiationList, remoteAlgorithmNegotiationList);
			return StateMachineResult.SUCCESS;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sendOurSupportedAlgorithmList(SshConnection connection, AlgorithmNegotiationList algorithmNegotiationList) throws IOException {
		byte[] localeKexMessage = algorithmNegotiationList.serialize();
		connection.setLocaleKexMessage(new SshString(localeKexMessage));
		dataExchangeService.sendPacket(connection, localeKexMessage);
	}

	private AlgorithmNegotiationList readRemoteSupportedAlgorithmList(SshConnection connection, byte[] data) throws IOException {
		connection.setRemoteKexMessage(new SshString(data));
		AlgorithmNegotiationList remoteAlgorithmNegotiationList = new AlgorithmNegotiationList(data);
		return remoteAlgorithmNegotiationList;
	}

	private void populateConnectionWithNegotiatedAlgorithms(SshConnection connection, AlgorithmNegotiationList localeAlgorithmNegotiationList, AlgorithmNegotiationList remoteAlgorithmNegotiationList) throws Exception {
		NegotiatedAlgorithmList negotiatedAlgoritms = negotitatedAlgorithmExtractor.extract(localeAlgorithmNegotiationList, remoteAlgorithmNegotiationList);
		connection.setNegotiatedAlgorithms(negotiatedAlgoritms);
		connection.setHashFunction(sshHashFactory.createForKeyExchangeMethod(negotiatedAlgoritms.getKexAlgorithm()));
		connection.setKeyProvider(serverHostKeyAlgorithmProvider.provideHostKeyAlgorithm(negotiatedAlgoritms.getServerKeyExchangeAlgorithm()));
	}

}
