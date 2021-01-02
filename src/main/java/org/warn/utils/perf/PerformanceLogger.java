package org.warn.utils.perf;

import java.time.Clock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceLogger {
	
	private static final Clock clock = Clock.systemUTC();
	
	long startTime;
	long endTime;
	long duration;
	long startMemory;
	long endMemory;
	
	public void start() {
		
		if( startTime == 0 ) {
			startTime = clock.millis();
			startMemory = Runtime.getRuntime().freeMemory();
		} else {
			log.warn("Performance logger already started..");
		}
	}
	
	public void printStatistics() {
		
		if( startTime > 0 ) {
			endTime = clock.millis();
			duration = (endTime - startTime);
			log.info( "Duration (ms) \t" + duration );
			
			endMemory = Runtime.getRuntime().freeMemory();
			log.info( "Free Memory at Start (bytes) \t" + startMemory );
			log.info( "Free Memory at End (bytes) \t" + endMemory );
			
		} else {
			log.warn("Performance logger has not been started..");
		}
	}

}
