package com.helospark.FakeSsh;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.AlgorithmNegotiationList;
import com.helospark.FakeSsh.domain.SshNamedList;

/**
 * Factory to create our supported {@link AlgorithmNegotiationList}.
 * @author helospark
 */
@Component
public class OurSupportedAlgorithmNegotiationListFactory {
	private RandomNumberGenerator randomNumberGenerator;

	@Autowired
	public OurSupportedAlgorithmNegotiationListFactory(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public AlgorithmNegotiationList createAlgorithmNegotiationList() {
		AlgorithmNegotiationList algorithmNegotiationList = new AlgorithmNegotiationList();
		algorithmNegotiationList.type = PacketType.SSH_MSG_KEXINIT;
		algorithmNegotiationList.cookie = randomNumberGenerator.generateRandomBytes(16);
		algorithmNegotiationList.reserved = 0;
		algorithmNegotiationList.firstKeyPacketFollow = (byte) 0;
		algorithmNegotiationList.kexAlgorithms = new SshNamedList(Collections.singletonList("diffie-hellman-group-exchange-sha1"));
		algorithmNegotiationList.serverHostKeyAlgorithms = new SshNamedList(Collections.singletonList("ssh-dss"));
		algorithmNegotiationList.encryptionAlgorithmsClientToServer = new SshNamedList(Arrays.asList("aes128-ctr"));
		algorithmNegotiationList.encryptionAlgorithmsServerToClient = new SshNamedList(Arrays.asList("aes128-ctr"));
		algorithmNegotiationList.macAlgorithmsClientToServer = new SshNamedList(Collections.singletonList("hmac-sha1"));
		algorithmNegotiationList.macAlgorithmsServerToClient = new SshNamedList(Collections.singletonList("hmac-sha1"));
		algorithmNegotiationList.compressionAlgorithmsClientToServer = new SshNamedList(Arrays.asList("none"));
		algorithmNegotiationList.compressionAlgorithmsServerToClient = new SshNamedList(Arrays.asList("none"));
		algorithmNegotiationList.languagesClientToServer = new SshNamedList(Collections.emptyList());
		algorithmNegotiationList.languagesServerToClient = new SshNamedList(Collections.emptyList());
		return algorithmNegotiationList;
	}
}
