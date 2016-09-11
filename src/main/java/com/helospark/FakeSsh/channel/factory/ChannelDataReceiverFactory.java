package com.helospark.FakeSsh.channel.factory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.channel.service.ChannelDataReceiver;

@Component
public class ChannelDataReceiverFactory {

	public ChannelDataReceiver create(PipedInputStream consoleInputStream) {
		try {
			PipedOutputStream pipedOutputStream = new PipedOutputStream(consoleInputStream);
			return new ChannelDataReceiver(pipedOutputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
