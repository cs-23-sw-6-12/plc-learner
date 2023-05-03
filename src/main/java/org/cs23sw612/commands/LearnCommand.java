package org.cs23sw612.commands;

import de.learnlib.api.SUL;
import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.IntegerWordInputAdapter;
import org.cs23sw612.Adapters.Output.IntegerWordOutputAdapter;
import org.cs23sw612.BAjER.BAjERClient;
import org.cs23sw612.Experiments.ExperimentBuilder;
import org.cs23sw612.SUL.SULClient;
import org.cs23sw612.Util.AlphabetUtil;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "learn", mixinStandardHelpOptions = true, version = "0.1.0", description = "Learns a PLCs logic")
public class LearnCommand implements Callable<Integer> {

    private final Logger logger;
    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    private SULSource source;
    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    private BenchmarkOptions benchmarkOptions;

    private static class SULSource {
        @CommandLine.ArgGroup(exclusive = false)
        BajerSulConnectionOptions connectionOptions;

        @CommandLine.Option(names = "--dot-path", description = "Load a SUL from a DOT file to test the learning setup.")
        String path;
    }

    private static class BajerSulConnectionOptions {
        @CommandLine.Parameters(index = "0", description = "Number of inputs")
        int inputCount;

        @CommandLine.Parameters(index = "1", description = "Number of outputs")
        int outputCount;

        @CommandLine.Parameters(index = "2", description = "BAjER server address")
        String bajerServerAddress;
        @CommandLine.Option(names = {"--port",
                "-p"}, description = "port for the BAjER server", defaultValue = "1337", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
        int bajerServerPort;
    }

    static class BenchmarkOptions {
        @CommandLine.Option(names = {"--benchmark",
                "-b"}, description = "Measure performance metrics and print them when learning has finished.")
        private boolean benchmark;
        @CommandLine.Option(names = {"--warmup-rounds",
                "-w"}, defaultValue = "5", description = "How many learning experiments should be performed before measuring.")
        private int warmup_rounds;

        @CommandLine.Option(names = {
                "--run-amount"}, defaultValue = "10", description = "How many learning experiments should be performed while measuring.")
        private int run_amount;

    }

    @CommandLine.Option(names = {"--learner",
            "-l"}, description = "Learner to use (case insensitive)", defaultValue = "DHC", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String learnerName;

    @CommandLine.Option(names = {"--visualize", "-v"}, description = "Visualize the automaton when done")
    private boolean visualize;

    @CommandLine.Option(names = {"--out",
            "-o"}, description = "Where the learned automaton should be saved", defaultValue = "automaton.dot", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String outputFileName;

    @CommandLine.Option(names = {
            "--no-cache"}, description = "Cache experiment results from BAjER, improves performance when learner queries the same string multiple times", defaultValue = "true", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private boolean cacheSul;

    @CommandLine.Option(names = {"--oracle",
            "-r"}, description = "Chooses the oracle to be used", defaultValue = "random-walk")
    private String oracleName;

    @CommandLine.Option(names = {"--max-steps",
            "-n"}, description = "Sets the max steps when using the random walk oracle", defaultValue = "10000")
    private Integer maxSteps;

    @CommandLine.Option(names = {
            "--restart-probability"}, description = "Sets the restart probability when using the random walk oracle", defaultValue = "0.05")
    private Double restartProbability;
    @CommandLine.Option(names = {"--depth",
            "-d"}, description = "Sets the depth when using the complete exploration oracle", defaultValue = "3")
    private Integer depth;

    private final LearnerFactoryRepository<Word<Integer>, Word<Integer>> learnerRepository;
    private final OracleFactoryRepository<Word<Integer>, Word<Integer>> oracleRepository;

    public LearnCommand(LearnerFactoryRepository<Word<Integer>, Word<Integer>> learnerRepository,
            OracleFactoryRepository<Word<Integer>, Word<Integer>> oracleRepository) {
        this.learnerRepository = learnerRepository;
        this.oracleRepository = oracleRepository;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Integer call() throws Exception {

        var experimentBuilder = getExperimentBuilder(source).withOracle(oracleRepository, oracleName)
                .withConfiguration(maxSteps, restartProbability, depth).withLearner(learnerRepository, learnerName)
                .outputDOT(outputFileName);

        if (cacheSul) {
            experimentBuilder = experimentBuilder.withCache();
        }

        if (visualize) {
            experimentBuilder = experimentBuilder.withVisualization();
        }

        if (benchmarkOptions != null) {
            experimentBuilder = experimentBuilder.intoBenchmark().withRunAmount(benchmarkOptions.run_amount)
                    .withWarmupRounds(benchmarkOptions.warmup_rounds);
        }

        var experiment = experimentBuilder.build();

        experiment.run();

        return 0;
    }

    private ExperimentBuilder<Word<Integer>, Word<Integer>> getExperimentBuilder(LearnCommand.SULSource source)
            throws IOException {
        Alphabet<Word<Integer>> alphabet;
        SUL<Word<Integer>, Word<Integer>> sul;

        if (source.path != null) {
            var file = new FileInputStream(source.path);
            var parsed = DOTParsers.mealy(AlphabetUtil::ParseInt).readModel(file);
            var model = parsed.model;

            alphabet = parsed.alphabet;
            sul = new MealySimulatorSUL<>(model);

        } else {
            var bajerClient = new BAjERClient();

            bajerClient.Connect(source.connectionOptions.bajerServerAddress, source.connectionOptions.bajerServerPort);

            alphabet = AlphabetUtil.createIntAlphabet(source.connectionOptions.inputCount);

            sul = new SULClient<>(bajerClient, new IntegerWordInputAdapter(), new IntegerWordOutputAdapter(),
                    (byte) source.connectionOptions.inputCount, (byte) source.connectionOptions.outputCount);
        }
        return new ExperimentBuilder<>(sul, alphabet);
    }

}