package de.kofk.routingcomp.service;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * 
 * 
 * @author Takara Baumbach
 *
 */
public interface ServiceConnector {
	public ServiceConnectorResult queryService(QueryCoordinates qc);
}
