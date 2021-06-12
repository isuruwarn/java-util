package org.warn.utils.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.core.StringHelper;

/**
 * A reusable class for managing user configurations. <br><br>
 * 
 * The configurations files will be read from or written to the user's home directory. <br><br>
 * 
 * This class is not a singleton, and it does not use file locks. Therefore it does 
 * not guarantee thread safety. If you create more than one instance to manage a particular 
 * configuration file, it will not be thread safe. For thread safety, ensure that only 
 * one instance is created (and exists) per configuration file.
 * 
 * @author Isuru W
 *
 */
public final class UserConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( UserConfig.class );
	private static final String CONFIG_FILE_NAME_MISSING_ERR_MSG = "Please provide a config file name";
	
	private String [] configFilePath;
	private ConcurrentMap<String, String> props;
	
	/**
	 * Initializes the user configurations by attempting load the provided configurations file into an internal
	 * property map.
	 * 
	 * @param defaultProps Default properties
	 * @param configFilePath Relative path elements of configuration file, which will be created in the user's home directory. 
	 */
	public UserConfig( ConcurrentMap<String, String> defaultProps, String... configFilePath ) {
		if( configFilePath == null || configFilePath.length == 0 || 
			configFilePath[0] == null || String.join( ",", configFilePath ).isEmpty() ) {
			throw new IllegalArgumentException(CONFIG_FILE_NAME_MISSING_ERR_MSG);
		}
		this.configFilePath = configFilePath;
		LOGGER.debug("Loading configurations from file - {}", StringHelper.arrayToString( this.configFilePath ) );
		this.props = UserConfigJsonUtils.loadMapFromHomeDir( this.configFilePath );
		if( this.props == null ) {
			LOGGER.debug("Could not find existing configurations file - " + Arrays.toString(this.configFilePath) );
			this.props = new ConcurrentHashMap<String, String>();
			if( defaultProps != null ) {
				LOGGER.debug("Loading default configurations");
				this.props.putAll( defaultProps );
			}
		}
	}
	
	public synchronized void updateConfig( String property, String value ) {
		String existingValue = this.props.get(property);
		if( existingValue == null || existingValue.isEmpty() || !existingValue.equals(value) ) {
			LOGGER.debug("Updating Property={}, ExistingValue={}, NewValue={}", property, existingValue, value );
			this.props.put( property, value );
			UserConfigJsonUtils.updateMapInHomeDir( this.props, this.configFilePath );
		}
	}
	
	public synchronized void updateConfig( String property, Collection<String> updatedValues ) {
		if( updatedValues != null ) {
			String jsonString = UserConfigJsonUtils.getJsonString(updatedValues);
			String existingValue = this.props.get(property);
			if( existingValue == null || existingValue.isEmpty() || !existingValue.equals( jsonString ) ) {
				LOGGER.debug("Updating Property={}, NewValue={}", property, jsonString );
				this.props.put( property, jsonString );
				UserConfigJsonUtils.updateMapInHomeDir( this.props, this.configFilePath );
			}
		}
	}
	
	public String getProperty( String property ) {
		return this.props.get( property );
	}
	
	public List<String> getListProperty( String propName ) {
		String propValue = this.props.get( propName );
		return UserConfigJsonUtils.getListFromJsonString( propValue );
	}
	
}
