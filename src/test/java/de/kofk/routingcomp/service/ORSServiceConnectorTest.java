/**
 * 
 */
package de.kofk.routingcomp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.kofk.routingcomp.QueryCoordinates;

/**
 * @author Takara Baumbach
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ WebServiceUtil.class })
public class ORSServiceConnectorTest {

	private ORSServiceConnector sc;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(WebServiceUtil.class);
		Logger.getGlobal().setLevel(Level.SEVERE);
		sc = (ORSServiceConnector) ServiceConnectorFactory.getServiceConnector("ORS", "key", true,
				new JSONObject("{\"param\": \"value\", \"options\": {\"param\": \"value\"}}"), Logger.getGlobal());
	}

	@Test
	public void ProcessesServiceResultCorrectly() {
		JSONObject result = new JSONObject("{\r\n" + "	\"bbox\": [8.676441, 49.411648, 8.687828, 49.426395],\r\n"
				+ "	\"info\": {\r\n"
				+ "		\"attribution\": \"openrouteservice.org | OpenStreetMap contributors\",\r\n"
				+ "		\"engine\": {\r\n" + "			\"build_date\": \"2018-09-23T13:47:22Z\",\r\n"
				+ "			\"version\": \"4.5.0\"\r\n" + "		},\r\n" + "		\"query\": {\r\n"
				+ "			\"coordinates\": [[8.680916, 49.410973], [8.687782, 49.4246]],\r\n"
				+ "			\"elevation\": false,\r\n" + "			\"geometry\": true,\r\n"
				+ "			\"geometry_format\": \"encodedpolyline\",\r\n" + "			\"instructions\": true,\r\n"
				+ "			\"instructions_format\": \"text\",\r\n" + "			\"language\": \"de\",\r\n"
				+ "			\"preference\": \"fastest\",\r\n" + "			\"profile\": \"driving-car\",\r\n"
				+ "			\"units\": \"m\"\r\n" + "		},\r\n" + "		\"service\": \"routing\",\r\n"
				+ "		\"timestamp\": 1538701097539\r\n" + "	},\r\n" + "	\"routes\": [{\r\n"
				+ "			\"bbox\": [8.676441, 49.411648, 8.687828, 49.426395],\r\n"
				+ "			\"geometry\": \"yuqlH{i~s@e@VQFRbBXfD?BgEX?BBpADlB?FBj@Ep@?J?d@O@oDJiCFuDRy@@sADoDHU@Q?Q?o@AeA?K?{@?G?gB?mCEU@G?k@AG?gC?kACc@?a@Ca@KgAe@[Uu@y@u@sAUc@[u@_@{@oBsDc@u@e@u@Wa@Yi@Wc@aCoDa@g@KM}A{B]a@u@}@aBoBIGECDELMHY@KbAcD`A}C`BgFX_Af@}AX{@Rm@P]EEACCGI?KB\",\r\n"
				+ "			\"geometry_format\": \"encodedpolyline\",\r\n" + "			\"segments\": [{\r\n"
				+ "					\"distance\": 2499,\r\n" + "					\"duration\": 306.1,\r\n"
				+ "					\"steps\": [{\r\n" + "							\"distance\": 32.9,\r\n"
				+ "							\"duration\": 7.9,\r\n"
				+ "							\"instruction\": \"Weiter nördlich\",\r\n"
				+ "							\"name\": \"\",\r\n" + "							\"type\": 11,\r\n"
				+ "							\"way_points\": [0, 2]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 101.5,\r\n"
				+ "							\"duration\": 24.3,\r\n"
				+ "							\"instruction\": \"Biegen Sie links auf Uferstraße ab\",\r\n"
				+ "							\"name\": \"Uferstraße\",\r\n"
				+ "							\"type\": 0,\r\n" + "							\"way_points\": [2, 5]\r\n"
				+ "						}, {\r\n" + "							\"distance\": 111.7,\r\n"
				+ "							\"duration\": 16.1,\r\n"
				+ "							\"instruction\": \"Biegen Sie rechts auf Am Römerbad ab\",\r\n"
				+ "							\"name\": \"Am Römerbad\",\r\n"
				+ "							\"type\": 1,\r\n" + "							\"way_points\": [5, 6]\r\n"
				+ "						}, {\r\n" + "							\"distance\": 126.2,\r\n"
				+ "							\"duration\": 31.2,\r\n"
				+ "							\"instruction\": \"Biegen Sie links auf Jahnstraße ab\",\r\n"
				+ "							\"name\": \"Jahnstraße\",\r\n"
				+ "							\"type\": 0,\r\n" + "							\"way_points\": [6, 14]\r\n"
				+ "						}, {\r\n" + "							\"distance\": 559.8,\r\n"
				+ "							\"duration\": 62.9,\r\n"
				+ "							\"instruction\": \"Biegen Sie rechts auf Berliner Straße ab\",\r\n"
				+ "							\"name\": \"Berliner Straße\",\r\n"
				+ "							\"type\": 1,\r\n"
				+ "							\"way_points\": [14, 26]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 181,\r\n" + "							\"duration\": 20.3,\r\n"
				+ "							\"instruction\": \"Weiter geradeaus auf Berliner Straße\",\r\n"
				+ "							\"name\": \"Berliner Straße\",\r\n"
				+ "							\"type\": 6,\r\n"
				+ "							\"way_points\": [26, 31]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 969.6,\r\n"
				+ "							\"duration\": 92.5,\r\n"
				+ "							\"instruction\": \"Weiter geradeaus auf Berliner Straße\",\r\n"
				+ "							\"name\": \"Berliner Straße\",\r\n"
				+ "							\"type\": 6,\r\n"
				+ "							\"way_points\": [31, 62]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 395,\r\n" + "							\"duration\": 45.7,\r\n"
				+ "							\"instruction\": \"Biegen Sie scharf rechts auf Rottmannstraße, B 3 ab\",\r\n"
				+ "							\"name\": \"Rottmannstraße, B 3\",\r\n"
				+ "							\"type\": 3,\r\n"
				+ "							\"way_points\": [62, 74]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 9.1,\r\n" + "							\"duration\": 2.2,\r\n"
				+ "							\"instruction\": \"Biegen Sie links auf Steubenstraße ab\",\r\n"
				+ "							\"name\": \"Steubenstraße\",\r\n"
				+ "							\"type\": 0,\r\n"
				+ "							\"way_points\": [74, 77]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 12.3,\r\n"
				+ "							\"duration\": 2.9,\r\n"
				+ "							\"instruction\": \"Biegen Sie links auf Steubenstraße ab\",\r\n"
				+ "							\"name\": \"Steubenstraße\",\r\n"
				+ "							\"type\": 0,\r\n"
				+ "							\"way_points\": [77, 79]\r\n" + "						}, {\r\n"
				+ "							\"distance\": 0,\r\n" + "							\"duration\": 0,\r\n"
				+ "							\"instruction\": \"Sie erreichen Steubenstraße (links)\",\r\n"
				+ "							\"name\": \"\",\r\n" + "							\"type\": 10,\r\n"
				+ "							\"way_points\": [79, 79]\r\n" + "						}\r\n"
				+ "					]\r\n" + "				}\r\n" + "			],\r\n" + "			\"summary\": {\r\n"
				+ "				\"distance\": 2499,\r\n" + "				\"duration\": 306.1\r\n"
				+ "			},\r\n" + "			\"way_points\": [0, 79]\r\n" + "		}\r\n" + "	]\r\n" + "}\r\n"
				+ "");
		when(WebServiceUtil.getJSONResponse(anyString())).thenReturn(result);
		ServiceConnectorResult output = sc.queryService(new QueryCoordinates(1.1, 2.2, 3.3, 4.4));
		ServiceConnectorResult expected = new ServiceConnectorResult(new QueryCoordinates(1.1, 2.2, 3.3, 4.4));
		expected.setApiType("ORS");
		expected.setDistance(2499);
		expected.setDistanceStr("2,5 km");
		expected.setDuration(306);
		expected.setDurationStr("5m 6s");
		expected.setTimingMode(true);
		expected.setTimes(output.getTimes());
		assertEquals(expected.toString(), output.toString());
	}

}
