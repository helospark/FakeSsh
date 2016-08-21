package com.helospark.FakeSsh;

/**
 * Spring component name of all of the Ssh states for easier linking.
 * @author helospark
 */
public class StateNames {
	public static final String START_STATE = "startState";
	public static final String SERVICE_REQUEST_STATE = "serviceRequestState";
	public static final String SUPPORTED_ALGORITHM_EXCHANGE_STATE = "supportedAlgorithmExchangeState";
	public static final String IDENTIFICATION_EXCHANGE_STATE = "identificationExchangeState";
	public static final String DIFFIE_HELLMAN_EXHCANGE_STATE = "diffieHellmanExchangeState";
}
