package org.warn.utils.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warn.utils.core.Constants;
import org.warn.utils.file.FileOperations;

public class CustomHttpClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpClient.class);
	
	public StringBuilder get( String url ) {
		// TODO provide option of using proxy
		URLConnection con = null;
		StringBuilder webContent = new StringBuilder();
		String respStatus = null;
		try {
			LOGGER.debug("Request URL - " + url);
			URL urlObj = new URL(url);
			con = urlObj.openConnection();
			setCommonHeaders(con);
			
			BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			respStatus = getReponseStatus( con );
			LOGGER.debug("Response Status - " + respStatus );
			String input;
			while( (input = br.readLine()) != null ) {
				webContent.append(input);
			}
			br.close();
			
		} catch( MalformedURLException e ) {
			LOGGER.error("Invalid URL", e);
			webContent.append( FileOperations.readResource( Constants.UNKNOWN_HOST_ERROR_HTML ) );
			
		} catch( FileNotFoundException e ) {
			LOGGER.error("Invalid URL", e);
			webContent.append( FileOperations.readResource( Constants.NOT_FOUND_ERROR_HTML ) );
			
		} catch( UnknownHostException e ) {
			LOGGER.error("Invalid URL", e);
			webContent.append( FileOperations.readResource( Constants.UNKNOWN_HOST_ERROR_HTML ) );
			
		} catch( IOException e ) {
			LOGGER.error("Error while connecting to website", e);
			if( webContent == null || webContent.toString().isEmpty() ) {
				webContent.append( FileOperations.readResource( Constants.RENDERING_ERROR_HTML ) );
			} else {
				webContent.append( respStatus );
			}
		}
		return webContent;
	}
	
	/**
	 * http://www.java2s.com/Code/Java/Network-Protocol/SendingaPOSTRequestUsingaURL.htm
	 * 
	 * @param url
	 * @param body
	 * @return
	 */
	public StringBuilder postAndGet( String url, StringBuilder body ) {
		StringBuilder webContent = new StringBuilder();
		String respStatus = null;
		try {
			// TODO provide option of using proxy
			//URL urlObj = new URL( protocol, proxy, port, url );
			URL urlObj = new URL( url );
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			//con.setRequestMethod("POST");
			con.setDoInput (true);
			con.setDoOutput (true);
			setCommonHeaders(con);
			
			body = new StringBuilder( URLEncoder.encode( body.toString(), "UTF-8" ) );
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write( body.toString() );
		    wr.flush();
		    wr.close();
		    
			BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			respStatus = getReponseStatus( con );
			LOGGER.debug("Response Status - " + respStatus );
			String input;
			while( (input = br.readLine()) != null ) {
				webContent.append(input);
			}
			br.close();
			
		} catch( IOException e ){
			LOGGER.error("Error while connecting to website", e);
			if( webContent == null || webContent.toString().isEmpty() ) {
				webContent.append( FileOperations.readResource( Constants.RENDERING_ERROR_HTML ) );
			} else {
				webContent.append( respStatus );
			}
		}
		return webContent;
	}
	
	public StringBuilder getHttpsWithSSLInfo( String url ) {
		StringBuilder webContent = new StringBuilder();
		String respStatus = null;
		try {
			// TODO provide option of using proxy
			//System.setProperty("proxySet", "true");
			//System.setProperty("proxyHost", "");
			//System.setProperty("proxyPort", "");
			System.setProperty("java.protocol.handler.pkgs", "com.ibm.net.ssl.internal.www.protocol");
			
			URL urlObj = new URL(url);
			HttpsURLConnection httpsCon = (HttpsURLConnection) urlObj.openConnection();
			setCommonHeaders(httpsCon);
			logSSLInfo(httpsCon);
			BufferedReader br = new BufferedReader( new InputStreamReader( httpsCon.getInputStream() ) );
			respStatus = getReponseStatus( httpsCon );
			LOGGER.debug("Response Status - " + respStatus );
			String line = "";
			while( (line = br.readLine()) != null ) {
				webContent.append(line);
			}
			br.close();
			
		} catch( IOException e ) {
			LOGGER.error("Error while connecting making https connection", e);
			if( webContent == null || webContent.toString().isEmpty() ) {
				webContent.append( FileOperations.readResource( Constants.RENDERING_ERROR_HTML ) );
			} else {
				webContent.append( respStatus );
			}
		}
		return webContent;
	}
	
	private void logSSLInfo( HttpsURLConnection con ) {
		if (con != null) {
			try {
				LOGGER.info("Response Code : " + con.getResponseCode());
				LOGGER.info("Cipher Suite : " + con.getCipherSuite() + "\n");
				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					LOGGER.info("Cert Type : " + cert.getType());
					LOGGER.info("Cert Hash Code : " + cert.hashCode());
					LOGGER.info("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
					LOGGER.info("Cert Public Key Format : " + cert.getPublicKey().getFormat() + "\n");
				}
			} catch( SSLPeerUnverifiedException e ) {
				LOGGER.error("Error while reading SSL info", e);
			} catch( IOException e ) {
				LOGGER.error("Error while reading SSL info", e);
			}
		}
	}
	
	private void setCommonHeaders( URLConnection con ) {
		con.setRequestProperty("User-Agent", "Java Client");
		con.setRequestProperty("Accept", "text/html");
		con.setRequestProperty("Accept-Language", "en-US");
		con.setRequestProperty("Connection", "close");
	}
		
	
	private static String getReponseStatus( URLConnection con ) {
		String status = con!=null?con.getHeaderField(null):"Unknown error";
		LOGGER.debug("Response Status - " + status);
		return status;
	}

}
