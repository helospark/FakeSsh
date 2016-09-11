package com.helospark.FakeSsh.fakeos;

public class MutableCommand {
	private String command;

	public MutableCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
