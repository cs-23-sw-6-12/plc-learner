package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

public class BenchmarkExperimentBuilder extends ExperimentBuilder {
    private int runs;
    private int warmup_rounds;

    public BenchmarkExperimentBuilder(SUL<Word<Integer>, Word<Integer>> sul, Alphabet<Word<Integer>> alphabet) {
        super(sul, alphabet);
    }

    public BenchmarkExperimentBuilder(ExperimentBuilder experimentBuilder) {
        super(experimentBuilder.sul, experimentBuilder.alphabet);
        this.learnerFactory = experimentBuilder.learnerFactory;
        this.oracleFactory = experimentBuilder.oracleFactory;
        this.EqOracleConfig = experimentBuilder.EqOracleConfig;
        this.dotOutputLocation = experimentBuilder.dotOutputLocation;
        this.visualize = experimentBuilder.visualize;
    }

    public BenchmarkExperimentBuilder withRunAmount(int runs) {
        this.runs = runs;
        return this;
    }

    public BenchmarkExperimentBuilder withWarmupRounds(int runs) {
        warmup_rounds = runs;
        return this;
    }

    @Override
    public BenchmarkExperiment build() {
        return new BenchmarkExperiment(this.learnerFactory, this.oracleFactory, this.EqOracleConfig, this.sul,
                this.alphabet, runs, warmup_rounds);
    }
}
