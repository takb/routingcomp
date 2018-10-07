package de.kofk.routingcomp.conf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Configuration loading utility.
 * 
 * @author Takara Baumbach
 *
 */
public class Config {

	final static String CONFIG_FILE_NAME = "config.json";
	private JSONObject conf;

	/**
	 * Default constructor
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	public Config() throws JSONException, IOException {
		this(CONFIG_FILE_NAME);
	}

	/**
	 * Constructor that takes a configuration file name specified at runtime
	 * 
	 * @param filename
	 * @throws JSONException
	 * @throws IOException
	 */
	public Config(String filename) throws JSONException, IOException {
		if (filename == null) {
			filename = CONFIG_FILE_NAME;
		}
		File file = new File(filename);
		conf = new JSONObject(FileUtils.readFileToString(file, "utf-8"));
	}

	/**
	 * @param key
	 * @return Configuration value for the specified key or null if not present
	 */
	public Object getParam(String key) {
		return this.getParam(key, null);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return Configuration value for the specified key or default value if key not
	 *         present
	 */
	public Object getParam(String key, Object defaultValue) {
		return conf.has(key) ? conf.get(key) : defaultValue;
	}
}
