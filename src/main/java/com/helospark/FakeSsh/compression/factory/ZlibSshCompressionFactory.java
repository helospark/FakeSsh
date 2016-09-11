package com.helospark.FakeSsh.compression.factory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.compression.SshCompression;
import com.helospark.FakeSsh.compression.SshCompressionFactory;
import com.helospark.FakeSsh.compression.implementation.ZlibSshCompression;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

/**
 * Factory to create {@link ZlibSshCompression}.
 * Note: SSH defines partial flush as the algorithm to flush data between packeges, but
 * it appears that it is not supported by the Deflate/Inflate present in the JDK.
 * For this reason JZlib is used as a backend.
 * @author helospark
 */
@Component
@Order(1)
public class ZlibSshCompressionFactory implements SshCompressionFactory {

	@Override
	public SshCompression create() {
		return new ZlibSshCompression(createDeflater(), createInflater());
	}

	public Deflater createDeflater() {
		Deflater deflater = new Deflater();
		deflater.init(6);
		return deflater;
	}

	@SuppressWarnings("deprecation")
	public Inflater createInflater() {
		Inflater inflater = new Inflater();
		inflater.inflateInit(JZlib.MAX_WBITS, JZlib.W_ZLIB);
		return inflater;
	}

	@Override
	public String getName() {
		return "zlib";
	}
}
