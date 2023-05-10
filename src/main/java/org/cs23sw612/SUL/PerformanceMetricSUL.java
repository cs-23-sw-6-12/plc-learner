package org.cs23sw612.SUL;

import de.learnlib.api.SUL;
import de.learnlib.filter.statistic.Counter;
import org.cs23sw612.Util.Stopwatch;

import java.time.Duration;

public class PerformanceMetricSUL<I, O> implements SUL<I, O> {

    private final SUL<I, O> sul;
    private Counter stepCounter = new Counter("SUL Steps", "#");
    private Counter experimentCounter = new Counter("Experiments", "#");
    private Counter longestWordLength = new Counter("Longest word", "# of symbols");
    private Counter currentWordLength = new Counter("Longest word", "# of symbols");
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

    public Duration getPreTime() {
        return preTimer.getTotalDuration();
    }

    public Counter getExperimentCounter() {
        return experimentCounter;
    }

    public Counter getStepCounter() {
        return stepCounter;
    }

    public Counter getLongestWordLength() {
        return longestWordLength;
    }

    @Override
    public void pre() {
        currentWordLength = new Counter("Longest word", "# of symbols");
        experimentCounter.increment();
        System.out.println("Starting experiment " + experimentCounter.getCount());
        preTimer.start();
        sul.pre();
        preTimer.stop();
    }

    @Override
    public void post() {
        if (currentWordLength.getCount() > longestWordLength.getCount()) {
            longestWordLength = currentWordLength;
        }
        postTimer.start();
        sul.post();
        postTimer.stop();
    }

    @Override
    public O step(I i) {
        currentWordLength.increment();
        stepCounter.increment();
        stepTimer.start();
        var rv = sul.step(i);
        stepTimer.stop();
        return rv;
    }
}
