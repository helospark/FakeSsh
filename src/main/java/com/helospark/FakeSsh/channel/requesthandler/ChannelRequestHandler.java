package com.helospark.FakeSsh.channel.requesthandler;


public interface ChannelRequestHandler {

	public boolean handleMessage(byte[] packet);

	public boolean canHandle(String requestType);

}
