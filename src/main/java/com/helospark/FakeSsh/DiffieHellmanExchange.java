package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.DhGexInit;
import com.helospark.FakeSsh.domain.DhGexReply;
import com.helospark.FakeSsh.domain.DhGexResponse;
import com.helospark.FakeSsh.domain.DhKeySize;
import com.helospark.FakeSsh.domain.GeneratedPrime;
import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.NegotiatedAlgorithmList;
import com.helospark.FakeSsh.domain.SshString;

@Component
public class DiffieHellmanExchange {
	private SshDataExchangeService dataExchangeService;
	private SafePrimeProvider safePrimeProvider;
	private HashFunction hashFunction;
	private PublicKeyProvider publicKeyProvider;
	private DssSignatureService dssSignatureService;
	private DiffieHellmanHashService diffieHellmanHashService;
	private SshCipherFactory sshCipherFactory;
	private SshMacFactory sshMacFactory;
	private SshServiceRequestService next;

	@Autowired
	public DiffieHellmanExchange(SshDataExchangeService dataExchangeService, SafePrimeProvider safePrimeProvider,
			HashFunction hashFunction, PublicKeyProvider publicKeyProvider, DssSignatureService dssSignatureService, DiffieHellmanHashService diffieHellmanHashService,
			SshCipherFactory sshCipherFactory, SshMacFactory sshMacFactory, SshServiceRequestService next) {
		this.dataExchangeService = dataExchangeService;
		this.safePrimeProvider = safePrimeProvider;
		this.hashFunction = hashFunction;
		this.publicKeyProvider = publicKeyProvider;
		this.dssSignatureService = dssSignatureService;
		this.diffieHellmanHashService = diffieHellmanHashService;
		this.sshCipherFactory = sshCipherFactory;
		this.sshMacFactory = sshMacFactory;
		this.next = next;
	}

