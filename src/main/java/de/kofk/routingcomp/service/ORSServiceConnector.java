package de.kofk.routingcomp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * ServiceConnector implementation that utilizes Open Routing Service API.
 * 
 * @author Takara Baumbach
 *
 */
public class ORSServiceConnector implements ServiceConnector {

	private String apiKey = "";
	private boolean timingMode = false;
	private JSONObject params;
	private Logger logger;
	boolean enabled;

	public ORSServiceConnector(String apiKey, boolean timingMode, JSONObject params, Logger logger) {
		this.apiKey = apiKey;
		this.timingMode = timingMode;
		this.params = params;
		this.logger = logger;
		this.enabled = true;
	}

	public ServiceConnectorResult queryService(QueryCoordinates qc) {
		logger.info("ORS API");
		ServiceConnectorResult result = new ServiceConnectorResult(qc);
		result.setApiType("ORS");
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
		logger.info("- initializing query");
		// query init
		String queryURL = "https://api.openrouteservice.org/directions?api_key=" + this.apiKey;
		try {
			queryURL += "&coordinates=" + URLEncoder.encode(
					String.format("%s,%s|%s,%s", qc.startLongitude, qc.startLatitude, qc.endLongitude, qc.endLatitude),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.severe("Encoding error during service URL initialization: " + e.getMessage());
			result.setErrorMessage("Connector disfunctional");
			return result;
		}
		boolean hasProfileParam = false;
		if (this.params != null) {
			Iterator<String> keysItr = this.params.keys();
			while (keysItr.hasNext()) {
				String key = keysItr.next();
				if (key == "profile") {
					hasProfileParam = true;
				}
				try {
					if (key.equals("options")) {
						try {
							queryURL += "&" + key + "=" +  URLEncoder.encode(this.params.getJSONObject(key).toString(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							logger.severe("Encoding error during service URL initialization: " + e.getMessage());
							result.setErrorMessage("Connector disfunctional");
						}
					} else {
						queryURL += "&" + key + "=" + this.params.getString(key);
					}
				} catch (JSONException e) {
					logger.warning("Optional parameter " + key + " has an invalid value.");
					e.printStackTrace();
				}
			}
		}
		if (!hasProfileParam) {
			queryURL += "&profile=driving-car";
		}

		if (this.timingMode) {
			times.put("submit", System.nanoTime());
		}
		logger.info("- submitting query to remote service");
		// query submit
		JSONObject qureyResult = WebServiceUtil.getJSONResponse(queryURL);

		if (this.timingMode) {
			times.put("process", System.nanoTime());
		}
		logger.info("- processing query result");
		// process result
		String errorMessage = "";
		if (qureyResult.has("error")) {
			try {
				// returned error may be a string containing an error message or an object containing a code and message
				errorMessage = qureyResult.getString("error");				
			} catch (JSONException e) {
				// error is object 
				errorMessage = "#" + qureyResult.getJSONObject("error").getInt("code") + " " + qureyResult.getJSONObject("error").getString("message");				
			}
			if (qureyResult.has("http_status") && qureyResult.getInt("http_status") == 403) {
				this.enabled = false;
				logger.severe("ORSServiceConnector is not configured properly. Terminating further operations on this connector.");
				errorMessage += " - ORSServiceConnector is not configured properly. Terminating further operations on this connector.";
			}
			result.setErrorMessage(errorMessage);				
		} else {
			JSONObject summary = qureyResult.getJSONArray("routes").getJSONObject(0).getJSONObject("summary");
			String durationStr = Duration.ofSeconds(summary.getInt("duration")).toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
			String distanceStr = summary.getInt("distance") > 1000 ? String.format("%.1f km", (float)summary.getInt("distance") / 1000) : summary.get("distance").toString();
			result.setDistanceStr(distanceStr);
			result.setDistance(summary.getFloat("distance"));
			result.setDurationStr(durationStr);
			result.setDuration(summary.getFloat("duration"));
		}

		if (this.timingMode) {
			times.put("end", System.nanoTime());
		}
		result.setTimingMode(this.timingMode);
		result.setTimes(times);
		logger.info("- done.");
		return result;
	}

}
