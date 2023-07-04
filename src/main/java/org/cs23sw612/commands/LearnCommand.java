package org.cs23sw612.commands;

import de.learnlib.api.SUL;
import de.learnlib.driver.util.MealySimulatorSUL;
import net.automatalib.serialization.dot.DOTParsers;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.BitWordInputAdapter;
import org.cs23sw612.Adapters.Output.BitWordOutputAdapter;
import org.cs23sw612.BAjER.BAjERClient;
import org.cs23sw612.Experiments.ExperimentBuilder;
import org.cs23sw612.SUL.SULClient;
import org.cs23sw612.Util.Bit;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleFactoryRepository;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "learn", mixinStandardHelpOptions = true, version = "0.1.0", description = "Learns a PLCs logic")
public class LearnCommand implements Callable<Integer> {
    @SuppressWarnings("unused")
    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    private SULSource source;
    @SuppressWarnings("unused")
    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    private BenchmarkOptions benchmarkOptions;

    private static class SULSource {
        @SuppressWarnings("unused")
        @CommandLine.ArgGroup(exclusive = false)
        BajerSulConnectionOptions connectionOptions;

        @SuppressWarnings("unused")
        @CommandLine.Option(names = "--dot-path", description = "Load a SUL from a DOT file to test the learning setup.")
        String path;
    }

    private static class BajerSulConnectionOptions {
        @SuppressWarnings("unused")
        @CommandLine.Parameters(index = "0", description = "Number of inputs")
        private int inputCount;

        @SuppressWarnings("unused")
        @CommandLine.Parameters(index = "1", description = "Number of outputs")
        private int outputCount;

        @SuppressWarnings("unused")
        @CommandLine.Parameters(index = "2", description = "BAjER server address")
        private String bajerServerAddress;

        @SuppressWarnings("unused")
        @CommandLine.Option(names = {"--port",
                "-p"}, description = "port for the BAjER server", defaultValue = "1337", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
        private int bajerServerPort;
    }

    static class BenchmarkOptions {
        @SuppressWarnings("unused")
        @CommandLine.Option(names = {"--benchmark",
                "-b"}, description = "Measure performance metrics and print them when learning has finished.")
        private boolean benchmark;
        @SuppressWarnings("unused")
        @CommandLine.Option(names = {"--warmup-rounds",
                "-w"}, defaultValue = "5", description = "How many learning experiments should be performed before measuring.")
        private int warmup_rounds;

        @SuppressWarnings("unused")
        @CommandLine.Option(names = {
                "--run-amount"}, defaultValue = "10", description = "How many learning experiments should be performed while measuring.")
        private int run_amount;

    }

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--learner",
            "-l"}, description = "Learner to use (case insensitive)", defaultValue = "DHC", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String learnerName;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--visualize-machine", "-vm"}, description = "Visualize the automaton when done")
    private boolean visualizeMachine;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--visualize-ladder", "-vl"}, description = "Visualize the ladder program when done")
    private boolean visualizeLadder;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--out",
            "-o"}, description = "Where the learned automaton should be saved", defaultValue = "automaton.dot", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String outputFileName;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--cache",
            "-c"}, description = "Cache file location", defaultValue = "SULCache.csv", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String cacheFilePath;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--no-cache",
            "-nc"}, description = "disable cache", defaultValue = "false", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private boolean noCache;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--oracle",
            "-r"}, description = "Chooses the oracle to be used", defaultValue = "random-walk")
    private String oracleName;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--max-steps",
            "-n"}, description = "Sets the max steps when using the random walk oracle", defaultValue = "10000")
    private Integer maxSteps;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {
            "--restart-probability"}, description = "Sets the restart probability when using the random walk oracle", defaultValue = "0.05")
    private Double restartProbability;

    @SuppressWarnings("unused")
    @CommandLine.Option(names = {"--depth",
            "-d"}, description = "Sets the depth when using the complete exploration oracle", defaultValue = "3")
    private Integer depth;

    private final LearnerFactoryRepository<Word<Bit>, Word<Bit>> learnerRepository;
    private final OracleFactoryRepository<Word<Bit>, Word<Bit>> oracleRepository;

    public LearnCommand(LearnerFactoryRepository<Word<Bit>, Word<Bit>> learnerRepository,
            OracleFactoryRepository<Word<Bit>, Word<Bit>> oracleRepository) {
        this.learnerRepository = learnerRepository;
        this.oracleRepository = oracleRepository;
    }

    @Override
    public Integer call() throws Exception {
        var experimentBuilder = getExperimentBuilder(source).withOracle(oracleRepository, oracleName)
                .withConfiguration(maxSteps, restartProbability, depth).withLearner(learnerRepository, learnerName)
                .outputDOT(outputFileName);

        if (!noCache)
            experimentBuilder = experimentBuilder.withCache(cacheFilePath);

        if (visualizeMachine)
            experimentBuilder = experimentBuilder.withMachineVisualization();

        if (visualizeLadder)
            experimentBuilder = experimentBuilder.withLadderVisualization();

        if (benchmarkOptions != null) {
            experimentBuilder = experimentBuilder.intoBenchmark().withRunAmount(benchmarkOptions.run_amount)
                    .withWarmupRounds(benchmarkOptions.warmup_rounds);
        }

        var experiment = experimentBuilder.build();

        experiment.run();

        return 0;
    }

    private ExperimentBuilder getExperimentBuilder(LearnCommand.SULSource source) throws IOException {
        Alphabet<Word<Bit>> alphabet;
        SUL<Word<Bit>, Word<Bit>> sul;

        if (source.path != null) {
            var file = new FileInputStream(source.path);
            var parsed = DOTParsers.mealy(Bit::parseBit).readModel(file);
            var model = parsed.model;

            alphabet = parsed.alphabet;
            sul = new MealySimulatorSUL<>(model);

        } else {
            var bajerClient = new BAjERClient();

            bajerClient.Connect(source.connectionOptions.bajerServerAddress, source.connectionOptions.bajerServerPort);

            // alphabet = AlphabetUtil.createAlphabet(source.connectionOptions.inputCount);
            alphabet = Bit.createAlphabet(source.connectionOptions.inputCount);
            sul = new SULClient(bajerClient, new BitWordInputAdapter(), new BitWordOutputAdapter(),
                    (byte) source.connectionOptions.inputCount, (byte) source.connectionOptions.outputCount);
        }
        return new ExperimentBuilder(sul, alphabet);
    }
}
