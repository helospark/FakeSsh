package com.helospark.FakeSsh.channel.factory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.channel.service.ChannelDataSender;
import com.helospark.FakeSsh.io.SshDataExchangeService;

@Component
public class ChannelDataSenderFactory {
	private SshDataExchangeService dataExchangeService;

	@Autowired
	public ChannelDataSenderFactory(SshDataExchangeService dataExchangeService) {
		this.dataExchangeService = dataExchangeService;
	}

	public ChannelDataSender createAndRun(SshConnection connection, PipedOutputStream outputStream, int maxPacketSize, int channelId) {
		PipedInputStream pipedInputStream = createPipedInputStream(outputStream);
		ChannelDataSender channelDataSender = new ChannelDataSender(pipedInputStream, dataExchangeService, connection, maxPacketSize, channelId);
		channelDataSender.startDataSender();
		return channelDataSender;
	}

	private PipedInputStream createPipedInputStream(PipedOutputStream outputStream) {
		try {
			return new PipedInputStream(outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Unable to created piped output stream", e);
		}
	}

}
