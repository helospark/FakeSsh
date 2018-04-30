package com.helospark.FakeSsh.configuration;

import java.util.List;

import com.helospark.FakeSsh.SshCommonState;
import com.helospark.FakeSsh.SshIdentificationExchanger;
import com.helospark.FakeSsh.StateMachineHandler;
import com.helospark.FakeSsh.io.SshDataExchangeService;
import com.helospark.FakeSsh.util.LoggerSupport;
import com.helospark.lightdi.annotation.Bean;
import com.helospark.lightdi.annotation.Configuration;

@Configuration
public class StateMachineConfiguration {
    private List<SshCommonState> commonStates;
    private SshStatesProvider sshStatesProvider;
    private SshIdentificationExchanger identificationExchanger;
    private LoggerSupport loggerSupport;
    private SshDataExchangeService dataExchangeService;

    // LightDi 0.0.3 will support qualified collections
    public StateMachineConfiguration(List<SshCommonState> commonStates, SshStatesProvider sshStatesProvider,
            SshIdentificationExchanger identificationExchanger,
            LoggerSupport loggerSupport, SshDataExchangeService dataExchangeService) {
        this.commonStates = commonStates;
        this.sshStatesProvider = sshStatesProvider;
        this.identificationExchanger = identificationExchanger;
        this.loggerSupport = loggerSupport;
        this.dataExchangeService = dataExchangeService;
    }

    @Bean(name = "stateMachine")
    public StateMachineHandler createStateMachine() {
        return new StateMachineHandler(commonStates, sshStatesProvider.sshStates(), identificationExchanger, loggerSupport, dataExchangeService);
    }

}