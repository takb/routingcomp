package de.kofk.routingcomp;

/**
 * The four floats comprising a query.
 * 
 * @author Takara Baumbach
 *
 */
public class QueryCoordinates {

	public double startLongitude;
	public double startLatitude;
	public double endLongitude;
	public double endLatitude;

	/**
	 * Constructor to set all four values.
	 * 
	 * @param startLongitude
	 * @param startLatitude
	 * @param endLongitude
	 * @param endLatitude
	 */
	public QueryCoordinates(double startLongitude, double startLatitude, double endLongitude, double endLatitude) {
		this.startLongitude = startLongitude;
		this.startLatitude = startLatitude;
		this.endLongitude = endLongitude;
		this.endLatitude = endLatitude;
	}

	public String toString() {
		return String.format("%s,%s => %s,%s", this.startLongitude, this.startLatitude, this.endLongitude,
				this.endLatitude);
	}
}
