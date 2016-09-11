package com.helospark.FakeSsh.fakeos;

public class OsStream {
	private OsInputStream standardInput;
	private OsOutputStream standardOutput;
	private OsOutputStream errorOutput;

	private OsStream(Builder builder) {
		this.standardInput = builder.standardInput;
		this.standardOutput = builder.standardOutput;
		this.errorOutput = builder.errorOutput;
	}

	public OsInputStream getStandardInput() {
		return standardInput;
	}

	public OsOutputStream getStandardOutput() {
		return standardOutput;
	}

	public OsOutputStream getErrorOutput() {
		return errorOutput;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private OsInputStream standardInput;
		private OsOutputStream standardOutput;
		private OsOutputStream errorOutput;

		public Builder withStandardInput(OsInputStream standardInput) {
			this.standardInput = standardInput;
			return this;
		}

		public Builder withStandardOutput(OsOutputStream standardOutput) {
			this.standardOutput = standardOutput;
			return this;
		}

		public Builder withErrorOutput(OsOutputStream errorOutput) {
			this.errorOutput = errorOutput;
			return this;
		}

		public OsStream build() {
			return new OsStream(this);
		}

	}

}
