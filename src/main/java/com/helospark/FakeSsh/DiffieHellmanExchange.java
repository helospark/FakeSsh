package com.helospark.FakeSsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.domain.DhGexInit;
import com.helospark.FakeSsh.domain.DhGexReply;
import com.helospark.FakeSsh.domain.DhGexResponse;
import com.helospark.FakeSsh.domain.DhKeySize;
import com.helospark.FakeSsh.domain.DiffieHellmanKey;
import com.helospark.FakeSsh.domain.GeneratedPrime;
import com.helospark.FakeSsh.domain.MpInt;
import com.helospark.FakeSsh.domain.SshString;

@Component(StateNames.DIFFIE_HELLMAN_EXHCANGE_STATE)
public class DiffieHellmanExchange implements SshState {
	private SshDataExchangeService dataExchangeService;
	private SafePrimeProvider safePrimeProvider;
	private DssSignatureService dssSignatureService;
	private SshServiceRequestService next;
	private LoggerSupport loggerSupport;
	private DsaKeyProvider dsaKeyProvider;
	private MacAndCipherPopulator macAndCipherPopulator;
	private DiffieHellmanKeyCalculatorService diffieHellmanKeyCalculatorService;
	private DiffieHellmanExchangeHashCalculator diffieHellmanExchangeHashCalculator;

	@Autowired
	public DiffieHellmanExchange(SshDataExchangeService dataExchangeService, SafePrimeProvider safePrimeProvider,
			DssSignatureService dssSignatureService, @Qualifier(StateNames.SERVICE_REQUEST_STATE) SshServiceRequestService next,
			LoggerSupport loggerSupport, DsaKeyProvider dsaKeyProvider, DiffieHellmanKeyCalculatorService diffieHellmanKeyCalculatorService,
			MacAndCipherPopulator macAndCipherPopulator, DiffieHellmanExchangeHashCalculator diffieHellmanExchangeHashCalculator) {
		this.dataExchangeService = dataExchangeService;
		this.safePrimeProvider = safePrimeProvider;
		this.dssSignatureService = dssSignatureService;
		this.next = next;
		this.loggerSupport = loggerSupport;
		this.dsaKeyProvider = dsaKeyProvider;
		this.macAndCipherPopulator = macAndCipherPopulator;
		this.diffieHellmanKeyCalculatorService = diffieHellmanKeyCalculatorService;
		this.diffieHellmanExchangeHashCalculator = diffieHellmanExchangeHashCalculator;
	}

	@Override
	public void enterState(SshConnection connection) {
		try {
			DhKeySize dhKeySize = readDhKeySize(connection);
			GeneratedPrime prime = safePrimeProvider.providePrime(dhKeySize.getMinimumLength(), dhKeySize.getPreferredLength(), dhKeySize.getMaximumLength());
			DhGexResponse dhGexResponse = createDhGexResponse(prime);
			sendSafePrime(connection, dhGexResponse);
			DhGexInit dhGexInit = readDhGexInit(connection);

			DiffieHellmanKey diffieHellmanKey = diffieHellmanKeyCalculatorService.calculateKey(prime, dhGexInit.getE());

			byte[] hash = diffieHellmanExchangeHashCalculator.calculateHash(connection, dhKeySize, dhGexResponse, dhGexInit, diffieHellmanKey);

			sendDhGexReply(connection, diffieHellmanKey, hash);

			exchangeNewKeyPackets(connection);

			populateConnectionWithKexResult(connection, diffieHellmanKey, hash);

			logDiffieHellmanData(dhGexInit, diffieHellmanKey);

			next.enterState(connection);
		} catch (ConnectionClosedException e) {
			return;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void populateConnectionWithKexResult(SshConnection connection, DiffieHellmanKey diffieHellmanKey, byte[] hash) throws IOException, NoSuchAlgorithmException {
		connection.setKey(diffieHellmanKey.getK());
		connection.setHash(hash);
		macAndCipherPopulator.populateMacAndCipherOnConnection(connection);
	}

	private void logDiffieHellmanData(DhGexInit dhGexInit, DiffieHellmanKey diffieHellmanKey) {
		loggerSupport.dumpBigIntegerInHex(dhGexInit.getE().getBigInteger(), "E");
		loggerSupport.dumpBigIntegerInHex(diffieHellmanKey.getK().getBigInteger(), "K");
		loggerSupport.dumpBigIntegerInHex(diffieHellmanKey.getF().getBigInteger(), "Shared key");
	}

	private void sendDhGexReply(SshConnection connection, DiffieHellmanKey diffieHellmanKey, byte[] hash) throws Exception, IOException {
		DhGexReply dhGexReply = new DhGexReply();
		dhGexReply.setPacketType(PacketType.SSH_MSG_KEX_DH_GEX_REPLY);
		dhGexReply.setPublicKey(dsaKeyProvider.providePublicKey());
		dhGexReply.setF(diffieHellmanKey.getF());
		dhGexReply.setHash(signHash(hash));
		dataExchangeService.sendPacket(connection, dhGexReply.serialize());
	}

	private void exchangeNewKeyPackets(SshConnection connection) throws IOException {
		byte[] newKeyResult = dataExchangeService.readPacket(connection);
		if (PacketType.fromValue(newKeyResult[0]) != PacketType.SSH_MSG_NEWKEYS) {
			throw new RuntimeException("Unexpected packet");
		}
		dataExchangeService.sendPacket(connection, new byte[] { PacketType.SSH_MSG_NEWKEYS.getValue() });
	}

	private SshString signHash(byte[] hash) throws Exception {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(new SshString("ssh-dss").serialize());
		byteStream.write(new SshString(dssSignatureService.sign(hash)).serialize());
		return new SshString(byteStream.toByteArray());
	}

	private DhGexInit readDhGexInit(SshConnection connection) throws IOException {
		byte[] packet = dataExchangeService.readPacket(connection);
		return new DhGexInit(packet);
	}

	private DhKeySize readDhKeySize(SshConnection connection) throws IOException {
		byte[] gexRequestMessage = dataExchangeService.readPacket(connection);
		return new DhKeySize(gexRequestMessage);
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
