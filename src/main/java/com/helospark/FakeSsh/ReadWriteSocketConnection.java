package com.helospark.FakeSsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket connection that can be read and written to.
 * @author helospark
 */
public class ReadWriteSocketConnection {
	private InputStream inputStream;
	private OutputStream outputStream;
	private Socket socket;

	public ReadWriteSocketConnection(Socket socket) throws IOException {
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
		this.socket = socket;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
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
