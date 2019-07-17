package org.warn.utils.config.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.warn.utils.text.TextHelper;

public class TextHelperTest {
	
	@Test
	public void testgetWordCount1() {
		String s = " $";
		int count = TextHelper.getWordCount(s);
		assertTrue( count == 0 );
	}
	
	@Test
	public void testgetWordCount2() {
		String s = " The  \n\r";
		int count = TextHelper.getWordCount(s);
		assertTrue( count == 1 );
	}
	
	@Test
	public void testgetWordCount3() {
		String s = "The . $";
		int count = TextHelper.getWordCount(s);
		assertTrue( count == 1 );
	}

	@Test
	public void testgetWordCount4() {
		String s = "The quick brown fox jumps over the lazy dog";
		int count = TextHelper.getWordCount(s);
		assertTrue( count == 9 );
	}
	
	@Test
	public void testgetWordCount5() {
		String s = "I am a Java programmer ...";
		int count = TextHelper.getWordCount(s);
		assertTrue( count == 5 );
	}
	
	@Test
	public void testgetWordCount6() {
		String s = "Total Count n = 1000";
		int count = TextHelper.getWordCount(s);
		assertTrue( count == 4 );
	}
	
	@Test
	public void testIsEmpty1() {
		String s = null;
		assertTrue( TextHelper.isEmpty(s) );
	}
	
	@Test
	public void testIsEmpty2() {
		String s = "";
		assertTrue( TextHelper.isEmpty(s) );
	}
	
	@Test
	public void testIsEmpty3() {
		String s = " ";
		assertFalse( TextHelper.isEmpty(s) );
	}
	
	@Test
	public void testIsEmpty4() {
		String s = "abc";
		assertFalse( TextHelper.isEmpty(s) );
	}
}
