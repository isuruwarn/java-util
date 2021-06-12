package org.warn.utils.perf;

import org.junit.Test;

import java.time.temporal.ChronoUnit;

public class PerformanceLoggerTest {

    @Test
    public void testWithDefaultConstructor() throws InterruptedException {
        PerformanceLogger perf = new PerformanceLogger();
        perf.start();
        Thread.sleep(2000);
        perf.printStatistics();
    }

    @Test
    public void testWithMilliSeconds() throws InterruptedException {
        PerformanceLogger perf = new PerformanceLogger( ChronoUnit.MILLIS );
        perf.start();
        Thread.sleep(1000);
        perf.printStatistics();
    }

    @Test
    public void testWithMinutes() throws InterruptedException {
        PerformanceLogger perf = new PerformanceLogger( ChronoUnit.MINUTES );
        perf.start();
        Thread.sleep(2000);
        perf.printStatistics();
    }
}
