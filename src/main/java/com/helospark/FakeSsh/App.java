package com.helospark.FakeSsh;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Entry point for the FakeSSH application.
 * @author helospark
 */
public class App {
	private static ApplicationContext context;

	public static void main(String[] args) throws IOException {
		initializeDIFramework();
		FakeSshDaemon daemon = context.getBean(FakeSshDaemon.class);
		daemon.run();
	}

	private static void initializeDIFramework() {
		context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
	}
}
