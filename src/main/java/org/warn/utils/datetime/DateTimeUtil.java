package org.warn.utils.datetime;

import java.text.SimpleDateFormat;

public class DateTimeUtil {

	public static final String FULL_TS_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_ONLY_TS_FORMAT = "yyyy-MM-dd";
	
	public static final SimpleDateFormat fullTimestampSDF = new SimpleDateFormat( FULL_TS_FORMAT );
}
