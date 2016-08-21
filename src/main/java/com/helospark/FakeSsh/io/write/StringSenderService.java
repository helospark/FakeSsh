package com.helospark.FakeSsh.io.write;

import static com.helospark.FakeSsh.ApplicationConstants.SSH_CHARSET;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.ReadWriteSocketConnection;
import com.helospark.FakeSsh.SshConnection;

/**
 * Sends plain strings to the given connection.
 * @author helospark
 */
@Component
public class StringSenderService {
	public void sendString(SshConnection connection, String stringToSend) throws IOException {
		ReadWriteSocketConnection readWriteSocket = connection.getConnection();
		byte[] bytes = stringToSend.getBytes(SSH_CHARSET);
		readWriteSocket.getOutputStream().write(bytes);
	}

}
