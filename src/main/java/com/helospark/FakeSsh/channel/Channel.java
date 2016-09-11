package com.helospark.FakeSsh.channel;

import com.helospark.FakeSsh.channel.service.ChannelDataReceiver;
import com.helospark.FakeSsh.channel.service.ChannelDataSender;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.fakeos.FakeOs;

public class Channel {
	private int channelId;
	private String terminalType;
	private ChannelDataSender channelDataSender;
	private ChannelDataReceiver channelDataReceiver;
	private FakeOs fakeOs; // Every channel will have a completely separate OS

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public void setChannelDataSender(ChannelDataSender channelDataSender) {
		this.channelDataSender = channelDataSender;
	}

	public void setFakeOs(FakeOs fakeOs) {
		this.fakeOs = fakeOs;
	}

	public int getChannelId() {
		return channelId;
	}

	public ChannelDataSender getChannelDataSender() {
		return channelDataSender;
	}

	public FakeOs getFakeOs() {
		return fakeOs;
	}

	public Object getConsoleInputPipedStream() {
		return fakeOs.getConsolePipedOutput();
	}

	public void setChannelDataReceiver(ChannelDataReceiver channelDataReceiver) {
		this.channelDataReceiver = channelDataReceiver;
	}

	public void sendDataToConsole(SshString data) {
		channelDataReceiver.handle(data);
	}

}
