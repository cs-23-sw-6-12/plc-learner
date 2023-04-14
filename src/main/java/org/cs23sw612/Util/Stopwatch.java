package org.cs23sw612.Util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Stopwatch {
    ArrayList<TimeSpan> timeSpans;
    Instant lastBegin;

    public Stopwatch() {
        timeSpans = new ArrayList<>();
    }

    public void start() {
        if (lastBegin != null) {
            throw new RuntimeException("Stopwatch is already running");
        }
        lastBegin = Instant.now();
    }

    public void stop() {
        timeSpans.add(new TimeSpan(lastBegin, Instant.now()));
        if (lastBegin == null) {
            throw new RuntimeException("Stopwatch has not been started");
        }
        lastBegin = null;
    }

    public Duration getTotalDuration() {
        return timeSpans.stream().map(TimeSpan::getDuration).reduce(Duration.ofSeconds(0), Duration::plus);
    }
}
