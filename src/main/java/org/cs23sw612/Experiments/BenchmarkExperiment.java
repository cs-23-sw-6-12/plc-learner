package org.cs23sw612.Experiments;

import de.learnlib.api.SUL;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Experiments.Util.Benchmark.BenchmarkResult;
import org.cs23sw612.Experiments.Util.Benchmark.EQStatisticsOracle;
import org.cs23sw612.Experiments.Util.Benchmark.MQStatisticsOracle;
import org.cs23sw612.Interfaces.MealyLearnerFactory;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;
import org.cs23sw612.SUL.PerformanceMetricSUL;
import org.cs23sw612.Util.Stopwatch;

public class BenchmarkExperiment implements IPLCExperiment {

    private final SUL<Word<Integer>, Word<Integer>> sul;
    private final MealyLearnerFactory<Word<Integer>, Word<Integer>> learnerFactory;
    private final OracleFactory<Word<Integer>, Word<Integer>> oracleFactory;
    private final OracleConfig config;
    Alphabet<Word<Integer>> alphabet;
    private final int repetitions;
    private final int warmupRounds;

    public BenchmarkExperiment(MealyLearnerFactory<Word<Integer>, Word<Integer>> learnerFactory,
            OracleFactory<Word<Integer>, Word<Integer>> oracleFactory, OracleConfig config,
            SUL<Word<Integer>, Word<Integer>> sul, Alphabet<Word<Integer>> alphabet, int repetitions,
            int warmupRounds) {
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
        var experiment = new PLCExperimentImpl(learningAlgorithm, eqStatOracle, alphabet, null, false);

        Stopwatch experimentTimer = new Stopwatch();
        experimentTimer.start();
        experiment.run();
        experimentTimer.stop();

        if (result != null)
            result.addRun(experimentTimer.getTotalDuration(), eqStatOracle.getCounter(), sulOracle.getMqCounter());
    }
}
