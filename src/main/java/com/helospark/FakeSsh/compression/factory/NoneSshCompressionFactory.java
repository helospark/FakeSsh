package com.helospark.FakeSsh.compression.factory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.compression.SshCompression;
import com.helospark.FakeSsh.compression.SshCompressionFactory;
import com.helospark.FakeSsh.compression.implementation.NoneSshCompression;

/**
 * Factory to create {@link NoneSshCompressionFactory}.
 * @author helospark
 */
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
