package com.helospark.FakeSsh.channel.requesthandler;

import org.springframework.stereotype.Component;

@Component
public class ShellChannelRequestHandler implements ChannelRequestHandler {

	@Override
	public boolean handleMessage(byte[] packet) {
		return true;
	}

	@Override
	public boolean canHandle(String requestType) {
		return requestType.equals("shell");
	}

}
