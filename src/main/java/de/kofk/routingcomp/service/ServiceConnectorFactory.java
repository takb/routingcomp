package de.kofk.routingcomp.service;

import java.util.logging.Logger;

import org.json.JSONObject;

/**
 * Factory class for getting the appropriate ServiceConnector depending on the
 * type string from configuration.
 * 
 * @author Takara Baumbach
 *
 */
public class ServiceConnectorFactory {
	final static String TYPE_GOOGLE = "GOOGLE";
	final static String TYPE_ORS = "ORS";

	public static ServiceConnector getServiceConnector(String apiType, String apiKey, boolean timingMode, JSONObject params,
			Logger logger) {
		switch (apiType.toUpperCase()) {
		case TYPE_GOOGLE:
			return new GoogleServiceConnector(apiKey, timingMode, params, logger);
		case TYPE_ORS:
			return new ORSServiceConnector(apiKey, timingMode, params, logger);
		default:
			return new DummyServiceConnector(apiKey, timingMode, params, logger);
		}
	}
}
