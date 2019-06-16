package org.warn.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesHelper {

	/**
	 * This method will load property files from the src/main/resources folder.
	 * 
	 * @param fileName File name and path relative to the src/main/resources folder
	 * @return A {@link Properties} object
	 */
	public static Properties loadFromResourcesDir( String fileName ) {
		Properties prop = new Properties();
		try( InputStream input = PropertiesHelper.class.getClassLoader().getResourceAsStream(fileName) ) {
			if( input != null) {
				prop.load(input);
			}
		} catch( IOException e ) {
			log.error("Error while loading property file - {}", fileName, e);
		}
		return prop;
	}
}
