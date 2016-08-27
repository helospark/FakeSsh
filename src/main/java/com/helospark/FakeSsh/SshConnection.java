package com.helospark.FakeSsh;

import java.util.Optional;

import com.helospark.FakeSsh.cipher.SshCipher;
import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.hmac.SshMac;
import com.helospark.FakeSsh.hostkey.ServerHostKeyAlgorithm;
import com.helospark.FakeSsh.kex.hash.SshHash;

/**
 * Represents all the states that a given SSH connection needs.
 * Note: Some of the fields are initially null or empty and are filled 
 * out throught the initialization process
 * @author helospark
 */
public class SshConnection {
	private ReadWriteSocketConnection connection;
	private SshString localeIdentificationMessage;
	private SshString remoteIdentificationMessage;
	private SshString remoteKexMessage;
	private SshString localeKexMessage;
	private byte[] hash;
	private byte[] sessionId;
	private MpInt key;
	private SshHash hashFunction;
	private NegotiatedAlgorithmList negotiatedAlgoritms;
	private Optional<SshCipher> clientToServerCipher = Optional.empty();
	private Optional<SshCipher> serverToClientCipher = Optional.empty();
	private Optional<SshMac> clientToServerMac = Optional.empty();
	private Optional<SshMac> serverToClientMac = Optional.empty();
	private ServerHostKeyAlgorithm serverHostKeyAlgorithm;
	private int numberOfSentPackeges = 0;
	private int numberOfReceivedPackages = 0;
	private boolean connectionClosed = false;

	public ReadWriteSocketConnection getConnection() {
		return connection;
	}

	public void setConnection(ReadWriteSocketConnection connection) {
		this.connection = connection;
	}

	public SshString getRemoteKexMessage() {
		return remoteKexMessage;
	}

	public void setRemoteKexMessage(SshString remoteKexMessage) {
		this.remoteKexMessage = remoteKexMessage;
	}

	public SshString getLocaleKexMessage() {
		return localeKexMessage;
	}

	public void setLocaleKexMessage(SshString localeKexMessage) {
		this.localeKexMessage = localeKexMessage;
	}

	public SshString getLocaleIdentificationMessage() {
		return localeIdentificationMessage;
	}

	public void setLocaleIdentificationMessage(SshString localeIdentificationMessage) {
		this.localeIdentificationMessage = localeIdentificationMessage;
	}

	public SshString getRemoteIdentificationMessage() {
		return remoteIdentificationMessage;
	}

	public void setRemoteIdentificationMessage(SshString remoteIdentificationMessage) {
		this.remoteIdentificationMessage = remoteIdentificationMessage;
	}

	public void setKey(MpInt k) {
		this.key = k;
	}

	public void setHash(byte[] correctedHash) {
		this.hash = correctedHash;
	}

	public byte[] getHash() {
		return hash;
	}

	public MpInt getKey() {
		return key;
	}

	public void setSessionId(byte[] sessionId) {
		this.sessionId = sessionId;
	}

	public byte[] getSessionId() {
		return sessionId;
	}

	public SshHash getHashFunction() {
		return hashFunction;
	}

	public void setNegotiatedAlgorithms(NegotiatedAlgorithmList negotiatedAlgoritms) {
		this.negotiatedAlgoritms = negotiatedAlgoritms;
	}

	public NegotiatedAlgorithmList getNegotiatedAlgoritms() {
		return negotiatedAlgoritms;
	}

	public Optional<SshCipher> getServerToClientCipher() {
		return serverToClientCipher;
	}

	public void setServerToClientCipher(SshCipher serverToClientCipher) {
		this.serverToClientCipher = Optional.of(serverToClientCipher);
	}

	public Optional<SshMac> getServerToClientMac() {
		return serverToClientMac;
	}

	public void setServerToClientMac(SshMac serverToClientMac) {
		this.serverToClientMac = Optional.of(serverToClientMac);
	}

	public Optional<SshCipher> getClientToServerCipher() {
		return clientToServerCipher;
	}

	public void setClientToServerCipher(SshCipher clientToServerCipher) {
		this.clientToServerCipher = Optional.of(clientToServerCipher);
	}

	public Optional<SshMac> getClientToServerMac() {
		return clientToServerMac;
	}

	public void setClientToServerMac(SshMac clientToServerMac) {
		this.clientToServerMac = Optional.of(clientToServerMac);
	}

	public void setHashFunction(SshHash hashFunction) {
		this.hashFunction = hashFunction;
	}

	public int getNumberOfReceivedPackages() {
		return numberOfReceivedPackages;
	}

	public int getNumberOfSentPackages() {
		return numberOfSentPackeges;
	}

	public void incrementNumberOfReceivedPackages() {
		++numberOfReceivedPackages;
	}

	public void incrementNumberOfSentPackages() {
		++numberOfSentPackeges;
	}

	public boolean isConnectionClosed() {
		return connectionClosed;
	}

	public void setConnectionClosed(boolean connectionClosed) {
		this.connectionClosed = connectionClosed;
	}

	public ServerHostKeyAlgorithm getKeyProvider() {
		return serverHostKeyAlgorithm;
	}

	public void setKeyProvider(ServerHostKeyAlgorithm serverHostKeyAlgorithm) {
		this.serverHostKeyAlgorithm = serverHostKeyAlgorithm;
	}

	public String getRemoteIpAsString() {
		return Optional.ofNullable(connection)
				.map(connection -> connection.getSocket().getInetAddress().getHostAddress())
				.orElse("N/A");
	}
}
