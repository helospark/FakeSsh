package com.helospark.FakeSsh.fakeos.commands.cd;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.CommandArguments;
import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.FakeProgram;
import com.helospark.FakeSsh.fakeos.OsStream;
import com.helospark.FakeSsh.fakeos.filesystem.FakeFilesystem;

@Component
public class FakeCd extends FakeProgram {

	@Override
	public int executeProgram(FakeOs os, OsStream osStream, CommandArguments arguments) {
		FakeFilesystem fileSystem = os.getFileSystem();
		if (arguments.getParameters().size() == 0) {
			goToHomeDirectory(fileSystem);
		} else {
			fileSystem.goToDirectory(arguments.getParameters().get(0));
		}

		return 0;
	}

	private void goToHomeDirectory(FakeFilesystem fileSystem) {
		fileSystem.goToDirectory("~");
	}

	@Override
	public boolean canHandle(String programName) {
		return programName.equals("cd");
	}

}
