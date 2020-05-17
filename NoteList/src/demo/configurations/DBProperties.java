package demo.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DBProperties {

    private String propertiesPath="./../../properties/db.properties";

    private void initProperties(String path) {
        propertiesPath = path;
        InputStream inputStream = getClass().getResourceAsStream(propertiesPath);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            URL=properties.getProperty("URL");
            PORT=properties.getProperty("PORT");
            DATABASE=properties.getProperty("DATABASE");
            USER_NAME=properties.getProperty("USER_NAME");
            USER_PASSWORD = properties.getProperty("USER_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DBProperties() {
        initProperties(propertiesPath); 
    }

    public String URL;
    public String PORT;
    public String DATABASE;
    public String USER_NAME;
    public String USER_PASSWORD;

    
}
