package com.helospark.FakeSsh;

import java.io.IOException;
import java.util.List;

import com.helospark.FakeSsh.io.SshDataExchangeService;
import com.helospark.FakeSsh.util.LoggerSupport;

public class StateMachineHandler {
	private List<SshCommonState> commonStates;
	private List<SshState> states;
	private SshIdentificationExchanger identificationExchanger;
	private LoggerSupport loggerSupport;
	private SshDataExchangeService dataExchangeService;

	public StateMachineHandler(List<SshCommonState> commonStates, List<SshState> states, SshIdentificationExchanger identificationExchanger, LoggerSupport loggerSupport, SshDataExchangeService dataExchangeService) {
		this.commonStates = commonStates;
		this.states = states;
		this.identificationExchanger = identificationExchanger;
		this.loggerSupport = loggerSupport;
		this.dataExchangeService = dataExchangeService;
	}

	public void handle(SshConnection connection) throws IOException {
		try {
			doWork(connection);
		} catch (ConnectionClosedException e) {
			loggerSupport.logDebugString("Connection was unexpectedly closed with " + connection.getRemoteIpAsString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			handleClosedConnection(connection);
		}
	}

	private void doWork(SshConnection connection) throws IOException {
		identificationExchanger.exchangeIdentification(connection);
		StateMachineState stateMachineState = new StateMachineState();
		while (stateMachineState.isRunning) {
			SshState currentState = states.get(stateMachineState.currentState);
			byte[] packet = readPacket(connection);
			StateMachineResult result = handleCommonState(connection, packet);
			if (result == StateMachineResult.NOT_HANDLED) {
				StateMachineResult stateMachineResult = currentState.enterState(connection, packet);
				stateMachineState = handleResult(stateMachineState, stateMachineResult);
			}
		}
	}

	private StateMachineState handleResult(StateMachineState stateMachineState, StateMachineResult stateMachineResult) {
		switch (stateMachineResult) {
		case CLOSED:
			stateMachineState.isRunning = false;
			break;
		case SUCCESS:
			++stateMachineState.currentState;
		case REPEAT_STATE:
			break;
		case NOT_HANDLED:
			throw new RuntimeException("Unable to handle packet");
		}
		return stateMachineState;
	}

	private void handleClosedConnection(SshConnection connection) {
		connection.getConnection().close();
		connection.setConnectionClosed(true);
		String ip = connection.getRemoteIpAsString();
		loggerSupport.logInfoString("Connection was closed with " + ip);
	}

	private StateMachineResult handleCommonState(SshConnection connection, byte[] packet) {
		return commonStates.stream()
				.filter(state -> state.canHandle(packet))
				.findFirst()
				.map(state -> state.enterState(connection, packet))
				.orElse(StateMachineResult.NOT_HANDLED);
	}

	private byte[] readPacket(SshConnection connection) throws IOException {
		return dataExchangeService.readPacket(connection);
	}

	static class StateMachineState {
		public boolean isRunning = true;
		public int currentState = 0;
	}
}
