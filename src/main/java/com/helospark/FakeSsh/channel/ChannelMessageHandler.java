package com.helospark.FakeSsh.channel;

import java.io.IOException;

import com.helospark.FakeSsh.PacketType;
import com.helospark.FakeSsh.SshConnection;

public interface ChannelMessageHandler {

	public void handleMessage(SshConnection connection, byte[] packet) throws IOException;

	public boolean canHandle(PacketType type);
}
