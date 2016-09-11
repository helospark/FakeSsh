package com.helospark.FakeSsh.channel.requesthandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.channel.Channel;
import com.helospark.FakeSsh.domain.PtyChannelRequest;
import com.helospark.FakeSsh.util.LoggerSupport;

@Component
public class PtyChannelRequestHandler implements ChannelRequestHandler {
	private LoggerSupport loggerSupport;

	@Autowired
	public PtyChannelRequestHandler(LoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	@Override
	public boolean handleMessage(Channel channel, byte[] packet) {
		try {
			PtyChannelRequest ptyChannelRequest = new PtyChannelRequest(packet);
			channel.setTerminalType(ptyChannelRequest.getTerminalType());
			loggerSupport.logInfoString("Set pty " + ptyChannelRequest.toString());
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
