package com.helospark.FakeSsh.channel.requesthandler;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.PtyChannelRequest;

@Component
public class PtyChannelRequestHandler implements ChannelRequestHandler {

	@Override
	public boolean handleMessage(byte[] packet) {
		try {
			PtyChannelRequest ptyChannelRequest = new PtyChannelRequest(packet);
			System.out.println(ptyChannelRequest);
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canHandle(String requestType) {
		return requestType.equals("pty-req");
	}

}
