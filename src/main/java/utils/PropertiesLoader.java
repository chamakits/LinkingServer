package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoader {
	private static final Logger log = LoggerFactory.getLogger(PropertiesLoader.class);
	public static Properties loadProperties(String propertiesLocation) {
		Properties result = null;
		InputStream in = null;	
		String workingDir = System.getProperty("user.dir");
		String path = workingDir + File.separator + propertiesLocation;
		File file = new File(path);
		log.info("Attempt to load properties file: " + file.getAbsolutePath().toString() + "\t File Exists = "  + file.exists());
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e1) {}
		
		if(in == null) {
			throw new RuntimeException("Unable to load properties file for " + propertiesLocation);
		}
		
		result = new Properties();
		try {
		result.load(in);
		} catch(IOException e) {
			throw new RuntimeException("Unable to read Properties file.");
		}
		if(result.size() < 2) {
			throw new RuntimeException("Properties file does not contain all the required parameters.  Properties file may be corrupt.");
		}
		return result;
	}

}
