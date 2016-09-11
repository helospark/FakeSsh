package com.helospark.FakeSsh.channel.requesthandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.channel.Channel;
import com.helospark.FakeSsh.channel.requesthandler.factory.FakeShellExecutorFactory;

@Component
public class ShellChannelRequestHandler implements ChannelRequestHandler {
	private FakeShellExecutorFactory fakeShellExecutorFactory;

	@Autowired
	public ShellChannelRequestHandler(FakeShellExecutorFactory fakeShellExecutorFactory) {
		this.fakeShellExecutorFactory = fakeShellExecutorFactory;
	}

	@Override
	public boolean handleMessage(Channel channel, byte[] packet) {
		channel.getFakeOs()
				.setShell(fakeShellExecutorFactory.createAndExecute(channel.getFakeOs()));
		return true;
	}

	@Override
	public boolean canHandle(String requestType) {
		return requestType.equals("shell");
	}

}
