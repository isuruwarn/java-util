package org.warn.utils.http;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCustomHttpClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCustomHttpClient.class);
	
	@Test
	public void testGetWithMalformedURL() {
		CustomHttpClient client = new CustomHttpClient();
		StringBuilder webContent = client.get("abc");
		Assert.assertTrue( webContent.toString().contains("Site cannot be reached") );
	}
	
	@Test //(expected = UnknownHostException.class )
	public void testGetOnUnknownHost() {
		CustomHttpClient client = new CustomHttpClient();
		StringBuilder webContent = client.get("https://www.aaaaaaaaaaaaa");
		Assert.assertTrue( webContent.toString().contains("Site cannot be reached") );
	}
	
	@Test
	public void testGet404() {
		CustomHttpClient client = new CustomHttpClient();
		StringBuilder webContent = client.get("https://postman-echo.com/invalid");
		Assert.assertTrue( webContent.toString().contains("Page not found") );
	}
	
	@Test
	public void testGet() {
		CustomHttpClient client = new CustomHttpClient();
		StringBuilder webContent = client.get("https://postman-echo.com/get");
		LOGGER.debug( "GET Response: " + webContent.toString() );
		Assert.assertTrue( webContent.toString().length() > 0 );
	}
	
	@Test
	public void testPostAndGet() {
		CustomHttpClient client = new CustomHttpClient();
		StringBuilder webContent = client.postAndGet( "https://postman-echo.com/post", new StringBuilder("&param=test") );
		LOGGER.debug( "POST Response: " + webContent.toString() );
		Assert.assertTrue( webContent.toString().length() > 0 );
	}
	
	@Test
	public void testGetHttpsWithSSLInfo() {
		CustomHttpClient client = new CustomHttpClient();
		StringBuilder webContent = client.getHttpsWithSSLInfo("https://postman-echo.com/get");
		LOGGER.debug( "GET HTTPS Response: " + webContent.toString() );
		Assert.assertTrue( webContent.toString().length() > 0 );
	}

}
