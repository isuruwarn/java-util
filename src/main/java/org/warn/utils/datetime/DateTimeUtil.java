package org.warn.utils.datetime;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {

	public static final String FULL_TS_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_ONLY_TS_FORMAT = "yyyy-MM-dd";
	
	public static final SimpleDateFormat fullTimestampSDF = new SimpleDateFormat( FULL_TS_FORMAT );
	public static final SimpleDateFormat dateSDF = new SimpleDateFormat( DATE_ONLY_TS_FORMAT );
	
	public static String formatDuration( TimeUnit timeUnit, long duration ) {
		
		long seconds;
		long minutes;
		long hours;
		
		if( timeUnit == TimeUnit.MILLISECONDS ) {
			seconds = TimeUnit.MILLISECONDS.toSeconds( duration ) % 60;
			minutes = TimeUnit.MILLISECONDS.toMinutes( duration ) % 60;
			hours = TimeUnit.MILLISECONDS.toHours( duration );
		
		} else if( timeUnit == TimeUnit.SECONDS ) {
			seconds = TimeUnit.SECONDS.toSeconds( duration ) % 60;
			minutes = TimeUnit.SECONDS.toMinutes( duration ) % 60;
			hours = TimeUnit.SECONDS.toHours( duration );

		} else if( timeUnit == TimeUnit.MINUTES ) {
			seconds = TimeUnit.MINUTES.toSeconds( duration ) % 60;
			minutes = TimeUnit.MINUTES.toMinutes( duration ) % 60;
			hours = TimeUnit.MINUTES.toHours( duration );

		} else if( timeUnit == TimeUnit.HOURS ) {
			seconds = TimeUnit.HOURS.toSeconds( duration ) % 60;
			minutes = TimeUnit.HOURS.toMinutes( duration ) % 60;
			hours = TimeUnit.HOURS.toHours( duration );
			
		} else {
			throw new UnsupportedOperationException("TimeUnit " + timeUnit + " is not supported");
		}
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds );
	}
}
