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

    private final SUL<Word<Boolean>, Word<Boolean>> sul;
    private final MealyLearnerFactory<Word<Boolean>, Word<Boolean>> learnerFactory;
    private final OracleFactory<Word<Boolean>, Word<Boolean>> oracleFactory;
    private final String outputFileName;
    private final OracleConfig config;
    Alphabet<Word<Boolean>> alphabet;
    private final int repetitions;
    private final int warmupRounds;
    private int roundsRun = 0;

    public BenchmarkExperiment(MealyLearnerFactory<Word<Boolean>, Word<Boolean>> learnerFactory,
            OracleFactory<Word<Boolean>, Word<Boolean>> oracleFactory, OracleConfig config,
            SUL<Word<Boolean>, Word<Boolean>> sul, String outputFileName, Alphabet<Word<Boolean>> alphabet,
            int repetitions, int warmupRounds) {
        this.learnerFactory = learnerFactory;
        this.oracleFactory = oracleFactory;
        this.config = config;
        this.sul = sul;
        this.outputFileName = outputFileName;
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
        var experiment = new PLCExperimentImpl(learningAlgorithm, eqStatOracle, alphabet, outputFileName, false, false);

        Stopwatch experimentTimer = new Stopwatch();
        experimentTimer.start();
        experiment.run();
        experimentTimer.stop();
        roundsRun++;

        if (result != null)
            result.addRun(experimentTimer.getTotalDuration(), eqStatOracle.getCounter(), sulOracle.getMqCounter());
    }
}
