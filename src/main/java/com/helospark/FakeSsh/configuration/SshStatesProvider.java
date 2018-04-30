package com.helospark.FakeSsh.configuration;

import java.util.ArrayList;
import java.util.List;

import com.helospark.FakeSsh.DiffieHellmanExchangeState;
import com.helospark.FakeSsh.SshChannelMessageHandlerState;
import com.helospark.FakeSsh.SshServiceRequestService;
import com.helospark.FakeSsh.SshState;
import com.helospark.FakeSsh.SshSupportedAlgorithmExchange;
import com.helospark.FakeSsh.SshUserAuthState;
import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Configuration;

@Configuration
public class SshStatesProvider {
	@Autowired
	private DiffieHellmanExchangeState diffieHellmanExchangeState;
	@Autowired
	private SshServiceRequestService sshServiceRequestService;
	@Autowired
	private SshSupportedAlgorithmExchange sshSupportedAlgorithmExchange;
	@Autowired
	private SshUserAuthState sshUserAuthState;
	@Autowired
	private SshChannelMessageHandlerState sshChannelMessageHandlerState;

	public List<SshState> sshStates() {
		List<SshState> states = new ArrayList<>();
		states.add(sshSupportedAlgorithmExchange);
		states.add(diffieHellmanExchangeState);
		states.add(sshServiceRequestService);
		states.add(sshUserAuthState);
		states.add(sshChannelMessageHandlerState);
		return states;
	}
}
