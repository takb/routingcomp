package de.kofk.routingcomp.service;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.JSONObject;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * Dummy implementation of Service Connector.
 * 
 * @author Takara Baumbach
 *
 */
public class DummyServiceConnector implements ServiceConnector {

	private String apiKey = "";
	private boolean timingMode = false;
	private JSONObject params;
	private Logger logger;
	boolean enabled;

	public DummyServiceConnector(String apiKey, boolean timingMode, JSONObject params, Logger logger) {
		this.apiKey = apiKey;
		this.timingMode = timingMode;
		this.params = params;
		this.logger = logger;
		this.enabled = true;
	}

	public ServiceConnectorResult queryService(QueryCoordinates qc) {
		logger.info("Dummy API");
		ServiceConnectorResult result = new ServiceConnectorResult(qc);
		result.setApiType("Dummy");
		if (!enabled) {
			logger.info("- API disabled");
			result.setErrorMessage("API disabled");
			return result;
		}
		HashMap<String, Long> times = null;
		if (this.timingMode) {
			times = new HashMap<String, Long>();
			times.put("init", System.nanoTime());
		}
		logger.info("- initializing query, params: "+this.params);
		// query init

		// NOTHING TO DO...
		if (this.timingMode) {
			times.put("submit", System.nanoTime());
		}
		logger.info("- submitting query to remote service");
		// query submit

		// NOTHING TO DO...
		if (this.timingMode) {
			times.put("process", System.nanoTime());
		}
		logger.info("- processing query result");
		// process result

		result.setErrorMessage("Dummy API, key: " + this.apiKey);
		if (this.timingMode) {
			times.put("end", System.nanoTime());
		}
		result.setTimingMode(this.timingMode);
		result.setTimes(times);
		logger.info("- done.");
		return result;
	}
}
