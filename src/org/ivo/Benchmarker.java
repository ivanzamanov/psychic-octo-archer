package org.ivo;

import java.util.concurrent.TimeUnit;

public class Benchmarker {

    static final int warmUpTime = 1;
    static final int benchmarkTime = 3;
    static final int stepTime = 1;
    static final TimeUnit timeUnit = TimeUnit.SECONDS;
    
    public static double benchmark(Runnable test) {
        long startTime = System.nanoTime();
        long endTime = startTime + timeUnit.toNanos(warmUpTime);
        long curTime = startTime;
        long step = 1;
        System.out.print("Warmup started... ");
        do {
            long curStart = curTime;
            for (long i = 0; i<step; ++i)
                test.run();
            curTime = System.nanoTime();
            if (curTime - curStart < timeUnit.toNanos(stepTime) / 2) {
                step = step * timeUnit.toNanos(stepTime) / (curTime - curStart);
            }
        } while (curTime < endTime);
        System.out.format("Warm-up complete, %d iterations per step", step);
        
        startTime = System.nanoTime();
        endTime = startTime + timeUnit.toNanos(benchmarkTime);
        long count = 0;
        do {
            for (long i = 0; i<step; ++i)
                test.run();
            count += step;
            curTime = System.nanoTime();
        } while (curTime < endTime);
        double time = (curTime - startTime) / count;
        System.out.format("\rTime per iteration: %.0fns\t", time);
        System.out.format("Iterations per second: %.0f\n", TimeUnit.SECONDS.toNanos(1) / time);
        return time;
    } 
    
}
