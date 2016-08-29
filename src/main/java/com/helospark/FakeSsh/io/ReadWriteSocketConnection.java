package com.helospark.FakeSsh.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.helospark.FakeSsh.io.read.NoBlockingOnBufferedDataPushBackInputStream;

/**
 * Socket connection that can be read and written to.
 * @author helospark
 */
public class ReadWriteSocketConnection {
	private NoBlockingOnBufferedDataPushBackInputStream inputStream;
	private OutputStream outputStream;
	private Socket socket;

	public ReadWriteSocketConnection(Socket socket) throws IOException {
		inputStream = new NoBlockingOnBufferedDataPushBackInputStream(socket.getInputStream(), 2000);
		outputStream = socket.getOutputStream();
		this.socket = socket;
	}

	public NoBlockingOnBufferedDataPushBackInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(NoBlockingOnBufferedDataPushBackInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Close the socket and all attached streams.
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// Ignore
		}
	}

}
