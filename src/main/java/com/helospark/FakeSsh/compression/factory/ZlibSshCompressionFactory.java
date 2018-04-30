package com.helospark.FakeSsh.compression.factory;

import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.compression.SshCompression;
import com.helospark.FakeSsh.compression.SshCompressionFactory;
import com.helospark.FakeSsh.compression.implementation.ZlibSshCompression;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

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
