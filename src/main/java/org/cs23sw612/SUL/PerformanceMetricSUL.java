package org.cs23sw612.SUL;

import de.learnlib.api.SUL;
import org.cs23sw612.Util.Stopwatch;

import java.time.Duration;

public class PerformanceMetricSUL<I, O> implements SUL<I, O> {

    private final SUL<I, O> sul;
    private long stepCounter = 0;
    private long experimentCounter = 0;
    private long longestWordLength = 0;
    private long currentWordLength = 0;
    private final Stopwatch preTimer;
    private final Stopwatch postTimer;
    private final Stopwatch stepTimer;
    public PerformanceMetricSUL(SUL<I, O> sul) {
        this.sul = sul;
        preTimer = new Stopwatch();
        postTimer = new Stopwatch();
        stepTimer = new Stopwatch();
    }

    public Duration getStepTime() {
        return stepTimer.getTotalDuration();
    }

    public Duration getPostTime() {
        return postTimer.getTotalDuration();
    }

    public Duration getPreTime() { return preTimer.getTotalDuration(); }

    public long getExperimentCounter() {
        return experimentCounter;
    }

    public long getStepCounter() {
        return stepCounter;
    }

    public long getLongestWordLength() {
        return longestWordLength;
    }

    @Override
    public void pre() {
        currentWordLength = 0;
        experimentCounter++;
        preTimer.start();
        sul.pre();
        preTimer.stop();
    }

    @Override
    public void post() {
        if (currentWordLength > longestWordLength) {
            longestWordLength = currentWordLength;
        }
        postTimer.start();
        sul.post();
        postTimer.stop();
    }

    @Override
    public O step(I i) {
        currentWordLength++;
        stepCounter++;
        stepTimer.start();
        var rv = sul.step(i);
        stepTimer.stop();
        return rv;
    }
}
