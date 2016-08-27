package com.helospark.FakeSsh;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.cipher.SshCipherProvider;
import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.SshNamedList;
import com.helospark.FakeSsh.hmac.SshMacProvider;
import com.helospark.FakeSsh.hostkey.ServerHostKeyAlgorithmProvider;
import com.helospark.FakeSsh.util.RandomNumberGenerator;

/**
 * Factory to create our supported {@link AlgorithmNegotiationList}.
 * @author helospark
 */
@Component
public class OurSupportedAlgorithmNegotiationListFactory {
	private RandomNumberGenerator randomNumberGenerator;
	private ServerHostKeyAlgorithmProvider serverHostKeyAlgorithmProvider;
	private SshCipherProvider cipherProvider;
	private SshMacProvider sshMacProvider;

	@Autowired
	public OurSupportedAlgorithmNegotiationListFactory(RandomNumberGenerator randomNumberGenerator,
			ServerHostKeyAlgorithmProvider serverHostKeyAlgorithmProvider, SshCipherProvider cipherProvider,
			SshMacProvider sshMacProvider) {
		this.randomNumberGenerator = randomNumberGenerator;
		this.serverHostKeyAlgorithmProvider = serverHostKeyAlgorithmProvider;
		this.cipherProvider = cipherProvider;
		this.sshMacProvider = sshMacProvider;
	}

	public AlgorithmNegotiationList createAlgorithmNegotiationList() {
		AlgorithmNegotiationList algorithmNegotiationList = new AlgorithmNegotiationList();
		algorithmNegotiationList.type = PacketType.SSH_MSG_KEXINIT;
		algorithmNegotiationList.cookie = randomNumberGenerator.generateRandomBytes(16);
		algorithmNegotiationList.reserved = 0;
		algorithmNegotiationList.firstKeyPacketFollow = (byte) 0;
		algorithmNegotiationList.kexAlgorithms = new SshNamedList(Collections.singletonList("diffie-hellman-group-exchange-sha1"));
		algorithmNegotiationList.serverHostKeyAlgorithms = new SshNamedList(serverHostKeyAlgorithmProvider.getJoinedSupportedAlgorithmList());
		algorithmNegotiationList.encryptionAlgorithmsClientToServer = new SshNamedList(cipherProvider.getAvailableAlgorithmNames());
		algorithmNegotiationList.encryptionAlgorithmsServerToClient = new SshNamedList(cipherProvider.getAvailableAlgorithmNames());
		algorithmNegotiationList.macAlgorithmsClientToServer = new SshNamedList(sshMacProvider.getSupportedAlgorithms());
		algorithmNegotiationList.macAlgorithmsServerToClient = new SshNamedList(sshMacProvider.getSupportedAlgorithms());
		algorithmNegotiationList.compressionAlgorithmsClientToServer = new SshNamedList(Arrays.asList("none"));
		algorithmNegotiationList.compressionAlgorithmsServerToClient = new SshNamedList(Arrays.asList("none"));
		algorithmNegotiationList.languagesClientToServer = new SshNamedList(Collections.emptyList());
		algorithmNegotiationList.languagesServerToClient = new SshNamedList(Collections.emptyList());
		return algorithmNegotiationList;
	}
}
