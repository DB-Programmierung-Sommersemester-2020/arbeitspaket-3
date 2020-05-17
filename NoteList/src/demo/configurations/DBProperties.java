package demo.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBProperties {

	private String propertiesPath = "./../../config/database.properties";
	private static DBProperties instance = null;

	private DBProperties() {
		initProperties(propertiesPath);
	}

	private void initProperties(String path) {
		propertiesPath = path;
		InputStream inputStream = getClass().getResourceAsStream(propertiesPath);
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			DATASOURCE_URL = properties.getProperty("DATASOURCE_URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DBProperties getDBProperties() {
		if (DBProperties.instance == null) {
			DBProperties.instance = new DBProperties();
		}
		return DBProperties.instance;
	}

	public static String DATASOURCE_URL;
}
