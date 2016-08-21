package com.helospark.FakeSsh.configuration;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerSocketConfiguration {

	@Bean(name = "fakeSshServerSocket")
	public ServerSocket createServerSocket(@Value("${FAKE_SSH_PORT}") int serverPort) throws IOException {
		return new ServerSocket(serverPort);
	}
}
