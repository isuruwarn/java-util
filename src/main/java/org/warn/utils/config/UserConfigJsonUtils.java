package org.warn.utils.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.file.FileOperations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserConfigJsonUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserConfigJsonUtils.class);
			
	@SuppressWarnings("unchecked")
	public static ConcurrentMap<String, String> loadMap( String... pathElements ) {
		ConcurrentMap<String, String> root = null;
		if( FileOperations.existsInHomeDir( pathElements ) ) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				StringBuilder fileContents = FileOperations.readFromHomeDir( pathElements );
				if( fileContents!=null && !fileContents.equals("") ) {
					root = mapper.readValue( fileContents.toString(), ConcurrentMap.class );
					LOGGER.debug( "Loaded properties - " + root.toString() );
				}
			} catch( JsonParseException e ) {
				LOGGER.error("Error while parsing config json", e );
				
			} catch( JsonMappingException e ) {
				LOGGER.error("Error while parsing config json", e );
				
			} catch( IOException e ) {
				LOGGER.error("Error while reading config json", e );
			}
		}
		return root;
	}
	
	public static synchronized void updateMap( Map<String, String> propertyMap, String... pathElements ) {
		if( FileOperations.checkOrCreateFileInHomeDir( pathElements ) ) {
			try {
				// https://www.mkyong.com/java/how-to-enable-pretty-print-json-output-jackson/
				ObjectMapper mapper = new ObjectMapper();
				FileOperations.writeToHomeDir( mapper.writerWithDefaultPrettyPrinter().writeValueAsString(propertyMap), pathElements );
			} catch( JsonProcessingException e ) {
				LOGGER.error("Error while writing config json", e );
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> loadList( String... pathElements ) {
		List<String> list = null;
		if( FileOperations.existsInHomeDir( pathElements ) ) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				StringBuilder fileContents = FileOperations.readFromHomeDir( pathElements );
				if( fileContents!=null && !fileContents.equals("") ) {
					list = mapper.readValue( fileContents.toString(), List.class );
					LOGGER.debug( "Loaded properties - " + list.toString() );
				}
			} catch( JsonParseException e ) {
				LOGGER.error("Error while parsing config json", e );
				
			} catch( JsonMappingException e ) {
				LOGGER.error("Error while parsing config json", e );
				
			} catch( IOException e ) {
				LOGGER.error("Error while reading config json", e );
			}
		}
		return list;
	}
	
	public static synchronized void updateList( List<String> list, String... pathElements ) {
		if( FileOperations.checkOrCreateFileInHomeDir( pathElements ) ) {
			try {
				// https://www.mkyong.com/java/how-to-enable-pretty-print-json-output-jackson/
				ObjectMapper mapper = new ObjectMapper();
				FileOperations.writeToHomeDir( mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list), pathElements );
			} catch( JsonProcessingException e ) {
				LOGGER.error("Error while writing config json", e );
			}
		}
	}

}
