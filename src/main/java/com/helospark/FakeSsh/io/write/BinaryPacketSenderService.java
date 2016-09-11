package com.helospark.FakeSsh.io.write;

import java.io.IOException;

import com.helospark.FakeSsh.SshConnection;

/**
 * Send a binary packet to the given connection.
 * Assembles the packet from the given payload, optionally encrypts, compresses it
 * @author helospark
 */
public interface BinaryPacketSenderService {

	public abstract void sendPacket(SshConnection connection, byte[] bytesToSend) throws IOException;

}