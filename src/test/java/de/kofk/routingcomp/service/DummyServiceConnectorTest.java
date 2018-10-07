/**
 * 
 */
package de.kofk.routingcomp.service;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * @author Takara Baumbach
 *
 */
public class DummyServiceConnectorTest {

	private DummyServiceConnector sc;

	@Before
	public void setUp() {
		Logger.getGlobal().setLevel(Level.SEVERE);
		sc = (DummyServiceConnector) ServiceConnectorFactory.getServiceConnector("invalid API type", "key", true,
				new JSONObject("{\"param\": \"value\"}"), Logger.getGlobal());
	}

	@Test
	public void ProcessesServiceResultCorrectly() {
		ServiceConnectorResult output = sc.queryService(new QueryCoordinates(1.1, 2.2, 3.3, 4.4));
		ServiceConnectorResult expected = new ServiceConnectorResult(new QueryCoordinates(1.1, 2.2, 3.3, 4.4));
		expected.setApiType("Dummy");
		expected.setErrorMessage("Dummy API, key: key");
		expected.setTimingMode(true);
		expected.setTimes(output.getTimes());
		assertEquals(expected.toString(), output.toString());
	}

}
