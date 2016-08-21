package com.helospark.FakeSsh.domain;

public class NegotiatedAlgorithmList {
	private String kexAlgorithms;
	private String encryptionAlgorithmsClientToServer;
	private String encryptionAlgorithmsServerToClient;
	private String macAlgorithmsClientToServer;
	private String macAlgorithmsServerToClient;
	private String compressionAlgorithmsClientToServer;
	private String compressionAlgorithmsServerToClient;
	private String languagesClientToServer;
	private String languagesServerToClient;

	public NegotiatedAlgorithmList(Builder builder) {
		kexAlgorithms = builder.kexAlgorithms;
		encryptionAlgorithmsClientToServer = builder.encryptionAlgorithmsClientToServer;
		encryptionAlgorithmsServerToClient = builder.encryptionAlgorithmsServerToClient;
		macAlgorithmsClientToServer = builder.macAlgorithmsClientToServer;
		macAlgorithmsServerToClient = builder.macAlgorithmsServerToClient;
		compressionAlgorithmsClientToServer = builder.compressionAlgorithmsClientToServer;
		compressionAlgorithmsServerToClient = builder.compressionAlgorithmsServerToClient;
		languagesClientToServer = builder.languagesClientToServer;
		languagesServerToClient = builder.languagesServerToClient;
	}

	public String getKexAlgorithm() {
		return kexAlgorithms;
	}

	public String getEncryptionAlgorithmsClientToServer() {
		return encryptionAlgorithmsClientToServer;
	}

	public String getEncryptionAlgorithmsServerToClient() {
		return encryptionAlgorithmsServerToClient;
	}

	public String getMacAlgorithmsClientToServer() {
		return macAlgorithmsClientToServer;
	}

	public String getMacAlgorithmsServerToClient() {
		return macAlgorithmsServerToClient;
	}

	public String getCompressionAlgorithmsClientToServer() {
		return compressionAlgorithmsClientToServer;
	}

	public String getCompressionAlgorithmsServerToClient() {
		return compressionAlgorithmsServerToClient;
	}

	public String getLanguagesClientToServer() {
		return languagesClientToServer;
	}

	public String getLanguagesServerToClient() {
		return languagesServerToClient;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String toString() {
		return "NegotiatedAlgorithmList [kexAlgorithms=" + kexAlgorithms + ", encryptionAlgorithmsClientToServer=" + encryptionAlgorithmsClientToServer + ", encryptionAlgorithmsServerToClient=" + encryptionAlgorithmsServerToClient
				+ ", macAlgorithmsClientToServer=" + macAlgorithmsClientToServer + ", macAlgorithmsServerToClient=" + macAlgorithmsServerToClient + ", compressionAlgorithmsClientToServer=" + compressionAlgorithmsClientToServer
				+ ", compressionAlgorithmsServerToClient=" + compressionAlgorithmsServerToClient + ", languagesClientToServer=" + languagesClientToServer + ", languagesServerToClient=" + languagesServerToClient + "]";
	}

	public static class Builder {
		private String kexAlgorithms;
		private String encryptionAlgorithmsClientToServer;
		private String encryptionAlgorithmsServerToClient;
		private String macAlgorithmsClientToServer;
		private String macAlgorithmsServerToClient;
		private String compressionAlgorithmsClientToServer;
		private String compressionAlgorithmsServerToClient;
		private String languagesClientToServer;
		private String languagesServerToClient;

		public Builder withKexAlgorithms(String kexAlgorithms) {
			this.kexAlgorithms = kexAlgorithms;
			return this;
		}

		public Builder withEncryptionAlgorithmsClientToServer(String encryptionAlgorithmsClientToServer) {
			this.encryptionAlgorithmsClientToServer = encryptionAlgorithmsClientToServer;
			return this;
		}

		public Builder withEncryptionAlgorithmsServerToClient(String encryptionAlgorithmsServerToClient) {
			this.encryptionAlgorithmsServerToClient = encryptionAlgorithmsServerToClient;
			return this;
		}

		public Builder withMacAlgorithmsClientToServer(String macAlgorithmsClientToServer) {
			this.macAlgorithmsClientToServer = macAlgorithmsClientToServer;
			return this;
		}

		public Builder withMacAlgorithmsServerToClient(String macAlgorithmsServerToClient) {
			this.macAlgorithmsServerToClient = macAlgorithmsServerToClient;
			return this;
		}

		public Builder withCompressionAlgorithmsClientToServer(String compressionAlgorithmsClientToServer) {
			this.compressionAlgorithmsClientToServer = compressionAlgorithmsClientToServer;
			return this;
		}

		public Builder withCompressionAlgorithmsServerToClient(String compressionAlgorithmsServerToClient) {
			this.compressionAlgorithmsServerToClient = compressionAlgorithmsServerToClient;
			return this;
		}

		public Builder withLanguagesClientToServer(String languagesClientToServer) {
			this.languagesClientToServer = languagesClientToServer;
			return this;
		}

		public Builder withLanguagesServerToClient(String languagesServerToClient) {
			this.languagesServerToClient = languagesServerToClient;
			return this;
		}

		public NegotiatedAlgorithmList build() {
			return new NegotiatedAlgorithmList(this);
		}

	}
}
