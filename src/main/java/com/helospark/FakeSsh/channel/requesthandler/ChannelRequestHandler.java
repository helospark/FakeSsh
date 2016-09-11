package com.helospark.FakeSsh.channel.requesthandler;

import com.helospark.FakeSsh.channel.Channel;

public interface ChannelRequestHandler {

	public boolean handleMessage(Channel channel, byte[] packet);

	public boolean canHandle(String requestType);

}
