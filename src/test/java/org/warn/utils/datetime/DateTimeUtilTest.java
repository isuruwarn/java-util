package org.warn.utils.datetime;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class DateTimeUtilTest {
	
	@Test
	public void testFormatDurationInMilliseconds1() {
		long duration = 150_000;
		String expected = "00:02:30";
		assertEquals( expected, DateTimeUtil.formatDuration( TimeUnit.MILLISECONDS, duration) );
	}
	
	@Test
	public void testFormatDurationInMilliseconds2() {
		long duration = 4_000_000;
		String expected = "01:06:40";
		assertEquals( expected, DateTimeUtil.formatDuration( TimeUnit.MILLISECONDS, duration) );
	}
	
	@Test
	public void testFormatDurationInSeconds1() {
		long duration = 150;
		String expected = "00:02:30";
		assertEquals( expected, DateTimeUtil.formatDuration( TimeUnit.SECONDS, duration) );
	}
	
	@Test
	public void testFormatDurationInSeconds2() {
		long duration = 4_000;
		String expected = "01:06:40";
		assertEquals( expected, DateTimeUtil.formatDuration( TimeUnit.SECONDS, duration) );
	}
	
	@Test
	public void testFormatDurationInHours() {
		long duration = 150;
		String expected = "150:00:00";
		assertEquals( expected, DateTimeUtil.formatDuration( TimeUnit.HOURS, duration) );
	}

}
