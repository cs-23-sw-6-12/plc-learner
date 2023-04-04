package org.cs23sw612.commands;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Adapters.Input.IntegerWordInputAdapter;
import org.cs23sw612.Adapters.Output.IntegerWordOutputAdapter;
import org.cs23sw612.SULClient;
import org.cs23sw612.SULClientConfiguration;
import org.cs23sw612.Util.AlphabetUtil;
import org.cs23sw612.Util.LearnerFactoryRepository;
import picocli.CommandLine;

import java.net.InetAddress;
import java.util.concurrent.Callable;


@CommandLine.Command(name = "learn", mixinStandardHelpOptions = true, version="0.1.0", description="Learns a PLCs logic")
public class LearnCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Number of inputs")
    private int inputCount;

    @CommandLine.Parameters(index = "1", description = "Number of outputs")
    private int outputCount;

    @CommandLine.Parameters(index = "2", description = "BAjER server address")
    private String bajerServerAddress;

    @CommandLine.Option(names = {"--port", "-p"}, description = "port for the BAjER server", defaultValue = "1337", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private int bajerServerPort;

    @CommandLine.Option(names = {"--learner", "-l"}, description = "Learner to use (case insensitive)", defaultValue = "DHC", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String learnerName;
    @Override
    public Integer call() throws Exception {
        var sul = SULClient.createClient(
                new SULClientConfiguration(bajerServerAddress, bajerServerPort),
                new IntegerWordInputAdapter(),
                new IntegerWordOutputAdapter()
        );
        sul.numberofinputs = inputCount;
        sul.numberofoutputs = outputCount;

        var alphabet = AlphabetUtil.createIntAlphabet(inputCount);
        var cache = SULCache.createTreeCache(alphabet, sul);

        var membershipOracle = new SULOracle<>(cache);

        var equivalenceOracle = new CompleteExplorationEQOracle<>(
                membershipOracle, 3);

        var learnerRepo = new LearnerFactoryRepository<Word<Integer>, Word<Integer>>();
        learnerRepo.addDefaultFactories();

        var learnerFactory = learnerRepo.getLearnerFactory(learnerName);

        if (learnerFactory == null) {
            System.err.format("Unknown learner \"%s\", use one of the following learners\n", learnerName);
            learnerRepo.getLearnerNames().forEach(learnerName -> {
                System.err.format("- %s\n", learnerName);
            });
            return 1;
        }

        var learner = learnerFactory.createLearner(alphabet, membershipOracle);

        var experiment = new Experiment.MealyExperiment<>(learner,
                equivalenceOracle, alphabet);

        experiment.run();

        var result = experiment.getFinalHypothesis();

        Visualization.visualize(result, alphabet);

        return 0;
    }
}