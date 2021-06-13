package org.warn.utils.core;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class EnvTest {

	@Test
	public void testUserHomeDir() {
		assertNotNull( Env.getUserHomeDir() );
	}

}
