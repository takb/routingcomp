package de.kofk.routingcomp.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public final class WebServiceUtil {
	public static JSONObject getJSONResponse(String url) {
		int httpStatus = 0;
		String responseStr;
		try {
			HttpResponse response = Request.Get(url).execute().returnResponse();
			httpStatus = response.getStatusLine().getStatusCode();
			responseStr = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			responseStr = String.format("{error_message: 'IO error: %s'}", e.getMessage());	
		}
		JSONObject responseJSON;
		try {
			responseJSON = new JSONObject(responseStr);
		} catch (JSONException e) {
			responseJSON = new JSONObject(String.format("{error_message: 'Service returned an invalid JSON response: %s'}", e.getMessage()));
		}
		responseJSON.put("http_status", httpStatus);
		return responseJSON;
	}
}