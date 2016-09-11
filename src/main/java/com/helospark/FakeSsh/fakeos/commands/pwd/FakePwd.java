package com.helospark.FakeSsh.fakeos.commands.pwd;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.CommandArguments;
import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.FakeProgram;
import com.helospark.FakeSsh.fakeos.OsStream;

@Component
public class FakePwd extends FakeProgram {

	@Override
	public int executeProgram(FakeOs os, OsStream osStream, CommandArguments arguments) {
		osStream.getStandardOutput()
				.writeLine(os.getFileSystem().getCurrentDirectory().toFullyQualifiedString());
		return 0;
	}

	@Override
	public boolean canHandle(String programName) {
		return programName.equals("pwd");
	}

}
