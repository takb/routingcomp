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
public class GoogleServiceConnectorTest {

	private GoogleServiceConnector sc;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(WebServiceUtil.class);
		Logger.getGlobal().setLevel(Level.SEVERE);
		sc = (GoogleServiceConnector) ServiceConnectorFactory.getServiceConnector("google", "key", true,
				new JSONObject("{\"param\": \"value\"}"), Logger.getGlobal());
	}

	@Test
	public void ProcessesServiceResultCorrectly() {
		JSONObject result = new JSONObject("{\r\n" + "	\"geocoded_waypoints\": [{\r\n"
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
		when(WebServiceUtil.getJSONResponse(anyString())).thenReturn(result);
		QueryCoordinates query = new QueryCoordinates(1.1, 2.2, 3.3, 4.4);
		ServiceConnectorResult output = sc.queryService(query);
		ServiceConnectorResult expected = new ServiceConnectorResult(query);
		expected.setApiType("Google");
		expected.setDistance(661);
		expected.setDistanceStr("0.7 km");
		expected.setDuration(30);
		expected.setDurationStr("1 min");
		expected.setTimingMode(true);
		expected.setTimes(output.getTimes());
		assertEquals(expected.toString(), output.toString());
	}

}
