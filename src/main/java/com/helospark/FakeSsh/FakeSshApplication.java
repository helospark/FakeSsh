package com.helospark.FakeSsh;

import java.io.IOException;

import com.helospark.lightdi.LightDi;
import com.helospark.lightdi.LightDiContext;

/**
 * Entry point for the FakeSSH application.
 * @author helospark
 */
public class FakeSshApplication {
	private static LightDiContext context;

	public static void main(String[] args) throws IOException {
		context = LightDi.initContextByClass(FakeSshConfiguration.class);
		FakeSshDaemon daemon = context.getBean(FakeSshDaemon.class);
		daemon.run();
	}
}
