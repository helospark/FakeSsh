package com.helospark.FakeSsh.io.write;

import java.io.IOException;

import com.helospark.FakeSsh.SshConnection;

public interface BinaryPacketSenderService {

	public abstract void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException;

}