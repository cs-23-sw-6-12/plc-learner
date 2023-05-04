package org.cs23sw612.Experiments.Util.Benchmark;

import de.learnlib.filter.statistic.Counter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BenchmarkResult {

    private final String learnerName;

    public BenchmarkResult(String learnerName) {

        this.learnerName = learnerName;
    }

    private final List<Run> runs = new ArrayList<>();

    public Duration getAverageDuration() {
        return runs.stream().map(x -> x.duration).reduce(Duration.ofSeconds(0), Duration::plus).dividedBy(runs.size());
    }

    public long getAverageMembershipQueries() {
        return runs.stream().map(x -> x.membershipQueries.getCount()).reduce(0L, Long::sum) / runs.size();
    }

    public long getAverageEquivalenceQueries() {
        return runs.stream().map(x -> x.equivalenceQueries.getCount()).reduce(0L, Long::sum) / runs.size();
    }

    public void addRun(Duration duration, Counter equivalenceQueries, Counter membershipQueries) {
        runs.add(new Run(duration, equivalenceQueries, membershipQueries));
    }

    public String getSummary() {
        return "Summary:\n" + "Learning Algorithm             : " + learnerName + "\n"
                + "Average Time               [ms]: " + String.format("%6s\n", getAverageDuration().toMillis())
                + "Average Membership  Queries [#]: " + String.format("%6s\n", getAverageMembershipQueries())
                + "Average Equivalence Queries [#]: " + String.format("%6s\n", getAverageEquivalenceQueries());
    }

    public String getDetails() {
        StringBuilder fmt = new StringBuilder("%24s");
        for (var ignored : runs) {
            fmt.append("%-11s");
        }
        fmt.append("\n");

        String h_fmt = "%-25s" + "%-11s".repeat(runs.size()) + "\n";

        var h_row = new ArrayList<>(IntStream.rangeClosed(0, runs.size()).mapToObj(x -> "Run " + x + ":").toList());
        h_row.add(0, "Benchmark Results:");

        var row1 = new ArrayList<>(runs.stream().map(x -> Long.toString(x.duration.toMillis())).toList());
        row1.add(0, "Time" + " ".repeat(15) + "[ms]: ");

        var row2 = new ArrayList<>(runs.stream().map(x -> Long.toString(x.membershipQueries.getCount())).toList());
        row2.add(0, "Membership Queries  [#]: ");

        var row3 = new ArrayList<>(runs.stream().map(x -> Long.toString(x.equivalenceQueries.getCount())).toList());
        row3.add(0, "Equivalence Queries [#]: ");
        return String.format(h_fmt, h_row.toArray()) + String.format(fmt.toString(), row1.toArray())
                + String.format(fmt.toString(), row2.toArray()) + String.format(fmt.toString(), row3.toArray())
                + getSummary();
    }

    @Override
    public String toString() {
        return getSummary();
    }

    private record Run(Duration duration, Counter equivalenceQueries, Counter membershipQueries) {
        @Override
        public String toString() {
            return "Duration [ms]: " + duration.toMillis() + "\n" + equivalenceQueries.getDetails() + "\n"
                    + membershipQueries.getDetails() + "\n";
        }
    }
}
