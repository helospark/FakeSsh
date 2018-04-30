package com.helospark.FakeSsh.compression.factory;

import com.helospark.lightdi.annotation.Order;
import com.helospark.lightdi.annotation.Component;

import com.helospark.FakeSsh.compression.SshCompression;
import com.helospark.FakeSsh.compression.SshCompressionFactory;
import com.helospark.FakeSsh.compression.implementation.NoneSshCompression;

@Component
@Order(0)
public class NoneSshCompressionFactory implements SshCompressionFactory {

	@Override
	public SshCompression create() {
		return new NoneSshCompression();
	}

	@Override
	public String getName() {
		return "none";
	}

}
