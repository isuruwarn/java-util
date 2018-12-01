package org.warn.utils.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		LOGGER.info("Loading configurations from file - " + this.configFilePath);
		this.props = UserConfigJsonUtils.loadMap( this.configFilePath );
		if( this.props == null ) {
			LOGGER.info("Could not find existing configurations file - " + Arrays.toString(this.configFilePath) );
			this.props = new ConcurrentHashMap<String, String>();
			if( defaultProps != null ) {
				LOGGER.info("Loading default configurations");
				this.props.putAll( defaultProps );
			}
		}
	}
	
	public synchronized void updateConfig( String property, String value ) {
		String existingValue = this.props.get(property);
		if( existingValue == null || existingValue.isEmpty() || !existingValue.equals(value) ) {
			LOGGER.debug("Updating Property={}, ExistingValue={}, NewValue={}", property, existingValue, value );
			this.props.put( property, value );
			UserConfigJsonUtils.updateMap( this.props, this.configFilePath );
		}
	}
	
	public synchronized void updateConfig( String property, Collection<String> value ) {
		String existingValue = this.props.get(property);
		if( existingValue == null || existingValue.isEmpty() || !existingValue.equals( value.toString() ) ) {
			LOGGER.debug("Updating Property={}, ExistingValue={}, NewValue={}", property, existingValue, value );
			this.props.put( property, value.toString() );
			UserConfigJsonUtils.updateMap( this.props, this.configFilePath );
		}
	}
	
	public String getProperty( String property ) {
		return (String) this.props.get(property);
	}
	
	public List<String> getListProperty( String property ) {
		List<String> list = null;
		String strList = (String) this.props.get(property);
		if( strList != null && !strList.isEmpty() ) {
			String [] arr = strList.split(",");
			list = Arrays.asList(arr);
		}
		return list;
	}
	
}
