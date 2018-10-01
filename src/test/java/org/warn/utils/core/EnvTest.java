package org.warn.utils.core;

import junit.framework.TestCase;

public class EnvTest extends TestCase {
	
	public void testUserHomeDir() {
		assertNotNull( Env.getUserHomeDir() );
	}

}
