package com.helospark.FakeSsh.channel.requesthandler;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.EnvChannelRequest;

@Component
public class EnvChannelRequestHandler implements ChannelRequestHandler {

	@Override
	public boolean handleMessage(byte[] packet) {
		try {
			EnvChannelRequest request = new EnvChannelRequest(packet);
			System.out.println(request);
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
