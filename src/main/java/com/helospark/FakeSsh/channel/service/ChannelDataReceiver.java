package com.helospark.FakeSsh.channel.service;

import java.io.IOException;
import java.io.PipedOutputStream;

import com.helospark.FakeSsh.domain.SshString;

public class ChannelDataReceiver {
	private PipedOutputStream pipedOutputStream;

	public ChannelDataReceiver(PipedOutputStream pipedOutputStream) {
		this.pipedOutputStream = pipedOutputStream;
	}

	public void handle(SshString data) {
		try {
			pipedOutputStream.write(data.getData());
			pipedOutputStream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
