package org.cs23sw612.commands;

import de.learnlib.api.SUL;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.graphs.Graph;
import net.automatalib.serialization.dot.DOTSerializationProvider;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.IntegerWordInputAdapter;
import org.cs23sw612.Adapters.Output.IntegerWordOutputAdapter;
import org.cs23sw612.BAjER.BAjERClient;
import org.cs23sw612.SUL.SULClient;
import org.cs23sw612.SUL.PerformanceMetricSUL;
import org.cs23sw612.Util.AlphabetUtil;
import org.cs23sw612.Util.LearnerFactoryRepository;
import picocli.CommandLine;
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

    @CommandLine.Option(names = {"--benchmark", "-b"}, description = "Measure performance metrics and print them when learning has finished")
    private boolean benchmark;

    @CommandLine.Option(names = {"--cache", "-c"}, description = "Cache experiment results from BAjER, improves performance when learner queries the same string multiple times", defaultValue = "true", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private boolean cacheSul;

    private final LearnerFactoryRepository<Word<Integer>, Word<Integer>> learnerRepository;

    public LearnCommand(LearnerFactoryRepository<Word<Integer>, Word<Integer>> learnerRepository) {
        this.learnerRepository = learnerRepository;
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

        SUL<Word<Integer>, Word<Integer>> bajerSul = new SULClient<>(bajerClient, new IntegerWordInputAdapter(), new IntegerWordOutputAdapter(), (byte) inputCount, (byte) outputCount);

        var bajerMetricsSul = new PerformanceMetricSUL<>(bajerSul);
        if (benchmark) {
            bajerSul = bajerMetricsSul;
        }

        SUL<Word<Integer>, Word<Integer>> finalSul = null;

        if (cacheSul) {
            finalSul = SULCache.createTreeCache(alphabet, bajerSul);
        } else {
            finalSul = bajerSul;
        }


        var membershipOracle = new SULOracle<>(finalSul);

        var equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);

        var learnerFactory = learnerRepository.getLearnerFactory(learnerName);

        if (learnerFactory == null) {
            System.err.format(
                    "Unknown learner \"%s\", use the list-learners command to see the list of available learners\n",
                    learnerName);
            return 1;
        }

        var learner = learnerFactory.createLearner(alphabet, membershipOracle);

        var experiment = new Experiment.MealyExperiment<>(learner, equivalenceOracle,
                alphabet);

        experiment.run();

        var result = experiment.getFinalHypothesis();

        DOTSerializationProvider.getInstance().writeModel(outFile,
                (Graph) result.transitionGraphView(alphabet).asNormalGraph());

        if (benchmark) {
            System.out.println("Benchmark results:");
            System.out.format("total step time: %sms\n", bajerMetricsSul.getStepTime().toMillis());
            System.out.format("total pre time: %sms\n", bajerMetricsSul.getPreTime().toMillis());
            System.out.format("total post time: %sms\n", bajerMetricsSul.getPostTime().toMillis());
            System.out.format("experiment count: %s\n", bajerMetricsSul.getCounter());
            System.out.format("step count: %s\n", bajerMetricsSul.getStepCounter());
        }

        if (visualize) {
            Visualization.visualize(result, alphabet);
        }

        return 0;
    }
}