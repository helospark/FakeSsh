package com.helospark.FakeSsh.io.read;

import java.io.IOException;

import com.helospark.FakeSsh.SshConnection;

public interface BinaryPacketReaderService {

	/**
	 * Reads a packet from the given connection.
	 * @param connection to read packet from
	 * @return decrypted bytes for the packet
	 * @throws IOException
	 */
	public abstract byte[] readPacket(SshConnection connection) throws IOException;

}