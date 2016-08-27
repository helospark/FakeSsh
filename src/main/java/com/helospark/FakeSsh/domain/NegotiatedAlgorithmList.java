package com.helospark.FakeSsh.domain;

public class NegotiatedAlgorithmList {
	private String kexAlgorithms;
	private String serverKeyExchangeAlgorithm;
	private String encryptionAlgorithmsClientToServer;
	private String encryptionAlgorithmsServerToClient;
	private String macAlgorithmsClientToServer;
	private String macAlgorithmsServerToClient;
	private String compressionAlgorithmsClientToServer;
	private String compressionAlgorithmsServerToClient;
	private String languagesClientToServer;
	private String languagesServerToClient;

	public NegotiatedAlgorithmList(Builder builder) {
		this.kexAlgorithms = builder.kexAlgorithms;
		this.encryptionAlgorithmsClientToServer = builder.encryptionAlgorithmsClientToServer;
		this.encryptionAlgorithmsServerToClient = builder.encryptionAlgorithmsServerToClient;
		this.macAlgorithmsClientToServer = builder.macAlgorithmsClientToServer;
		this.macAlgorithmsServerToClient = builder.macAlgorithmsServerToClient;
		this.compressionAlgorithmsClientToServer = builder.compressionAlgorithmsClientToServer;
		this.compressionAlgorithmsServerToClient = builder.compressionAlgorithmsServerToClient;
		this.languagesClientToServer = builder.languagesClientToServer;
		this.languagesServerToClient = builder.languagesServerToClient;
		this.serverKeyExchangeAlgorithm = builder.serverKeyExchangeAlgorithm;
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

	public String getServerKeyExchangeAlgorithm() {
		return serverKeyExchangeAlgorithm;
	}

	@Override
	public String toString() {
		return "NegotiatedAlgorithmList [kexAlgorithms=" + kexAlgorithms + ", serverKeyExchangeAlgorithm=" + serverKeyExchangeAlgorithm + ", encryptionAlgorithmsClientToServer=" + encryptionAlgorithmsClientToServer + ", encryptionAlgorithmsServerToClient="
				+ encryptionAlgorithmsServerToClient + ", macAlgorithmsClientToServer=" + macAlgorithmsClientToServer + ", macAlgorithmsServerToClient=" + macAlgorithmsServerToClient + ", compressionAlgorithmsClientToServer="
				+ compressionAlgorithmsClientToServer + ", compressionAlgorithmsServerToClient=" + compressionAlgorithmsServerToClient + ", languagesClientToServer=" + languagesClientToServer + ", languagesServerToClient=" + languagesServerToClient + "]";
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
		private String serverKeyExchangeAlgorithm;

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

		public Builder withServerKeyExchangeAlgorithm(String serverKeyExchangeAlgorithm) {
			this.serverKeyExchangeAlgorithm = serverKeyExchangeAlgorithm;
			return this;
		}

		public NegotiatedAlgorithmList build() {
			return new NegotiatedAlgorithmList(this);
		}

	}
}
