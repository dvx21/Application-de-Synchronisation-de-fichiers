/**
 * An enumeration that represents the application configuration.
 * This enumeration follows the Singleton pattern and provides access to various configuration parameters.
 */
public enum AppConfig {
    /**
     * The only instance of AppConfig.
     */
    INSTANCE;

    /**
     * The name of the application.
     */
    private final String appName = "File Synchronization";

    /**
     * The version number of the application.
     */
    private final String appVersion = "1.0";

    /**
     * The author(s) of the application.
     */
    private final String[] appAuthor = "DERGHOUM Loïc, ZEKARI Amr".split(", ");

    /**
     * The path to the application's icon.
     */
    private final String appIconPath = "";

    /**
     * Returns the name of the application.
     *
     * @return The name of the application.
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Returns the version number of the application.
     *
     * @return The version number of the application.
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Returns the authors of the application.
     *
     * @return The authors of the application.
     */
    public String[] getAppAuthor() {
        return appAuthor;
    }

    /**
     * Returns the path to the application's icon.
     * 
     * Maybe Later!
     *
     * @return The path to the application's icon.
     */
    public String getAppIconPath() {
        return appIconPath;
    }
}

