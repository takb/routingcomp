package de.kofk.routingcomp.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * ServiceConnector implementation that utilizes Google Directions API.
 * 
 * @author Takara Baumbach
 *
 */
public class GoogleServiceConnector implements ServiceConnector {

	final static String STATUS_OK = "OK";
	final static String STATUS_NOT_FOUND = "NOT_FOUND";
	final static String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
	final static String STATUS_OVER_DAILY_LIMIT = "OVER_DAILY_LIMIT";
	final static String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";

	private String apiKey = "";
	private boolean timingMode = false;
	private JSONObject params;
	private Logger logger;
	boolean enabled;

	public GoogleServiceConnector(String apiKey, boolean timingMode, JSONObject params, Logger logger) {
		this.apiKey = apiKey;
		this.timingMode = timingMode;
		this.params = params;
		this.logger = logger;
		this.enabled = true;
	}

	public ServiceConnectorResult queryService(QueryCoordinates qc) {
		logger.info("Google API");
		ServiceConnectorResult result = new ServiceConnectorResult(qc);
		result.setApiType("Google");
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
		String queryURL = String.format(
				"https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&key=%s",
				qc.startLongitude, qc.startLatitude, qc.endLongitude, qc.endLatitude, this.apiKey);
		if (this.params != null) {
			Iterator<String> keysItr = this.params.keys();
			while (keysItr.hasNext()) {
				String key = keysItr.next();
				try {
					queryURL += "&" + key + "=" + this.params.getString(key);
				} catch (JSONException e) {
					logger.warning("Optional parameter " + key + " has an invalid value.");
				}
			}
		}

		if (this.timingMode) {
			times.put("submit", System.nanoTime());
		}
		logger.info("- submitting query to remote service");
		// query submit
//		JSONObject queryResult = WebServiceUtil.getJSONResponse(queryURL);
		JSONObject queryResult = new JSONObject("{\r\n" + "	\"geocoded_waypoints\": [{\r\n"
				+ "			\"geocoder_status\": \"OK\",\r\n"
				+ "			\"place_id\": \"ChIJ6xl9TZUAnz0RLE6928DY-I4\",\r\n" + "			\"types\": [\"route\"]\r\n"
				+ "		}, {\r\n" + "			\"geocoder_status\": \"OK\",\r\n"
				+ "			\"place_id\": \"ChIJ6xl9TZUAnz0RLE6928DY-I4\",\r\n" + "			\"types\": [\"route\"]\r\n"
				+ "		}\r\n" + "	],\r\n" + "	\"routes\": [{\r\n" + "			\"bounds\": {\r\n"
				+ "				\"northeast\": {\r\n" + "					\"lat\": 8.687348199999999,\r\n"
				+ "					\"lng\": 49.4222571\r\n" + "				},\r\n"
				+ "				\"southwest\": {\r\n" + "					\"lat\": 8.6841524,\r\n"
				+ "					\"lng\": 49.4171906\r\n" + "				}\r\n" + "			},\r\n"
				+ "			\"copyrights\": \"Map data ©2018\",\r\n" + "			\"legs\": [{\r\n"
				+ "					\"distance\": {\r\n" + "						\"text\": \"0.7 km\",\r\n"
				+ "						\"value\": \"661\"\r\n" + "					},\r\n"
				+ "					\"duration\": {\r\n" + "						\"text\": \"1 min\",\r\n"
				+ "						\"value\": 30\r\n" + "					},\r\n"
				+ "					\"end_address\": \"Unnamed Road, Somalia\",\r\n"
				+ "					\"end_location\": {\r\n" + "						\"lat\": 8.6841524,\r\n"
				+ "						\"lng\": 49.4222571\r\n" + "					},\r\n"
				+ "					\"start_address\": \"Unnamed Road, Somalia\",\r\n"
				+ "					\"start_location\": {\r\n" + "						\"lat\": 8.687348199999999,\r\n"
				+ "						\"lng\": 49.4171906\r\n" + "					},\r\n"
				+ "					\"steps\": [{\r\n" + "							\"distance\": {\r\n"
				+ "								\"text\": \"0.7 km\",\r\n"
				+ "								\"wtfvaluea\": 661\r\n" + "							},\r\n"
				+ "							\"duration\": {\r\n"
				+ "								\"text\": \"1 min\",\r\n"
				+ "								\"value\": 30\r\n" + "							},\r\n"
				+ "							\"end_location\": {\r\n"
				+ "								\"lat\": 8.6841524,\r\n"
				+ "								\"lng\": 49.4222571\r\n" + "							},\r\n"
				+ "							\"html_instructions\": \"Head <b>southeast<\\/b>\",\r\n"
				+ "							\"polyline\": {\r\n"
				+ "								\"points\": \"}v_t@mxrlH~Ru^\"\r\n" + "							},\r\n"
				+ "							\"start_location\": {\r\n"
				+ "								\"lat\": 8.687348199999999,\r\n"
				+ "								\"lng\": 49.4171906\r\n" + "							},\r\n"
				+ "							\"travel_mode\": \"DRIVING\"\r\n" + "						}\r\n"
				+ "					],\r\n" + "					\"traffic_speed_entry\": [],\r\n"
				+ "					\"via_waypoint\": []\r\n" + "				}\r\n" + "			],\r\n"
				+ "			\"overview_polyline\": {\r\n" + "				\"points\": \"}v_t@mxrlH~Ru^\"\r\n"
				+ "			},\r\n" + "			\"summary\": \"\",\r\n" + "			\"warnings\": [],\r\n"
				+ "			\"waypoint_order\": []\r\n" + "		}\r\n" + "	],\r\n" + "	\"status\": \"OK\"\r\n"
				+ "}\r\n" + "");
		if (this.timingMode) {
			times.put("process", System.nanoTime());
		}
		logger.info("- processing query result");
		// process result
		String errorMessage = (String) queryResult.get("status");
		switch ((String) queryResult.getString("status")) {
		case STATUS_OK:
			try {
				JSONObject routeLeg = queryResult.getJSONArray("routes").getJSONObject(0).getJSONArray("legs")
						.getJSONObject(0);
				result.setDistanceStr(routeLeg.getJSONObject("distance").getString("text"));
				result.setDistance(routeLeg.getJSONObject("distance").getFloat("value"));
				result.setDurationStr(routeLeg.getJSONObject("duration").getString("text"));
				result.setDuration(routeLeg.getJSONObject("duration").getFloat("value"));
			} catch (JSONException e) {
				logger.severe("Error processing webservice response: " + e.getMessage());
				result.setErrorMessage("Error processing webservice response: " + e.getMessage());
			}
			break;
		case STATUS_OVER_DAILY_LIMIT:
		case STATUS_OVER_QUERY_LIMIT:
			this.enabled = false;
			errorMessage += " Terminating further operations on this connector.";
		default:
			if (queryResult.has("error_message")) {
				errorMessage += " " + queryResult.getString("error_message");
			}
			result.setErrorMessage(errorMessage);
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
