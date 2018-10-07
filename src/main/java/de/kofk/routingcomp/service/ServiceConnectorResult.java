package de.kofk.routingcomp.service;

import java.util.HashMap;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * Container for the result data of a service query.
 * 
 * @author Takara Baumbach
 *
 */
public class ServiceConnectorResult {
	private double queryStartLongitude;
	private double queryStartLatitude;
	private double queryEndLongitude;
	private double queryEndLatitude;
	private String errorMessage;
	private String apiType;
	private float distance;
	private float duration;
	private String distanceStr;
	private String durationStr;
	private HashMap<String, Long> times;
	private boolean timingMode = false;

	public ServiceConnectorResult() {
		this.queryStartLongitude = 0;
		this.queryStartLatitude = 0;
		this.queryEndLongitude = 0;
		this.queryEndLatitude = 0;
		this.errorMessage = "";
	}

	public ServiceConnectorResult(QueryCoordinates qc) {
		this.queryStartLongitude = qc.startLongitude;
		this.queryStartLatitude = qc.startLatitude;
		this.queryEndLongitude = qc.endLongitude;
		this.queryEndLatitude = qc.endLatitude;
		this.errorMessage = "";
	}

	public double getQueryStartLongitude() {
		return queryStartLongitude;
	}

	public double getQueryStartLatitude() {
		return queryStartLatitude;
	}

	public double getQueryEndLongitude() {
		return queryEndLongitude;
	}

	public double getQueryEndLatitude() {
		return queryEndLatitude;
	}

	public String getQueryIdentifier() {
		return String.format("%s,%s=>%s,%s", this.queryStartLongitude, this.queryStartLatitude, this.queryEndLongitude,
				this.queryEndLatitude);
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public String getDistanceStr() {
		return distanceStr;
	}

	public void setDistanceStr(String distanceStr) {
		this.distanceStr = distanceStr;
	}

	public String getDurationStr() {
		return durationStr;
	}

	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public boolean isTimingMode() {
		return timingMode;
	}

	public void setTimingMode(boolean timingMode) {
		this.timingMode = timingMode;
	}

	public HashMap<String, Long> getTimes() {
		return times;
	}

	public void setTimes(HashMap<String, Long> times) {
		this.times = times;
	}

	public float getInitTime() {
		return (float) (this.times.get("submit") - this.times.get("init")) / 1000000;
	}

	public float getRequestTime() {
		return (float) (this.times.get("process") - this.times.get("submit")) / 1000000;
	}

	public float getProcessTime() {
		return (float) (this.times.get("end") - this.times.get("process")) / 1000000;
	}

	public boolean hasError() {
		return !this.errorMessage.equals("");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.apiType + ": " + (this.errorMessage.isEmpty() ? "SUCCESS" : "ERROR: " + this.errorMessage));
		if (this.errorMessage.isEmpty()) {
			sb.append(String.format(" Result: distance %s (%sm), duration %s (%ss)", this.distanceStr, this.distance, this.durationStr, this.duration));
		}
		if (this.timingMode) {
			sb.append(String.format("\nProcessing time: INIT %12.6fms, REQUEST %12.6fms, PROCESS %12.6fms",
					this.getInitTime(), this.getRequestTime(), this.getProcessTime()));
		}
		return sb.toString();
	}

}
