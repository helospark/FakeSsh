package com.helospark.FakeSsh.channel.requesthandler;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.channel.Channel;
import com.helospark.FakeSsh.domain.EnvChannelRequest;
import com.helospark.FakeSsh.util.LoggerSupport;

@Component
public class EnvChannelRequestHandler implements ChannelRequestHandler {
	private LoggerSupport loggerSupport;

	public EnvChannelRequestHandler(LoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	@Override
	public boolean handleMessage(Channel channel, byte[] packet) {
		try {
			EnvChannelRequest request = new EnvChannelRequest(packet);
			channel.getFakeOs().addEnvironmentVariable(request.getVariableName(), request.getVariableValue());
			loggerSupport.logInfoString("Set environmentVariable " + request.toString());
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canHandle(String requestType) {
		return requestType.equals("env");
	}

}
