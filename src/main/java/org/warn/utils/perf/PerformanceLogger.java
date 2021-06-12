package org.warn.utils.perf;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.extern.slf4j.Slf4j;

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
			log.info( "Duration ({}): {}", chronoUnit.toString(), duration );
			
			endMemory = Runtime.getRuntime().freeMemory();
			log.info( "Free Memory at Start (bytes): " + startMemory );
			log.info( "Free Memory at End (bytes): " + endMemory );
			log.info( "---------------------------------------" );

		} else {
			log.warn("Performance logger has not been started..");
		}
	}

}
