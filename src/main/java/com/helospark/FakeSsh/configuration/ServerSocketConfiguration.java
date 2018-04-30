package com.helospark.FakeSsh.configuration;

import java.io.IOException;
import java.net.ServerSocket;

import com.helospark.lightdi.annotation.Value;
import com.helospark.lightdi.annotation.Bean;
import com.helospark.lightdi.annotation.Configuration;

@Configuration
public class ServerSocketConfiguration {

	@Bean(name = "fakeSshServerSocket")
	public ServerSocket createServerSocket(@Value("${FAKE_SSH_PORT}") int serverPort) throws IOException {
		return new ServerSocket(serverPort);
	}
}
