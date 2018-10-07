package de.kofk.routingcomp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.kofk.routingcomp.conf.Config;
import de.kofk.routingcomp.service.ServiceConnector;
import de.kofk.routingcomp.service.ServiceConnectorFactory;
import de.kofk.routingcomp.service.ServiceConnectorResult;

/**
 * Simple routing service comparison tool.
 * 
 * @author Takara Baumbach
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger("RoutingComp");

	public static void main(String[] args) {

		Config config = null;
		String configFile = null;
		String sourceFile = null;
		boolean debugMode = false;
		boolean timingMode = false;

		// logger settings
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");
		LOGGER.setLevel(Level.WARNING);

		// CLI
		Options options = makeOptions();
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			debugMode = cmd.hasOption("debug");
			timingMode = cmd.hasOption("timing");
			configFile = cmd.getOptionValue("config");
			sourceFile = cmd.getArgs()[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			// no CSV file parameter given, fail later if also not configured in
			// configuration file
		} catch (ParseException e) {
			String errorMsg = e.getMessage();
			printError(errorMsg, options, 1, debugMode, e);
		}

		// configuration file
		try {
			config = new Config(configFile);
			debugMode = debugMode || (Boolean) config.getParam("debug");
			timingMode = timingMode || (Boolean) config.getParam("timing");
			if (sourceFile == null) { // call parameter overrides configuration file setting
				sourceFile = (String) config.getParam("source");
			}
		} catch (IOException e) {
			String errorMsg = "Could not open configuration file: " + e.getMessage();
			printError(errorMsg, null, 1, debugMode, e);
		} catch (JSONException e) {
			String errorMsg = "Invalid JSON in configuration file: " + e.getMessage();
			printError(errorMsg, null, 1, debugMode, e);
		} catch (ClassCastException e) {
			String errorMsg = "Invalid configuration value: " + e.getMessage();
			printError(errorMsg, null, 1, debugMode, e);
		}
		if (debugMode) {
			LOGGER.setLevel(Level.ALL);
		}
		LOGGER.info(String.format("Configuration loaded - debug mode: %s, timing mode: %s", config.getParam("debug"),
				config.getParam("timing")));

		// load CSV
		if (sourceFile == null || sourceFile.isEmpty()) {
			String errorMsg = "No CSV input file specified.";
			printError(errorMsg, options, 1, debugMode, null);
		}
		String csvData = null;
		try {
			csvData = FileUtils.readFileToString(new File(sourceFile), "utf-8");
		} catch (FileNotFoundException e) {
			String errorMsg = "CSV input file could not be opened.";
			printError(errorMsg, null, 1, debugMode, e);
		} catch (IOException e) {
			String errorMsg = "CSV input file could not be read.";
			printError(errorMsg, null, 1, debugMode, e);
		}
		ArrayList<QueryCoordinates> coordinates = loadCoordinatesFromCSVString(csvData, debugMode);
		LOGGER.info(String.format("CSV source file loaded: %s contained %s records", sourceFile, coordinates.size()));

		// load Connectors
		ArrayList<ServiceConnector> connectors = loadServiceConnectors(config, timingMode, debugMode);
		LOGGER.info(String.format("Service connectors loaded: %s APIs", connectors.size()));

		// query & output
		HashMap<String, Double> requestTimesTotalByAPI = new HashMap<String, Double>();
		HashMap<String, Integer> numResultsByAPI = new HashMap<String, Integer>();
		for (QueryCoordinates c : coordinates) {
			LOGGER.info(String.format("Processing record no. %s - coordinates: %s, %s => %s, %s", coordinates.indexOf(c) + 1,
					c.startLongitude, c.startLatitude, c.endLongitude, c.endLatitude));
			for (ServiceConnector sc : connectors) {
				ServiceConnectorResult result = sc.queryService(c);
				System.out.println(result);
				if (result.isTimingMode() && !result.hasError()) {
					String key = result.getApiType();
					requestTimesTotalByAPI.put(key,
							(requestTimesTotalByAPI.getOrDefault(key, 0.0)) + result.getRequestTime());
					numResultsByAPI.put(key, (numResultsByAPI.getOrDefault(key, 0)) + 1);
				}
			}
		}
		System.out.println("Average request time: ");
		for (String key : requestTimesTotalByAPI.keySet()) {
			double avgTime = requestTimesTotalByAPI.get(key) / numResultsByAPI.getOrDefault(key, 1);
			System.out.println(String.format("%1$10s ", key) + String.format("%20.6f ms ", avgTime) + String.format(" / %5d call(s)", numResultsByAPI.getOrDefault(key, 1)));
		}
	}

	/**
	 * Set up CLI options
	 * 
	 * @return Options options
	 */
	private static Options makeOptions() {
		Options options = new Options();

		Option configOption = new Option("c", "config", true, "configuration file path");
		configOption.setRequired(false);
		configOption.setArgName("file");
		options.addOption(configOption);

		Option debugOption = new Option("d", "debug", false, "debug mode");
		debugOption.setRequired(false);
		options.addOption(debugOption);

		Option timingOption = new Option("t", "timing", false, "timing mode");
		timingOption.setRequired(false);
		options.addOption(timingOption);

		return options;
	}

	/**
	 * Load ServiceConnector instances according to configuration
	 * 
	 * @param conf
	 * @param timingMode
	 * @param debugMode
	 * @return ArrayList<ServiceConnector>
	 */
	private static ArrayList<ServiceConnector> loadServiceConnectors(Config conf, boolean timingMode,
			boolean debugMode) {
		ArrayList<ServiceConnector> connectors = new ArrayList<ServiceConnector>();
		try {
			JSONArray apis = (JSONArray) conf.getParam("apis");
			for (Object api : apis) {
				if (api instanceof JSONObject && ((JSONObject) api).has("type") && ((JSONObject) api).has("key")) {
					String apiType = ((JSONObject) api).getString("type");
					String apiKey = ((JSONObject) api).getString("key");
					LOGGER.info(String.format("Loading connector type %s with API key %s", apiType, apiKey));
					JSONObject params = null;
					try {
						params = ((JSONObject) api).getJSONObject("params");
						LOGGER.info("API has optional params: " + params);
					} catch (Exception e) {
						LOGGER.info("API has no optional params defined.");
					}
					connectors.add(
							ServiceConnectorFactory.getServiceConnector(apiType, apiKey, timingMode, params, LOGGER));
				} else {
					LOGGER.warning("Invalid API specification: " + api);
				}
			}
		} catch (ClassCastException e) {
			String errorMsg = "Invalid configuration of APIs: " + e.getMessage();
			printError(errorMsg, null, 1, debugMode, e);
		}
		return connectors;
	}

	/**
	 * Load from CSV file and generate coordinates objects
	 * 
	 * @param sourceFile
	 * @param debugMode
	 * @return ArrayList<QueryCoordinates>
	 */
	private static ArrayList<QueryCoordinates> loadCoordinatesFromCSVString(String csvStr, boolean debugMode) {
		ArrayList<QueryCoordinates> records = new ArrayList<QueryCoordinates>();
		try {
			CSVParser csv = CSVParser.parse(csvStr,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			for (CSVRecord csvRecord : csv) {
				try {
					double startLon = Double.parseDouble(csvRecord.get("start_lon"));
					double startLat = Double.parseDouble(csvRecord.get("start_lat"));
					double endLon = Double.parseDouble(csvRecord.get("end_lon"));
					double endLat = Double.parseDouble(csvRecord.get("end_lat"));
					records.add(new QueryCoordinates(startLon, startLat, endLon, endLat));
				} catch (NumberFormatException e) {
					// ignore invalid rows and try to read all other...
					String errorMsg = "CSV input file seems to be formatted incorrectly. Every data field has to contain a float value.";
					printError(errorMsg, null, 0, debugMode, e);

				}
			}
		} catch (IOException e) {
			String errorMsg = "CSV input file could not be read. Make sure the separator character is a comma: ','";
			printError(errorMsg, null, 1, debugMode, e);
		} catch (IllegalStateException e) {
			String errorMsg = "CSV input file seems to be formatted incorrectly. Make sure the file has a header row like \"start_lon\",\"start_lat\",\"end_lon\",\"end_lat\".";
			printError(errorMsg, null, 1, debugMode, e);
		} catch (IllegalArgumentException e) {
			String errorMsg = "CSV input file seems to be formatted incorrectly. Make sure the file has a header row like \"start_lon\",\"start_lat\",\"end_lon\",\"end_lat\".";
			printError(errorMsg, null, 1, debugMode, e);
		}
		return records;
	}

	/**
	 * Print error (and exit if exitCode > 0)
	 * 
	 * @param msg       Error message to print
	 * @param options   if not null, print usage info
	 * @param exitCode  if > 0, exit
	 * @param debugMode
	 * @param e         if not null and debugMode is true, print stack trace
	 */
	private static void printError(String msg, Options options, int exitCode, boolean debugMode, Exception e) {
		System.out.println(msg);
		if (e != null && debugMode) {
			e.printStackTrace();
		}
		if (options != null) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("routingcomp [OPTIONS] [CSV input file]", options);
		}
		if (exitCode > 0) {
			System.exit(exitCode);
		}
	}
}
