import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 * A class to handle a configuration file using Properties.
 */
public class Config {

    private Properties properties;

    /**
     * Creates a Config instance and loads the specified configuration file.
     *
     * @param name the name of the configuration file
     */
    public Config(String name) {
        properties = new Properties();
        File configFile = new File(name);
        try {
            if (configFile.exists()) {
                // Load the properties from the existing configuration file
                properties.load(new FileInputStream(configFile));
                History.getLogger().log(Level.INFO, "Loaded previous configuration file: " + name);
            } else {
                // Create a new configuration file if it does not exist
                configFile.createNewFile();
                OutputStream outputStream = new FileOutputStream(configFile);
                properties.store(outputStream, "Configuration Properties");
                outputStream.close();
                History.getLogger().log(Level.INFO, "Created new configuration file");
            }
        } catch (IOException e) {
            History.getLogger().log(Level.SEVERE, "Unable to load or create configuration file: " + e);
            throw new RuntimeException("Unable to load or create config.properties file", e);
        }
    }

    /**
     * Gets the string value associated with the specified key.
     *
     * @param key the key of the property
     * @return the string value associated with the key, or null if the key is not found
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets the integer value associated with the specified key.
     *
     * @param key the key of the property
     * @return the integer value associated with the key
     * @throws NumberFormatException if the value cannot be parsed as an integer
     */
    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    /**
     * Gets the double value associated with the specified key.
     *
     * @param key the key of the property
     * @return the double value associated with the key
     * @throws NumberFormatException if the value cannot be parsed as a double
     */
    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    /**
     * Gets the boolean value associated with the specified key.
     *
     * @param key the key of the property
     * @return the boolean value associated with the key
     * @throws NullPointerException if the value is null
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    /**
     * Sets the string value associated with the specified key and saves the changes to the configuration file.
     *
     * @param key the key of the property
     * @param value the value to set
     */
    public void setString(String key, String value) {
        properties.setProperty(key, value);
        save();
    }

    /**
     * Sets the integer value associated with the specified key and saves the changes to the configuration file.
     *
     * @param key the key of the property
     * @param value the value to set
     */
    public void setInt(String key, int value) {
        setString(key, Integer.toString(value));
    }

    /**
     * Sets the double value associated with the specified key and saves the changes to the configuration file.
     *
     * @param key the key of the property
     * @param value the value to set
     */
    public void setDouble(String key, double value) {
        setString(key, Double.toString(value));
    }

    /**
     * Sets the boolean value associated with the specified key and saves the changes to the configuration file.
     *
     * @param key the key of the property
     * @param value the value to set
     */
    public void setBoolean(String key, boolean value) {
        setString(key, Boolean.toString(value));
    }

    /**
     * Saves the properties to the config.properties file
     */
    private void save() {
        try {
            OutputStream outputStream = new FileOutputStream("config.properties");
            properties.store(outputStream, null);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save config.properties file", e);
        }
    }
}