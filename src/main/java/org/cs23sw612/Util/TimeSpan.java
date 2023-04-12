package org.cs23sw612.Util;

import java.time.Duration;
import java.time.Instant;

public class TimeSpan {
    public TimeSpan(Instant from, Instant to) {
        this.from = from;
        this.to = to;
    }

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public Instant from;
    public Instant to;
}
