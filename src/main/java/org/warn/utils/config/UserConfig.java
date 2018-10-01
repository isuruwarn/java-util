package org.warn.utils.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.file.FileOperations;

/**
 * A reusable class for managing user configurations.
 * 
 * This class is not a singleton, and it does not use file locks. Therefore it does 
 * not guarantee thread safety. If you create more than one instance to manage a particular 
 * configuration file, it will not be thread safe. It is up to you manage concurrency 
 * while using this class. 
 * 
 * @author Isuru W
 *
 */
public final class UserConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( UserConfig.class );
	private static final String CONFIG_FILE_NAME_MISSING_ERR_MSG = "Please provide a config file name";
	//private static final String NOT_INITIALIZED_ERR_MSG = "User configuration has not been initialized";
	
	private String configFile;
	private ConcurrentMap<String, String> props;
	
	private UserConfig( String configFile, ConcurrentMap<String, String> defaultProps ) {
		if( configFile == null || configFile.equals("") ) {
			throw new IllegalArgumentException(CONFIG_FILE_NAME_MISSING_ERR_MSG);
		}
		this.configFile = configFile;
		if( FileOperations.exists( configFile ) ) {
			LOGGER.info("Loading configurations from file - " + configFile);
			this.props = UserConfigJsonUtils.loadMap(configFile);
		}
		if( this.props == null ) {
			LOGGER.info("Loading default configurations");
			this.props = new ConcurrentHashMap<String, String>();
			if( defaultProps != null ) {
				this.props.putAll( defaultProps );
			}
		}
	}
	
	public synchronized void updateConfig( String property, String value ) {
//		if( this.props == null ) {
//			throw new IllegalStateException(NOT_INITIALIZED_ERR_MSG);
//		}
		String existingValue = this.props.get(property);
		if( existingValue == null || existingValue.equals("") || !existingValue.equals(value) ) {
			LOGGER.debug("Updating Property={}, ExistingValue={}, NewValue={}", property, existingValue, value );
			this.props.put( property, value );
			UserConfigJsonUtils.updateMap( this.props, this.configFile );
		}
	}
	
	public String getProperty( String property ) {
//		if( this.props == null ) {
//			throw new IllegalStateException(NOT_INITIALIZED_ERR_MSG);
//		}
		return (String) this.props.get(property);
	}
	
}
