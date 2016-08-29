package com.helospark.FakeSsh.channel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.channel.requesthandler.ChannelRequestHandler;
import com.helospark.FakeSsh.domain.ChannelRequest;
import com.helospark.FakeSsh.domain.ChannelRequestResult;
import com.helospark.FakeSsh.io.SshDataExchangeService;

@Component
public class ChannelRequestMessageHandler implements ChannelMessageHandler {
	private List<ChannelRequestHandler> channelRequestHandlers;
	private SshDataExchangeService dataExchangeService;

	@Autowired
	public ChannelRequestMessageHandler(List<ChannelRequestHandler> channelRequestHandlers, SshDataExchangeService dataExchangeService) {
		this.channelRequestHandlers = channelRequestHandlers;
		this.dataExchangeService = dataExchangeService;
	}

	@Override
	public void handleMessage(SshConnection connection, byte[] packet) throws IOException {
		ChannelRequest request = new ChannelRequest(packet);
		Optional<ChannelRequestHandler> handler = channelRequestHandlers.stream()
				.filter(h -> h.canHandle(request.getRequestType()))
				.findFirst();
		boolean result = handler.map(h -> h.handleMessage(packet)).orElse(false);
		if (request.isWantReply()) {
			ChannelRequestResult channelRequestResult = new ChannelRequestResult();
			channelRequestResult.setType(result ? PacketType.SSH_MSG_CHANNEL_SUCCESS : PacketType.SSH_MSG_CHANNEL_FAILURE);
			channelRequestResult.setRecepientChannel(request.getRecipientChannel());
			dataExchangeService.sendPacket(connection, channelRequestResult.serialize());
		}
	}

	@Override
	public boolean canHandle(PacketType type) {
		return type == PacketType.SSH_MSG_CHANNEL_REQUEST;
	}

}
