package com.helospark.FakeSsh.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.helospark.FakeSsh.SshCommonState;
import com.helospark.FakeSsh.SshIdentificationExchanger;
import com.helospark.FakeSsh.SshState;
import com.helospark.FakeSsh.SshStateMachine;
import com.helospark.FakeSsh.io.SshDataExchangeService;
import com.helospark.FakeSsh.util.LoggerSupport;

@Configuration
public class StateMachineConfiguration {
	@Autowired
	private List<SshCommonState> commonStates;
	@Autowired
	@Qualifier("sshStates")
	private List<SshState> states;
	@Autowired
	private SshIdentificationExchanger identificationExchanger;
	@Autowired
	private LoggerSupport loggerSupport;
	@Autowired
	private SshDataExchangeService dataExchangeService;

	@Bean(name = "stateMachine")
	public SshStateMachine createStateMachine() {
		return new SshStateMachine(commonStates, states, identificationExchanger, loggerSupport, dataExchangeService);
	}

}