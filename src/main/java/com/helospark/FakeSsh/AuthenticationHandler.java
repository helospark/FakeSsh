package com.helospark.FakeSsh;

import com.helospark.FakeSsh.domain.SshUserauthRequest;

/**
 * Handler to the authentication process.
 * @author helospark
 */
public interface AuthenticationHandler {

	boolean canHandle(SshUserauthRequest sshUserauthRequest);

	boolean isSuccessful(byte[] rawData, final SshConnection connection) throws Exception;

	String getHandledMethodName();

}
