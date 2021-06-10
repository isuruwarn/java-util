package org.warn.utils.file;

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
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.core.Env;

import static java.nio.file.StandardOpenOption.*;

public class FileOperations {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileOperations.class);
	
	public static boolean exists( String path, String... additionalPathElements ) {
		Path p = Paths.get( path, additionalPathElements );
		return Files.exists(p);
	}
	
	public static boolean existsInHomeDir( String... pathElements ) {
		return exists( Env.getUserHomeDir(), pathElements );
	}
	
	/**
	 * Returns true if given folder path exists. If it does not exist, attempts to create the given path elements
	 * and return true, and returns false if this fails.
	 * 
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return True or false
	 */
	public static boolean checkOrCreateDir( String path, String... additionalPathElements ) {
		Path p = Paths.get( path, additionalPathElements );
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
	
	public static boolean checkOrCreateDirInHomeDir( String... pathElements ) {
		return checkOrCreateDir( Env.getUserHomeDir(), pathElements );
	}
	
	/**
	 * Returns true if given file exists. If it does not exist, attempts to create the given file
	 * and return true, and returns false if this fails.
	 * 
	 * @param path Path string or initial part of the path string (mandatory)
	 * @param additionalPathElements Additional directory path elements
	 * @return True or false
	 */
	public static boolean checkOrCreateFile( String path, String... additionalPathElements ) {
		Path p = Paths.get( path, additionalPathElements );
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
	
	public static boolean checkOrCreateFileInHomeDir( String... pathElements ) {
		return checkOrCreateFile( Env.getUserHomeDir(), pathElements );
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
		return write( content, TRUNCATE_EXISTING, path, additionalPathElements );
	}

	public static boolean appendToFile( String content, String path, String... additionalPathElements ) {
		return write( content, APPEND, path, additionalPathElements );
	}

	public static boolean writeToHomeDir( String content, String... additionalPathElements ) {
		return write( content, Env.getUserHomeDir(), additionalPathElements );
	}

	public static boolean appendToFileInHomeDir( String content, String... additionalPathElements ) {
		return write( content, APPEND, Env.getUserHomeDir(), additionalPathElements );
	}

	private static boolean write(String content, StandardOpenOption option, String path, String... additionalPathElements ) {

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
		try ( BufferedOutputStream out = new BufferedOutputStream( Files.newOutputStream( p, CREATE, option ) ) ) {
			out.write( data, 0, data.length );
			return true;
		} catch( IOException e ) {
			LOGGER.error("Error while writing file to local directory", e );
			return false;
		}
	}
	
	/**
	 * Copy source file to specified target. Overwrites existing file. Returns null in case of failure.
	 * 
	 * @param source {@link Path} of source file
	 * @param target {@link Path} of target file
	 * @return {@link Path} of copied file or null in case of failure
	 * @throws IOException 
	 */
	public static Path copy( Path source, Path target ) throws IOException {
		return Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES );
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
	
	public static int deleteFiles( String path, String pattern ) {
		
		int deletedCount = 0;
		Path dir = Paths.get(path);
		Finder finder = new Finder(pattern);
		try {
			Files.walkFileTree( dir, finder );
		} catch (IOException e) {
			LOGGER.error("Error while scanning files for deletion", e);
		}
		
		for( Path p: finder.getMatchedFiles() ) {
			LOGGER.debug("DELETING FILE - " + p);
			try {
				Files.delete(p);
				deletedCount++;
			} catch( IOException e ) {
				LOGGER.error("Error while deleting files", e);
			}
		}
		
		LOGGER.debug("Deleted " + deletedCount + " file(s)..");
		return deletedCount;
	}
}
