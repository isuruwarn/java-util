package org.warn.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaURLConnection {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( JavaURLConnection.class );
	
	public StringBuilder getContent( String urlString ) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(urlString);
			//Proxy proxy = new Proxy( Proxy.Type.HTTP, new InetSocketAddress( "proxy", 8080 ) );
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
			String inputLine;
			while( (inputLine = in.readLine()) != null ) { 
				sb.append(inputLine + "\n");
			}
			in.close();
			LOGGER.debug( sb.toString() );
			
		} catch( MalformedURLException e ) {
			LOGGER.error("Invalid URL", e);
		} catch( IOException e ) {
			LOGGER.error("Error while openning connection", e);
		}
		return sb;
	}

}