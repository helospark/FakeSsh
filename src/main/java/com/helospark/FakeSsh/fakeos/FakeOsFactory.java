package com.helospark.FakeSsh.fakeos;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.SshConnection;
import com.helospark.FakeSsh.channel.Channel;
import com.helospark.FakeSsh.fakeos.filesystem.FakeFilesystemFactory;
import com.helospark.FakeSsh.fakeos.filesystem.FakeGroup;
import com.helospark.FakeSsh.fakeos.filesystem.FakeUser;

@Component
public class FakeOsFactory {
	private FakeFilesystemFactory fakeFilesystemFactory;

	@Autowired
	public FakeOsFactory(FakeFilesystemFactory fakeFilesystemFactory) {
		this.fakeFilesystemFactory = fakeFilesystemFactory;
	}

	public FakeOs createFakeOs(SshConnection sshConnection, Channel channel, PipedOutputStream pipedInput, PipedInputStream pipedOutput) {
		FakeOs fakeOs = FakeOs.builder()
				.withCurrentDomain("localhost")
				.withCurrentUser("root")
				.withConsolePipedInput(pipedInput)
				.withConsolePipedOutput(pipedOutput)
				.withUsers(Collections.singletonList(new FakeUser("root")))
				.withGroups(Collections.singletonList(new FakeGroup("root")))
				.build();
		fakeOs.setFileSystem(fakeFilesystemFactory.create(fakeOs));
		fakeOs.addEnvironmentVariable("PATH", "/bin:/usr/bin");
		return fakeOs;
	}
}
