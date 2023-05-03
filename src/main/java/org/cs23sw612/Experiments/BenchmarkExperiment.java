package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import net.automatalib.words.Alphabet;
import org.cs23sw612.Experiments.Util.Benchmark.BenchmarkResult;
import org.cs23sw612.Experiments.Util.Benchmark.EQStatisticsOracle;
import org.cs23sw612.Experiments.Util.Benchmark.MQStatisticsOracle;
import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;
import org.cs23sw612.SUL.PerformanceMetricSUL;
import org.cs23sw612.Util.Stopwatch;

public class BenchmarkExperiment<I, O> implements IPLCExperiment<I, O> {

    private final SUL<I, O> sul;
    private final MealyLearnerFactory<I, O> learnerFactory;
    private final OracleFactory<I, O> oracleFactory;
    private final OracleConfig config;
    Alphabet<I> alphabet;
    private final int repetitions;
    private final int warmupRounds;

    public BenchmarkExperiment(MealyLearnerFactory<I, O> learnerFactory, OracleFactory<I, O> oracleFactory,
            OracleConfig config, SUL<I, O> sul, Alphabet<I> alphabet, int repetitions, int warmupRounds) {
        this.learnerFactory = learnerFactory;
        this.oracleFactory = oracleFactory;
        this.config = config;
        this.sul = sul;
        this.alphabet = alphabet;
        this.repetitions = repetitions;
        this.warmupRounds = warmupRounds;
    }

    public void run() {
        var results = new BenchmarkResult(learnerFactory.getName());

        for (int i = 0; i < warmupRounds; i++) {
            runExperiment();
        }
        for (int i = 0; i < repetitions; i++) {
            runExperiment(results);
        }

        System.out.println(results.getDetails());
    }

    private void runExperiment() {
        runExperiment(null);
    }

    private void runExperiment(BenchmarkResult result) {

        var sulOracle = new MQStatisticsOracle<>(new PerformanceMetricSUL<>(sul));
        var learningAlgorithm = learnerFactory.createLearner(alphabet, sulOracle);
        var equivalenceOracle = oracleFactory.createOracle(sul, config);
        var eqStatOracle = new EQStatisticsOracle<>(equivalenceOracle);
        var experiment = new PLCExperimentImpl<>(learningAlgorithm, eqStatOracle, alphabet, null, false);

        Stopwatch experimentTimer = new Stopwatch();
        experimentTimer.start();
        experiment.run();
        experimentTimer.stop();

        if (result != null)
            result.addRun(experimentTimer.getTotalDuration(), eqStatOracle.getCounter(), sulOracle.getMqCounter());
    }
}
