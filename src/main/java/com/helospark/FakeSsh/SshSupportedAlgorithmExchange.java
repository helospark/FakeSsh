package com.helospark.FakeSsh;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.domain.SshString;

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
	private SshState next;

	@Autowired
	public SshSupportedAlgorithmExchange(SshDataExchangeService dataExchangeService, OurSupportedAlgorithmNegotiationListFactory ourSupportedAlgorithmNegotiationListFactory,
			@Qualifier(StateNames.DIFFIE_HELLMAN_EXHCANGE_STATE) SshState diffieHellmanExchange, NegotitatedAlgorithmExtractor negotitatedAlgorithmExtractor,
			SshHashFactory sshHashFactory) {
		this.dataExchangeService = dataExchangeService;
		this.ourSupportedAlgorithmNegotiationListFactory = ourSupportedAlgorithmNegotiationListFactory;
		this.next = diffieHellmanExchange;
		this.negotitatedAlgorithmExtractor = negotitatedAlgorithmExtractor;
		this.sshHashFactory = sshHashFactory;
	}

	@Override
	public void enterState(SshConnection connection) {
		try {
			AlgorithmNegotiationList algorithmNegotiationList = ourSupportedAlgorithmNegotiationListFactory.createAlgorithmNegotiationList();
			sendOurSupportedAlgorithmList(connection, algorithmNegotiationList);
			AlgorithmNegotiationList remoteAlgorithmNegotiationList = readRemoteSupportedAlgorithmList(connection);
			populateConnectionWithNegotiatedAlgorithms(connection, algorithmNegotiationList, remoteAlgorithmNegotiationList);
			next.enterState(connection);
		} catch (ConnectionClosedException e) {
			return;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sendOurSupportedAlgorithmList(SshConnection connection, AlgorithmNegotiationList algorithmNegotiationList) throws IOException {
		byte[] localeKexMessage = algorithmNegotiationList.serialize();
		connection.setLocaleKexMessage(new SshString(localeKexMessage));
		dataExchangeService.sendPacket(connection, localeKexMessage);
	}

	private AlgorithmNegotiationList readRemoteSupportedAlgorithmList(SshConnection connection) throws IOException {
		byte[] remoteKexMessage = dataExchangeService.readPacket(connection);
		connection.setRemoteKexMessage(new SshString(remoteKexMessage));
		AlgorithmNegotiationList remoteAlgorithmNegotiationList = new AlgorithmNegotiationList(remoteKexMessage);
		return remoteAlgorithmNegotiationList;
	}

	private void populateConnectionWithNegotiatedAlgorithms(SshConnection connection, AlgorithmNegotiationList localeAlgorithmNegotiationList, AlgorithmNegotiationList remoteAlgorithmNegotiationList) throws NoSuchAlgorithmException {
		NegotiatedAlgorithmList negotiatedAlgoritms = negotitatedAlgorithmExtractor.extract(localeAlgorithmNegotiationList, remoteAlgorithmNegotiationList);
		connection.setNegotiatedAlgorithms(negotiatedAlgoritms);
		connection.setHashFunction(sshHashFactory.createForKeyExchangeMethod(negotiatedAlgoritms.getKexAlgorithm()));
	}

}
