package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.domain.DhGexInit;
import com.helospark.FakeSsh.domain.DhGexReply;
import com.helospark.FakeSsh.domain.DhGexResponse;
import com.helospark.FakeSsh.domain.DhKeySize;
import com.helospark.FakeSsh.domain.DiffieHellmanKey;
import com.helospark.FakeSsh.domain.GeneratedPrime;
import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;
import com.helospark.FakeSsh.hostkey.ServerHostKeyAlgorithm;
import com.helospark.FakeSsh.hostkey.dsa.DssSignatureService;
import com.helospark.FakeSsh.io.SshDataExchangeService;
import com.helospark.FakeSsh.kex.DiffieHellmanExchangeHashCalculator;
import com.helospark.FakeSsh.kex.DiffieHellmanKeyCalculatorService;
import com.helospark.FakeSsh.kex.NegotiatedAlgorithmPopulator;
import com.helospark.FakeSsh.kex.SafePrimeProvider;
import com.helospark.FakeSsh.util.LoggerSupport;

@Component(StateNames.DIFFIE_HELLMAN_EXHCANGE_STATE)
public class DiffieHellmanExchangeState implements SshState {
	private SshDataExchangeService dataExchangeService;
	private SafePrimeProvider safePrimeProvider;
	private LoggerSupport loggerSupport;
	private NegotiatedAlgorithmPopulator negotiatedAlgorithmPopulator;
	private DiffieHellmanKeyCalculatorService diffieHellmanKeyCalculatorService;
	private DiffieHellmanExchangeHashCalculator diffieHellmanExchangeHashCalculator;

	@Autowired
	public DiffieHellmanExchangeState(SshDataExchangeService dataExchangeService, SafePrimeProvider safePrimeProvider,
			DssSignatureService dssSignatureService, LoggerSupport loggerSupport, DiffieHellmanKeyCalculatorService diffieHellmanKeyCalculatorService,
			NegotiatedAlgorithmPopulator negotiatedAlgorithmPopulator, DiffieHellmanExchangeHashCalculator diffieHellmanExchangeHashCalculator) {
		this.dataExchangeService = dataExchangeService;
		this.safePrimeProvider = safePrimeProvider;
		this.loggerSupport = loggerSupport;
		this.negotiatedAlgorithmPopulator = negotiatedAlgorithmPopulator;
		this.diffieHellmanKeyCalculatorService = diffieHellmanKeyCalculatorService;
		this.diffieHellmanExchangeHashCalculator = diffieHellmanExchangeHashCalculator;
	}

	@Override
	public StateMachineResult enterState(SshConnection connection, byte[] previousPacket) {
		try {
			DhKeySize dhKeySize = readDhKeySize(previousPacket);
			GeneratedPrime prime = safePrimeProvider.providePrime(dhKeySize.getMinimumLength(), dhKeySize.getPreferredLength(), dhKeySize.getMaximumLength());
			DhGexResponse dhGexResponse = createDhGexResponse(prime);
			sendSafePrime(connection, dhGexResponse);
			DhGexInit dhGexInit = readDhGexInit(connection);

			DiffieHellmanKey diffieHellmanKey = diffieHellmanKeyCalculatorService.calculateKey(prime, dhGexInit.getE());
			SshString publicKey = connection.getKeyProvider().providePublicKey();
			byte[] hash = diffieHellmanExchangeHashCalculator.calculateHash(connection, dhKeySize, dhGexResponse, dhGexInit, diffieHellmanKey, publicKey);

			sendDhGexReply(connection, diffieHellmanKey, hash);

			exchangeNewKeyPackets(connection);

			populateConnectionWithKexResult(connection, diffieHellmanKey, hash);

			logDiffieHellmanData(dhGexInit, diffieHellmanKey);

			return StateMachineResult.SUCCESS;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void populateConnectionWithKexResult(SshConnection connection, DiffieHellmanKey diffieHellmanKey, byte[] hash) throws IOException, NoSuchAlgorithmException {
		connection.setKey(diffieHellmanKey.getK());
		connection.setHash(hash);
		negotiatedAlgorithmPopulator.populateMacAndCipherOnConnection(connection);
	}

	private void logDiffieHellmanData(DhGexInit dhGexInit, DiffieHellmanKey diffieHellmanKey) {
		loggerSupport.dumpBigIntegerInHex(dhGexInit.getE().getBigInteger(), "E");
		loggerSupport.dumpBigIntegerInHex(diffieHellmanKey.getK().getBigInteger(), "K");
		loggerSupport.dumpBigIntegerInHex(diffieHellmanKey.getF().getBigInteger(), "Shared key");
	}

	private void sendDhGexReply(SshConnection connection, DiffieHellmanKey diffieHellmanKey, byte[] hash) throws Exception, IOException {
		DhGexReply dhGexReply = new DhGexReply();
		dhGexReply.setPacketType(PacketType.SSH_MSG_KEX_DH_GEX_REPLY);
		dhGexReply.setPublicKey(connection.getKeyProvider().providePublicKey());
		dhGexReply.setF(diffieHellmanKey.getF());
		dhGexReply.setHash(signHash(connection.getKeyProvider(), hash));
		dataExchangeService.sendPacket(connection, dhGexReply.serialize());
	}

	private void exchangeNewKeyPackets(SshConnection connection) throws IOException {
		byte[] newKeyResult = dataExchangeService.readPacket(connection);
		if (PacketType.fromValue(newKeyResult[0]) != PacketType.SSH_MSG_NEWKEYS) {
			throw new RuntimeException("Unexpected packet");
		}
		dataExchangeService.sendPacket(connection, new byte[] { PacketType.SSH_MSG_NEWKEYS.getValue() });
	}

	private SshString signHash(ServerHostKeyAlgorithm serverHostKeyAlgorithm, byte[] hash) throws Exception {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(new SshString(serverHostKeyAlgorithm.getSignatureName()).serialize());
		byteStream.write(new SshString(serverHostKeyAlgorithm.sign(hash)).serialize());
		return new SshString(byteStream.toByteArray());
	}

	private DhGexInit readDhGexInit(SshConnection connection) throws IOException {
		byte[] packet = dataExchangeService.readPacket(connection);
		return new DhGexInit(packet);
	}

	private DhKeySize readDhKeySize(byte[] previousPacket) throws IOException {
		return new DhKeySize(previousPacket);
	}

	private void sendSafePrime(SshConnection connection, DhGexResponse dhGexResponse) throws IOException {
		dataExchangeService.sendPacket(connection, dhGexResponse.serialize());
	}

	private DhGexResponse createDhGexResponse(GeneratedPrime prime) {
		DhGexResponse dhGexResponse = new DhGexResponse();
		dhGexResponse.setGeneratorG(new MpInt(prime.getGenerator()));
		dhGexResponse.setSafePrimeP(new MpInt(prime.getPrime()));
		return dhGexResponse;
	}

}
