
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * This class represents the main entry point of the program.
 * 
 */
public class History extends Thread {

	// Paths to the source and target directories
	private static String sourcePath;
	private static String targetPath;

	// List of files to ignore during synchronization
	private static List<String> ignoredFiles = new ArrayList<>();

	// Flags to control application behavior
	private static boolean start = true;
	private static boolean gui = false;

	// Logger object for logging application messages
	private static Logger logger = Logger.getLogger("History");
	ConsoleHandler handler = new ConsoleHandler();

	// Configuration object for loading application settings
	private static Config config;

	/**
	 * Main method that starts the application.
	 * 
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {

		// Uncomment the following line to test the application with a pre-defined set of arguments
		//args = "gui&&/Users/loicderghoum/Desktop/source&&/Users/loicderghoum/Desktop/target&&[.DS_Store, *.html, style.css]".split("&&");
		
		// Create a new history object
		History h = new History();

		// Initialize the logger
		h.initLogger();

		// Load application settings from the configuration file
		config = new Config("config.properties");

		// Process command line arguments
		h.processArgs(args);

		// If the GUI flag is set, draw the GUI
		if(gui)
		{
			@SuppressWarnings("unused")
			GUI gui = new GUI();
		}

		// Create a new thread to run the synchronization process
		Thread thread = new Thread(() -> {
			logger.log(Level.INFO, "Listening...");

			// Run an infinite loop while the application is running
			while (true) {
				if(start)
				{
					// Call the synchronize function
					/*
					 * 
					 * We shall replace every "\" for "/" on Windows operating systems for error handling about files and folder
					 * because \ need to be escaped sometimes
					 * 
					 */
					synchronize(sourcePath.replaceAll("\\\\", "/"), targetPath.replaceAll("\\\\", "/"));					
				}

				try {
					// Pause the thread for 1000 milliseconds
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// Start the thread
		thread.start();

		// Add a shutdown hook to detect when the application is closing
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				// In case we need to save logs or other
				logger.log(Level.INFO, "Fin du processus");
			}
		});
	}

	/**
	*
    * This method synchronizes two directories, source and target.<br/>
    * It compares the contents of the two directories and identifies the files that need to be added to or deleted from the target directory.<br/>
    * The method logs any changes made during synchronization.<br/>
	*
    * @param _source the source directory to synchronize from
    * @param _target the target directory to synchronize to
    */
	public static void synchronize(String _source, String _target) {
		History h = new History();

		// List of files in source and target directories
		List<Doc> source = h.listContent(_source);
		List<Doc> target = h.listContent(_target);

		// List of files to add and to delete
		List<Doc> toAdd = h.difference(source, target);
		List<Doc> toDel = h.difference(target, source);

		// Add missing files to the target directory
		for (Doc doc : toAdd) {
			History.getLogger().log(Level.INFO, "Change detected on '" + doc.name + "'. File synchronized");
			doc.saveTo(_target);
		}

		// Delete files present in the target directory but not in the source directory
		for (Doc doc : toDel) {
			History.getLogger().log(Level.INFO, "Deletion detected on '" + doc.name + "'. File deleted");
			doc.deleteFrom(_target);
		}
	}

	/**
	 * Returns a list of all documents in the specified directory and its subdirectories.
	 * @param path the path of the directory to search
	 * @return a list of Doc objects representing each document found in the directory
	 */
	private List<Doc> listContent(String path) {
		path = path.replaceAll("\\\\", "/");
		File file = new File(path);
		List<Doc> docs = new ArrayList<>();

		// Browse all files in the directory and add them to the list
		for (File f : file.listFiles()) {
			if(f.isDirectory())
			{
				docs.addAll(listContent(f.getAbsolutePath().replaceAll("\\\\", "/")));
			}
			else
			{
				docs.add(new Doc(f.getAbsolutePath().replaceAll("\\\\", "/").replaceAll(getSourcePath(), "").replaceAll(getTargetPath(), ""), f.lastModified()));
			}
		}

		return docs;
	}

	/**
	 *
	 * This method calculates the difference between two lists of documents.<br/>
	 * It returns a new list that contains all the documents in the source list
	 * that are not present in the reference list and are not marked as ignored.
	 * 
	 * @param source the source list of documents
	 * @param reference the reference list of documents
	 * @return a new list of documents that are in the source list but not in the reference list
	 */
	private List<Doc> difference(List<Doc> source, List<Doc> reference) {
		List<Doc> diff = new ArrayList<>();

		for (Doc s : source) {
			if (!reference.contains(s) && !s.isIgnored())
				diff.add(s);
		}

		return diff;
	}


	/**
	 *
	 * This method initializes and configures the application logger by reading the
	 *
	 * logger configuration from the logging.properties file.<br/>
	 *
	 * It also uses a formatter to format log messages.<br/>
	 *
	 * Finally, it adds the configured handler to the logger.
	 */
	private void initLogger() {
		try {
			// Read logger configuration from logging.properties file
			LogManager.getLogManager()
			.readConfiguration(this.getClass().getClassLoader().getResourceAsStream("logging.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Configure formatter to format log messages
		SimpleFormatter formatter = new SimpleFormatter() {
			private static final String format = "[%1$tF %1$tT] [%2$s]: %3$s%n";

			@Override
			public synchronized String format(LogRecord lr) {
				return String.format(format, new Date(lr.getMillis()), lr.getLevel().getName(), lr.getMessage());
			}

		};

		// Configure handler and add to logger
		handler.setLevel(Level.ALL);
		handler.setFormatter(formatter);
		logger.addHandler(handler);
	}

	/**
	 *
	 * Returns the application logger.
	 * @return the logger instance.
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 *
	 * Processes command-line arguments and sets the appropriate fields.
	 *
	 * @param args the command-line arguments
	 */
	private void processArgs(String[] args) {

		String jarFilePath = History.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String jarFileName = new File(jarFilePath).getName();

		if (args.length == 0) {
			// By default, show the graphical interface
			start = false;
			gui = true;
			setIgnoredList(Arrays.asList(".DS_Store".split(", ")));
			return;
		}

		String mode = args[0];

		if (mode.equalsIgnoreCase("gui")) {
			start = false;
			gui = true;

			if (args.length >= 2) {
				setSourcePath(new File(args[1]).getAbsolutePath() + File.separator);
				setTargetPath(new File(args[2]).getAbsolutePath() + File.separator);
			}

			if (args.length <= 3 || args[3].isEmpty() || args[3].isBlank()) {
				setIgnoredList(Arrays.asList(".DS_Store".split(", ")));
			} else if (args.length > 3) {
				String ignored = args[3].substring(1, args[3].length() - 1);
				setIgnoredList(Arrays.asList(ignored.split(", ")));
			}

		} else if (mode.equalsIgnoreCase("nogui")) {
			if (args.length < 3) {
				logger.log(Level.INFO, "Usage: java -jar " + jarFileName + " [gui|nogui] [source] [target] [ignored files]");
				System.exit(1);
			}

			File sourceFolder = new File(args[1]);
	        File targetFolder = new File(args[2]);
			
			if (!sourceFolder.exists() || !targetFolder.exists()) {
                // Show an error message if source or target folder doesn't exist
                if (!sourceFolder.exists()) {
                	logger.severe("Folder '" + args[1] + "' not found!");
                	System.exit(1);
                }

                if (!targetFolder.exists()) {
                	logger.severe("Folder '" + args[2] + "' not found!");
                	System.exit(1);
                }
			}

			setSourcePath(sourceFolder + File.separator);
			setTargetPath(targetFolder + File.separator);

			if (args.length <= 3 || args[3].isEmpty() || args[3].isBlank()) {
				setIgnoredList(Arrays.asList(".DS_Store".split(", ")));
			} else if (args.length > 3) {
				String ignored = args[3].substring(1, args[3].length() - 1);
				setIgnoredList(Arrays.asList(ignored.split(", ")));
			}

		} else {
			logger.severe("Invalid mode: " + mode);
			System.exit(1);
		}
	}

	/**
	 * Returns the state of the process.
	 * 
	 * @return boolean The state of the process.
	 */
	public static boolean getProcessState() {
		return start;
	}

	/**
	 * Toggles the state of the process.
	 */
	public static void toggleProcess() {
		start = !start;
	}

	/**
	 * Starts the process.
	 */
	public static void startProcess() {
		start = true;
	}

	/**
	 * Stops the process.
	 */
	public static void stopProcess() {
		start = false;
	}

	/**
	 * Returns the configuration object.
	 * 
	 * @return Config The configuration object.
	 */
	public static Config getConfig() {
		return config;
	}

	/**
	 * Returns the source path.
	 * 
	 * @return String The source path.
	 */
	public static String getSourcePath() {
		return sourcePath;
	}

	/**
	 * Returns the target path.
	 * 
	 * @return String The target path.
	 */
	public static String getTargetPath() {
		return targetPath;
	}

	/**
	 * Returns the list of ignored files.
	 * 
	 * @return List<String> The list of ignored files.
	 */
	public static List<String> getIgnoredList() {
		return ignoredFiles;
	}

	/**
	 * Sets the source path.
	 * 
	 * @param path The new source path.
	 */
	public static void setSourcePath(String path) {
		logger.log(Level.INFO, "Source path set as '" + path + "'");
		sourcePath = path.replaceAll("\\\\", "/");
		config.setString("source", path);
	}

	/**
	 * Sets the target path.
	 * 
	 * @param path The new target path.
	 */
	public static void setTargetPath(String path) {
		logger.log(Level.INFO, "Target path set as '" + path + "'");
		targetPath = path.replaceAll("\\\\", "/");
		config.setString("target", path);
	}

	/**
	 * Sets the list of ignored files.
	 * 
	 * @param list The new list of ignored files.
	 */
	public static void setIgnoredList(List<String> list) {
		if(list.size() >= 1 && (list.get(0).isEmpty() || list.get(0).isBlank()))
			list = Arrays.asList(".DS_Store");
		
		logger.log(Level.INFO, "Ignored file list: " + list.toString());
		ignoredFiles = list;
		config.setString("ignore", list.toString().replace("[", "").replace("]", ""));
	}
}