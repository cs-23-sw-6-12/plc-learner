package org.cs23sw612.commands;

import de.learnlib.api.SUL;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.graphs.Graph;
import net.automatalib.serialization.dot.DOTSerializationProvider;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.IntegerWordInputAdapter;
import org.cs23sw612.Adapters.Output.IntegerWordOutputAdapter;
import org.cs23sw612.BAjER.BAjERClient;
import org.cs23sw612.HashCacheStorage;
import org.cs23sw612.OracleConfig;
import org.cs23sw612.SUL.SULClient;
import org.cs23sw612.SUL.PerformanceMetricSUL;
import org.cs23sw612.SUL.GenericCache;
import org.cs23sw612.Util.AlphabetUtil;
import org.cs23sw612.Util.LearnerFactoryRepository;
import org.cs23sw612.Util.OracleRepository;
import org.cs23sw612.Util.Stopwatch;
import picocli.CommandLine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "learn", mixinStandardHelpOptions = true, version = "0.1.0", description = "Learns a PLCs logic")
public class LearnCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Number of inputs")
    private int inputCount;

    @CommandLine.Parameters(index = "1", description = "Number of outputs")
    private int outputCount;

    @CommandLine.Parameters(index = "2", description = "BAjER server address")
    private String bajerServerAddress;

    @CommandLine.Option(names = {"--port",
            "-p"}, description = "port for the BAjER server", defaultValue = "1337", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private int bajerServerPort;

    @CommandLine.Option(names = {"--learner",
            "-l"}, description = "Learner to use (case insensitive)", defaultValue = "DHC", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String learnerName;

    @CommandLine.Option(names = {"--visualize", "-v"}, description = "Visualize the automaton when done")
    private boolean visualize;

    @CommandLine.Option(names = {"--out",
            "-o"}, description = "Where the learned automaton should be saved", defaultValue = "automaton.dot", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String outputFileName;

    @CommandLine.Option(names = {"--benchmark",
            "-b"}, description = "Measure performance metrics and print them when learning has finished")
    private boolean benchmark;

    @CommandLine.Option(names = {"--cache",
            "-c"}, description = "Cache experiment results from BAjER, improves performance when learner queries the same string multiple times", defaultValue = "true", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
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
    private final OracleRepository oracleRepository;

    public LearnCommand(LearnerFactoryRepository<Word<Integer>, Word<Integer>> learnerRepository,
            OracleRepository oracleRepository) {
        this.learnerRepository = learnerRepository;
        this.oracleRepository = oracleRepository;
    }

    @Override
    public Integer call() throws Exception {
        FileOutputStream outFile;
        try {
            outFile = new FileOutputStream(outputFileName);
        } catch (Exception ex) {
            System.err.println("Could not open output file for writing");
            System.err.println(ex.getMessage());
            return 1;
        }

        var bajerClient = new BAjERClient();
        try {
            bajerClient.Connect(bajerServerAddress, bajerServerPort);
        } catch (Exception ex) {
            System.err.println("Could not connect to BAjER server");
            System.err.println(ex.getMessage());
            return 1;
        }

        var alphabet = AlphabetUtil.createIntAlphabet(inputCount);

        SUL<Word<Integer>, Word<Integer>> bajerSul = new SULClient<>(bajerClient, new IntegerWordInputAdapter(),
                new IntegerWordOutputAdapter(), (byte) inputCount, (byte) outputCount);

        var bajerMetricsSul = new PerformanceMetricSUL<>(bajerSul);
        if (benchmark) {
            bajerSul = bajerMetricsSul;
        }

        SUL<Word<Integer>, Word<Integer>> finalSul = null;

        if (cacheSul) {
            finalSul = new GenericCache(new HashCacheStorage(new File("bob.csv")), bajerSul);
        } else {
            finalSul = bajerSul;
        }

        var oracle = oracleRepository.getOracleFactory(oracleName).createOracle(finalSul,
                new OracleConfig(maxSteps, restartProbability, depth));

        if (oracle == null) {
            System.err.format(
                    "Unknown oracle \"%s\", use the list-oracles command to see the list of available learners\n",
                    oracleName);
            return 1;
        }

        var learnerFactory = learnerRepository.getLearnerFactory(learnerName);

        if (learnerFactory == null) {
            System.err.format(
                    "Unknown learner \"%s\", use the list-learners command to see the list of available learners\n",
                    learnerName);
            return 1;
        }

        var learner = learnerFactory.createLearner(alphabet, new SULOracle<>(finalSul));

        var experiment = new Experiment.MealyExperiment<>(learner, oracle, alphabet);

        Stopwatch experimentTimer = new Stopwatch();
        experimentTimer.start();
        experiment.run();
        experimentTimer.stop();

        var result = experiment.getFinalHypothesis();

        DOTSerializationProvider.getInstance().writeModel(outFile,
                (Graph) result.transitionGraphView(alphabet).asNormalGraph());

        if (benchmark) {
            System.out.println("Benchmark results:");
            System.out.format("total experiment time %.4f seconds\n",
                    experimentTimer.getTotalDuration().toMillis() / 1000.0);
            System.out.format("total step time: %.4f seconds\n", bajerMetricsSul.getStepTime().toMillis() / 1000.0);
            System.out.format("total pre time: %.4f seconds\n", bajerMetricsSul.getPreTime().toMillis() / 1000.0);
            System.out.format("total post time: %.4f seconds\n", bajerMetricsSul.getPostTime().toMillis() / 1000.0);
            System.out.format("experiment count: %s\n", bajerMetricsSul.getExperimentCounter());
            System.out.format("step count: %s\n", bajerMetricsSul.getStepCounter());
            System.out.format("step average time: %.5f ms\n",
                    ((double) bajerMetricsSul.getStepTime().toMillis()) / bajerMetricsSul.getStepCounter());
            System.out.format("pre average time: %.5f ms\n",
                    ((double) bajerMetricsSul.getPreTime().toMillis()) / bajerMetricsSul.getExperimentCounter());
            System.out.format("longest word during experiment: %s\n", bajerMetricsSul.getLongestWordLength());
        }

        if (visualize) {
            Visualization.visualize(result, alphabet);
        }

        return 0;
    }
}