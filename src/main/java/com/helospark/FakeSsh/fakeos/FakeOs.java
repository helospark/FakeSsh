package com.helospark.FakeSsh.fakeos;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.helospark.FakeSsh.fakeos.filesystem.FakeFilesystem;
import com.helospark.FakeSsh.fakeos.filesystem.FakeGroup;
import com.helospark.FakeSsh.fakeos.filesystem.FakeUser;

public class FakeOs {
	private String currentUser;
	private String currentDomain;
	private Map<String, String> environmentVariables = new HashMap<>();
	private FakeFilesystem fileSystem;
	private List<FakeProgram> currentlyRunningProgram = new ArrayList<>();
	private PipedOutputStream consolePipedOutput;
	private PipedInputStream consolePipedInput;
	private FakeShellExecutor shell;
	private List<FakeGroup> groups;
	private List<FakeUser> users;

	public FakeOs(Builder builder) {
		this.currentUser = builder.currentUser;
		this.currentDomain = builder.currentDomain;
		this.fileSystem = builder.fileSystem;
		this.consolePipedOutput = builder.consolePipedInput;
		this.consolePipedInput = builder.consolePipedOutput;
		this.groups = builder.groups;
		this.users = builder.users;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public String getCurrentDomain() {
		return currentDomain;
	}

	public Map<String, String> getEnvironmentVariables() {
		return environmentVariables;
	}

	public FakeFilesystem getFileSystem() {
		return fileSystem;
	}

	public OutputStream getConsolePipedOutput() {
		return consolePipedOutput;
	}

	public InputStream getConsolePipedInput() {
		return consolePipedInput;
	}

	public void addCurrentlyRunningProgram(FakeProgram programToAdd) {
		this.currentlyRunningProgram.add(programToAdd);
	}

	public void setShell(FakeShellExecutor shell) {
		this.shell = shell;
	}

	public void addEnvironmentVariable(String variableName, String variableValue) {
		environmentVariables.put(variableName, variableValue);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String currentUser;
		private String currentDomain;
		private FakeFilesystem fileSystem;
		private PipedOutputStream consolePipedInput;
		private PipedInputStream consolePipedOutput;
		private List<FakeGroup> groups = new ArrayList<>();
		private List<FakeUser> users = new ArrayList<>();

		public Builder withCurrentUser(String currentUser) {
			this.currentUser = currentUser;
			return this;
		}

		public Builder withCurrentDomain(String currentDomain) {
			this.currentDomain = currentDomain;
			return this;
		}

		public Builder withFileSystem(FakeFilesystem fileSystem) {
			this.fileSystem = fileSystem;
			return this;
		}

		public Builder withConsolePipedInput(PipedOutputStream consolePipedInput) {
			this.consolePipedInput = consolePipedInput;
			return this;
		}

		public Builder withConsolePipedOutput(PipedInputStream consolePipedOutput) {
			this.consolePipedOutput = consolePipedOutput;
			return this;
		}

		public Builder withGroups(List<FakeGroup> groups) {
			this.groups = groups;
			return this;
		}

		public Builder withUsers(List<FakeUser> users) {
			this.users = users;
			return this;
		}

		public FakeOs build() {
			return new FakeOs(this);
		}

	}

	public List<FakeGroup> getGroups() {
		return groups;
	}

	public List<FakeUser> getUsers() {
		return users;
	}

	public void setFileSystem(FakeFilesystem fileSystem) {
		this.fileSystem = fileSystem;
	}

}
