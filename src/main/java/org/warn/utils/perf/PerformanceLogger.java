package org.warn.utils.perf;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.warn.utils.datetime.DateTimeUtil;
import org.warn.utils.file.FileHelper;

@Slf4j
public class PerformanceLogger {
	
	private static final Clock clock = Clock.systemUTC();
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private long duration;
	private long startMemory;
	private long endMemory;
	private ChronoUnit chronoUnit;

	public PerformanceLogger() {
		this( ChronoUnit.SECONDS );
	}

	public PerformanceLogger( ChronoUnit chronoUnit ) {
		this.chronoUnit = chronoUnit;
	}
	
	public void start() {
		if( startTime == null ) {
			startTime = LocalDateTime.now();
			startMemory = Runtime.getRuntime().freeMemory();
		} else {
			log.warn("Performance logger already started..");
		}
	}
	
	public void printStatistics() {
		if( startTime != null ) {
			log.info( "---------------------------------------" );

			endTime = LocalDateTime.now();
			duration = chronoUnit.between( startTime, endTime );
			log.info( "Duration: {}", DateTimeUtil.formatDuration( TimeUnit.of(chronoUnit), duration ) );
			
			endMemory = Runtime.getRuntime().freeMemory();
			log.info( "Free Memory at Start: {}", FileHelper.printFileSizeUserFriendly( startMemory ) );
			log.info( "Free Memory at End: {}", FileHelper.printFileSizeUserFriendly( endMemory ) );
			log.info( "---------------------------------------" );

		} else {
			log.warn("Performance logger has not been started..");
		}
	}

	/**
	 * Duration between start time and the last call of the {@link #printStatistics} method.
	 *
	 * @return Last calculated duration
	 */
	public long getLastCalculatedDuration() {
		return duration;
	}

	public long getCurrentDuration() {
		return chronoUnit.between( startTime, LocalDateTime.now() );
	}

}
