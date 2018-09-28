package org.warn.utils.file;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.core.Env;

public class FileOperations {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileOperations.class);
	
	public static boolean exists( String path, String... additionalPathElements ) {
		Path p = Paths.get( path, additionalPathElements );
		return Files.exists(p);
	}
	
	public static boolean existsInHomeDir( String... pathElements ) {
		Path p = Paths.get( Env.getUserHomeDir(), pathElements );
		return Files.exists(p);
	}
	
	/**
	 * Returns true if given folder path exists. If it does not exist, attempts to create the given path elements
	 * and return true, and returns false if this fails.
	 * 
	 * @param pathElements Directory path elements
	 * @return True or false
	 */
	public static boolean checkOrCreateDir( String... pathElements ) {
		Path p = Paths.get( Env.getUserHomeDir(), pathElements );
		try {
			if( !Files.exists(p) ) {
				Files.createDirectories(p);
			}
			return Files.exists(p);
		} catch (IOException e ) {
			LOGGER.error("Error while checking or creating directory", e );
			return false;
		}
	}
	
	/**
	 * Returns true if given file exists. If it does not exist, attempts to create the given path elements
	 * and return true, and returns false if this fails.
	 * 
	 * @param pathElements Directory path elements
	 * @return True or false
	 */
	public static boolean checkOrCreateFile( String... pathElements ) {
		Path p = Paths.get( Env.getUserHomeDir(), pathElements );
		try {
			if( !Files.exists(p) ) {
				Files.createFile(p);
			}
			return Files.exists(p);
		} catch (IOException e ) {
			LOGGER.error("Error while checking or creating file", e );
			return false;
		}
	}
	
	/**
	 * Read resource from classpath. Returns null if file is not found or an 
	 * exception is encountered.
	 * 
	 * @param path Resource path
	 * @return File contents as {@link StringBuilder} object
	 */
	public static StringBuilder readResource( String path ) {
		StringBuilder fileContent = null;
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader( FileOperations.class.getResourceAsStream( path ) ) );
			fileContent = new StringBuilder();
			String line;
			while( ( line = br.readLine() ) != null ) {
				fileContent.append(line);
			}
			br.close();
		} catch (IOException e) {
			LOGGER.debug("Error while loading page", e );
		}
		return fileContent;
	}
	
	/**
	 * Reads specified file and returns contents as a {@link StringBuilder} object. Returns null if file is not found or an 
	 * exception is encountered.
	 * 
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return File contents as {@link StringBuilder} object
	 */
	public static StringBuilder read( String path, String... additionalPathElements ) {
		Path p = Paths.get( path, additionalPathElements );
		StringBuilder sb = null;
		Charset charset = Charset.forName("UTF8");
		try ( BufferedReader reader = Files.newBufferedReader(p, charset) ) {
			sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch( NoSuchFileException e ) {
			LOGGER.error("File not found in local directory - " + e.getMessage() );
			
		} catch (IOException e) {
			LOGGER.error("Error while reading file from local directory", e );
		}
		return sb;
	}
	
	/**
	 * Reads specified file from the users local directory and returns contents as a {@link StringBuilder} object. 
	 * Returns null if file is not found or an exception is encountered.
	 * 
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return File contents as {@link StringBuilder} object
	 */
	public static StringBuilder readFromHomeDir( String... additionalPathElements ) {
		return read( Env.getUserHomeDir(), additionalPathElements );
	}
	
	/**
	 * Reads specified file and returns contents as a list. Returns null if file is not found or an 
	 * exception is encountered.
	 * 
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return File contents as {@link List<String>} object
	 */
	public static List<String> readLines( String path, String... additionalPathElements ) {
		StringBuilder sb = read( path, additionalPathElements );
		if( sb != null ) {
			String [] arrLines = sb.toString().split("\n");
			return Arrays.asList(arrLines);
		}
		return Arrays.asList();
	}
	
	/**
	 * Writes given file content to the specified path in the users local directory.
	 * 
	 * Note - Overwrites existing file of same name
	 * 
	 * @param content File content
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return True if successful, false during failure
	 */
	public static boolean write( String content, String path, String... additionalPathElements ) {
		
		if( content == null || content.equals("") ) {
			return false;
		}
		if( path == null || path.equals("") ) {
			return false;
		}
		
		byte data[] = null;
		try {
			data = content.getBytes("UTF8");
		} catch( UnsupportedEncodingException e ) {
			LOGGER.error("Error while writing file to local directory", e );
		}
		
	    Path p = Paths.get( path, additionalPathElements );
	    try ( BufferedOutputStream out = new BufferedOutputStream( Files.newOutputStream( p, CREATE, TRUNCATE_EXISTING ) ) ) {
	    	out.write( data, 0, data.length );
	    	return true;
	    } catch( IOException e ) {
	    	LOGGER.error("Error while writing file to local directory", e );
	    	return false;
	    }
	}
	
	public static boolean writeToHomeDir( String content, String... additionalPathElements ) {
		return write( content, Env.getUserHomeDir(), additionalPathElements );
	}
	
	/**
	 * Deletes all files in specified directory without deleting the directory.
	 * 
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return True if deletion succeeds, false if it fails
	 */
	public static boolean clearDir( String path, String... additionalPathElements ) {
		Path p = Paths.get( path, additionalPathElements );
		try {
			FileUtils.cleanDirectory( p.toFile() );
			return true;
		} catch (IllegalArgumentException e) {
			LOGGER.error("Error while clearing cache", e );
		} catch (IOException e) {
			LOGGER.error("Error while clearing cache", e );
		}
		return false;
	}
	
	/**
	 * Deletes all files in specified directory (located within users home directory) 
	 * without deleting the directory.
	 * 
	 * @param pathElements Directory path elements
	 * @return True if deletion succeeds, false if it fails
	 */
	public static boolean clearDirInHomeDir( String... pathElements ) {
		return clearDir( Env.getUserHomeDir(), pathElements );
	}

}
