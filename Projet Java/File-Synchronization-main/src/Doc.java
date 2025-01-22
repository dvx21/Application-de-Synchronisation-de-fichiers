import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * Represents a document with its name and last modification date.
 */
public class Doc {

	// The name of the document.
	public String name;

	// The last modification date of the document in milliseconds since the epoch.
	public long date;

	/**
	 * Constructs a new Doc object with the given name and last modification date.
	 * 
	 * @param name The name of the document.
	 * @param date The last modification date of the document in milliseconds since the epoch.
	 */
	public Doc(String name, long date) {
		this.name = name;
		this.date = date;
	}

	/**
	 * Returns the string representation of the Doc object.
	 * @return the string representation of the Doc object.
	 */
	@Override
	public String toString() {
		//return name + "(" + new Date(date).toString() + ")";
		return name;
	}

	/**
	 * Compares two Doc objects for equality based on their name and date.
	 * @param other the object to compare to.
	 * @return true if the objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		Doc d = (Doc) other;
		return (name.equals(d.name) && date == d.date);
	}

	/**
	 * This method saves the document to the given directory. If the document is a directory, it creates it in the target directory.
	 * If the document is a file, it copies the content from the source directory to the target directory.
	 * It also sets the last modified date of the target document to be the same as the original document.
	 *
	 * @param dir The target directory where the document should be saved.
	 */
	public void saveTo(String dir) {
		// Get the source and target file objects
		File sourceFile = new File(History.getSourcePath() + name);
		File file = new File(dir + name);

		// Create necessary directories
		file.getParentFile().mkdirs();

		// If the document is a directory, create it in the target directory
		if (sourceFile.isDirectory()) {
			History.getLogger().log(Level.INFO, sourceFile.getAbsolutePath() + ": DIRECTORY");
			file.mkdir();
		} else {
			// If the document is a file, copy its content from the source directory to the target directory
			if (!file.exists()) {
				try {
					file.createNewFile();
					file.setLastModified(file.lastModified());
				} catch (IOException e) {
					History.getLogger().log(Level.SEVERE,
							"Error occurred while creating the file. Do you have the correct permissions?");
					History.getLogger().log(Level.SEVERE, e.getMessage());
					return;
				}
			}

			try {
				byte[] bytes = Files.readAllBytes(Path.of(sourceFile.getAbsolutePath()));
				Files.write(Path.of(file.getAbsolutePath()), bytes);
			} catch (IOException e) {
				History.getLogger().log(Level.SEVERE,
						"An error occurred while writing the file. Do you have the correct permissions?");
				History.getLogger().log(Level.SEVERE, e.getMessage());
			}

		}

		// Set the last modified date of the target document to be the same as the original document
		file.setLastModified(date);
	}

	/**
	 * Deletes the file corresponding to this Doc object from the specified directory.
	 *
	 * @param dir the directory to delete the file from
	 */
	public void deleteFrom(String dir) {
	    File file = new File(dir + name);
	    file.delete();
	}

	/**
	 * Checks whether this Doc object should be ignored according to the list of ignored files/directories.
	 *
	 * @return true if this Doc object should be ignored, false otherwise
	 */
	public boolean isIgnored() {
	    for (String ignoreValue : History.getIgnoredList()) {
	        String nameWithoutPath = new File(name).getName();
	        if (ignoreValue.charAt(0) == '*') {
	            // ignoreValue is a wildcard, so check if nameWithoutPath ends with the non-wildcard part
	            String nonWildcardPart = ignoreValue.substring(1);
	            if (nameWithoutPath.endsWith(nonWildcardPart)) {
	                return true;
	            }
	        } else {
	            // ignoreValue is a full file/directory name, check if the name matches exactly
	            if (nameWithoutPath.equals(ignoreValue) || name.equals(ignoreValue)) {
	                return true;
	            }
	            // check if the file is in an ignored directory
	            if (name.startsWith(ignoreValue + "/") || name.startsWith(ignoreValue + "\\")) {
	                return true;
	            }
	        }
	    }
	    return false;
	}


}