	public void process(SshConnection connection) {
		try {
			DhKeySize dhKeySize = readDhKeySize(connection);
			GeneratedPrime prime = safePrimeProvider.providePrime(dhKeySize.getMinimumLength(), dhKeySize.getPreferredLength(), dhKeySize.getMaximumLength());
			DhGexResponse dhGexResponse = createDhGexResponse(prime);
			sendSafePrime(connection, dhGexResponse);
			DhGexInit dhGexInit = readDhGexInit(connection);

			BigInteger y = new BigInteger(prime.getBitLength() - 1, new SecureRandom());
			MpInt f = new MpInt(prime.getGenerator().modPow(y, prime.getPrime()));
			MpInt k = new MpInt(dhGexInit.getE().getBigInteger().modPow(y, prime.getPrime()));

			SshString hash = calculateHash(connection, dhKeySize, dhGexResponse, dhGexInit, f, k);

			DhGexReply dhGexReply = new DhGexReply();
			dhGexReply.setPacketType(PacketType.SSH_MSG_KEX_DH_GEX_REPLY);
			dhGexReply.setPublicKey(dssSignatureService.providePublicKey());
			dhGexReply.setF(f);

			dhGexReply.setHash(signHash(hash));

			connection.setKey(k);
			connection.setHash(hash);
			connection.setSessionId(hash);

			dumpBigInteger(k.getBigInteger().toByteArray(), "shared key");
			dumpBigInteger(publicKeyProvider.provide().serialize(), "Public key");

			dataExchangeService.sendPacket(connection, dhGexReply.serialize());

			dataExchangeService.sendPacket(connection, new byte[] { PacketType.SSH_MSG_NEWKEYS.getValue() });

			byte[] newKeyResult = dataExchangeService.readPacket(connection);
			if (PacketType.fromValue(newKeyResult[0]) != PacketType.SSH_MSG_NEWKEYS) {
				throw new RuntimeException("Unexpected packet");
			}
			calculateKeys(connection);
			next.handleServiceRequest(connection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void dumpBigInteger(byte[] data, String name) {
		System.out.println(name);
		for (int i = 0; i < data.length; ++i) {
			System.out.printf("%02X", data[i]);
		}
		System.out.println();
	}

	private void calculateKeys(SshConnection connection) throws IOException, NoSuchAlgorithmException {
		NegotiatedAlgorithmList negotiatedAlgorithms = connection.getNegotiatedAlgoritms();
		byte[] ivClientToServer = diffieHellmanHashService.hash(connection, 'A', 16);
		byte[] ivServerToClient = diffieHellmanHashService.hash(connection, 'B', 16);
		byte[] keyClientToServer = diffieHellmanHashService.hash(connection, 'C', 16);
		byte[] keyServerToClient = diffieHellmanHashService.hash(connection, 'D', 16);
		byte[] integrityKeyClientToServer = diffieHellmanHashService.hash(connection, 'E', 20);
		byte[] integrityKeyServerToClient = diffieHellmanHashService.hash(connection, 'F', 20);

		SshCipher serverToClientCipher = sshCipherFactory.createCipher(negotiatedAlgorithms.getEncryptionAlgorithmsServerToClient(), keyServerToClient, ivServerToClient);
		SshCipher clientToServerCipher = sshCipherFactory.createCipher(negotiatedAlgorithms.getEncryptionAlgorithmsClientToServer(), keyClientToServer, ivClientToServer);
		SshMac clientToServerMac = sshMacFactory.createMac(negotiatedAlgorithms.getMacAlgorithmsClientToServer(), integrityKeyClientToServer);
		SshMac serverToClientMac = sshMacFactory.createMac(negotiatedAlgorithms.getMacAlgorithmsServerToClient(), integrityKeyServerToClient);

		connection.setServerToClientCipher(serverToClientCipher);
		connection.setClientToServerCipher(clientToServerCipher);
		connection.setClientToServerMac(clientToServerMac);
		connection.setServerToClientMac(serverToClientMac);
	}

	private SshString signHash(SshString hash) throws Exception {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(new SshString("ssh-dss").serialize());
		byteStream.write(new SshString(dssSignatureService.sign(hash.getData())).serialize());
		return new SshString(byteStream.toByteArray());
	}

	private DhGexInit readDhGexInit(SshConnection connection) throws IOException {
		byte[] packet = dataExchangeService.readPacket(connection);
		DhGexInit dhGexInit = new DhGexInit(packet);
		System.out.println(dhGexInit);
		return dhGexInit;
	}

	private DhKeySize readDhKeySize(SshConnection connection) throws IOException {
		byte[] gexRequestMessage = readGexRequestMessage(connection);
		DhKeySize dhKeySize = new DhKeySize();
		dhKeySize.deserialize(gexRequestMessage);
		return dhKeySize;
	}

	private byte[] readGexRequestMessage(SshConnection connection) throws IOException {
		byte[] eData = dataExchangeService.readPacket(connection);
		byte[] e = new byte[eData.length - 1];
		PacketType packetType = PacketType.fromValue(eData[0]);
		if (packetType != PacketType.SSH_MSG_KEX_DH_GEX_REQUEST) {
			throw new RuntimeException("Not expected package");
		}
		System.arraycopy(eData, 1, e, 0, e.length);
		return e;
	}

	private void sendSafePrime(SshConnection connection, DhGexResponse dhGexResponse) throws IOException {
		byte[] dataToSend = dhGexResponse.serialize();
		dataExchangeService.sendPacket(connection, dataToSend);
	}

	private DhGexResponse createDhGexResponse(GeneratedPrime prime) {
		DhGexResponse dhGexResponse = new DhGexResponse();
		dhGexResponse.setGeneratorG(new MpInt(prime.getGenerator()));
		dhGexResponse.setSafePrimeP(new MpInt(prime.getPrime()));
		return dhGexResponse;
	}

	private SshString calculateHash(SshConnection connection, DhKeySize dhKeySize, DhGexResponse dhGexResponse, DhGexInit dhGexInit, MpInt f, MpInt k) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(connection.getRemoteIdentificationMessage().serialize());
		byteStream.write(connection.getLocaleIdentificationMessage().serialize());
		byteStream.write(connection.getRemoteKexMessage().serialize());
		byteStream.write(connection.getLocaleKexMessage().serialize());
		byteStream.write(publicKeyProvider.provide().serialize());
		byteStream.write(ByteConverterUtils.intToByte(dhKeySize.getMinimumLength()));
		byteStream.write(ByteConverterUtils.intToByte(dhKeySize.getPreferredLength()));
		byteStream.write(ByteConverterUtils.intToByte(dhKeySize.getMaximumLength()));
		byteStream.write(dhGexResponse.getSafePrimeP().serialize());
		byteStream.write(dhGexResponse.getGeneratorG().serialize());
		byteStream.write(dhGexInit.getE().serialize());
		byteStream.write(f.serialize());
		byteStream.write(k.serialize());
		byte[] bytesToHash = byteStream.toByteArray();
		System.out.flush();
		// TODO?
		byte[] result = hashFunction.hash(hashFunction.hash(bytesToHash));
		return new SshString(result);
	}
}
