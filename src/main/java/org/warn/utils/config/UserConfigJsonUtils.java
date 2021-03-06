package org.warn.utils.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.file.FileOperations;
import org.warn.utils.json.JsonUtil;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UserConfigJsonUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserConfigJsonUtils.class);
			
	@SuppressWarnings("unchecked")
	public static ConcurrentMap<String, String> loadMapFromHomeDir( String... pathElements ) {
		ConcurrentMap<String, String> root = null;
		if( FileOperations.existsInHomeDir( pathElements ) ) {
			try {
				StringBuilder fileContents = FileOperations.readFromHomeDir( pathElements );
				if( fileContents!=null && !fileContents.toString().isEmpty() ) {
					root = JsonUtil.mapper.readValue( fileContents.toString(), ConcurrentMap.class );
					LOGGER.debug( "Loaded properties - " + root.toString() );
				}
			} catch( JsonParseException | JsonMappingException e ) {
				LOGGER.error("Error while parsing config json", e );
				
			} catch( IOException e ) {
				LOGGER.error("Error while reading config json", e );
			}
		}
		return root;
	}
	
	public static synchronized void updateMapInHomeDir( Map<String, String> propertyMap, String... pathElements ) {
		if( FileOperations.checkOrCreateDirInHomeDir( Arrays.copyOfRange( pathElements, 0, pathElements.length-1 ) ) ) {
			try {
				// https://www.mkyong.com/java/how-to-enable-pretty-print-json-output-jackson/
				FileOperations.writeToHomeDir( JsonUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(propertyMap), pathElements );
			} catch( JsonProcessingException e ) {
				LOGGER.error("Error while writing config json", e );
			}
		}
	}
	
	public static <T> List<T> loadListFromHomeDir( Class<T> c, String... pathElements ) {
		List<T> list = new ArrayList<T>();
		if( FileOperations.existsInHomeDir( pathElements ) ) {
			try {
				StringBuilder fileContents = FileOperations.readFromHomeDir( pathElements );
				JavaType type = JsonUtil.mapper.getTypeFactory().constructCollectionType( List.class, Class.forName(c.getName() ) );
				list = JsonUtil.mapper.readValue( fileContents.toString(), type );
				
			} catch( IOException e ) {
				LOGGER.error("Error while reading config json", e );
				
			} catch( ClassNotFoundException e ) {
				LOGGER.error("Error while reading config json", e );
			}
		}
		return list;
	}
	
	public static synchronized void updateListInHomeDir( List<?> list, String... pathElements ) {
		if( FileOperations.checkOrCreateDirInHomeDir( Arrays.copyOfRange( pathElements, 0, pathElements.length-1 ) ) ) {
			try {
				// https://www.mkyong.com/java/how-to-enable-pretty-print-json-output-jackson/
				FileOperations.writeToHomeDir( JsonUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list), pathElements );
			} catch( JsonProcessingException e ) {
				LOGGER.error("Error while writing config json", e );
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getListFromJsonString( String jsonString ) {
		List<String> list = new ArrayList<String>();
		if( jsonString != null && !jsonString.isEmpty() ) {
			try {
				list = JsonUtil.mapper.readValue( jsonString, List.class );
			} catch (JsonParseException | JsonMappingException e) {
				LOGGER.error("Error while parsing json property", e );
			} catch (IOException e) {
				LOGGER.error("Error while parsing json property", e );
			}
		}
		return list;
	}
	
	public static String getJsonString( Collection<String> collection ) {
		if( collection != null ) {
			try {
				return JsonUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString( collection );
			} catch (JsonProcessingException e) {
				LOGGER.error("Error while processing json string", e );
			}
		}
		return null;
	}

}
