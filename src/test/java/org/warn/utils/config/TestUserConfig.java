package org.warn.utils.config;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import junit.framework.TestCase;

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class TestUserConfig extends TestCase {
	
	private UserConfig uc;
	
	public void test1IntializeWithNullFileName() {
		try {
			uc = new UserConfig( null, null, null );
		} catch( Exception e ) {
			assertTrue( e instanceof IllegalArgumentException );
			assertEquals( e.getMessage(), "Please provide a config file name" );
		}
	}
	
	public void test2IntializeWithEmptyArray() {
		try {
			String [] arr = {};
			uc = new UserConfig( null, arr );
		} catch( Exception e ) {
			assertTrue( e instanceof IllegalArgumentException );
			assertEquals( e.getMessage(), "Please provide a config file name" );
		}
	}
	
	public void test3IntializeWithEmptyFileName() {
		try {
			uc = new UserConfig( null, "" );
		} catch( Exception e ) {
			assertTrue( e instanceof IllegalArgumentException );
			assertEquals( e.getMessage(), "Please provide a config file name" );
		}
	}
	
	public void test4GetNullProperty() {
		uc = new UserConfig( null, ".tempConf", ".config.json" );
		String prop = uc.getProperty("abc");
		assertNull( prop );
	}
	
}
