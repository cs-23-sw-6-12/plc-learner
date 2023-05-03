package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import net.automatalib.words.Alphabet;

public class BenchmarkExperimentBuilder<I, O> extends ExperimentBuilder<I, O> {
    private int runs;
    private int warmup_rounds;

    public BenchmarkExperimentBuilder(SUL<I, O> sul, Alphabet<I> alphabet) {
        super(sul, alphabet);
    }

    public BenchmarkExperimentBuilder(ExperimentBuilder<I, O> experimentBuilder) {
        super(experimentBuilder.sul, experimentBuilder.alphabet);
        this.learnerFactory = experimentBuilder.learnerFactory;
        this.oracleFactory = experimentBuilder.oracleFactory;
        this.EqOracleConfig = experimentBuilder.EqOracleConfig;
        this.dotOutputLocation = experimentBuilder.dotOutputLocation;
        this.visualize = experimentBuilder.visualize;
    }

    public BenchmarkExperimentBuilder<I, O> withRunAmount(int runs) {
        this.runs = runs;
        return this;
    }

    public BenchmarkExperimentBuilder<I, O> withWarmupRounds(int runs) {
        warmup_rounds = runs;
        return this;
    }

    @Override
    public BenchmarkExperiment<I, O> build() {
        return new BenchmarkExperiment<>(this.learnerFactory, this.oracleFactory, this.EqOracleConfig, this.sul,
                this.alphabet, runs, warmup_rounds);
    }
}
