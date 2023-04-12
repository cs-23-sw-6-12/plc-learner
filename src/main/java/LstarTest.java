import de.learnlib.algorithms.lstar.AbstractExtensibleAutomatonLStar;
import de.learnlib.algorithms.lstar.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstar.closing.ClosingStrategies;
import de.learnlib.algorithms.lstar.mealy.ClassicLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.SUL;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.driver.util.MealySimulatorSUL;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.equivalence.MealySimulatorEQOracle;
import de.learnlib.oracle.equivalence.mealy.RandomWalkEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;
import org.cs23sw612.ExampleSUL;
import org.cs23sw612.Util.MachineEquivalence;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

public class LstarTest {
    private static final double RESET_PROBABILITY = 0.05;
    private static final int MAX_STEPS = 10_000;
    private static final int RANDOM_SEED = 46_346_293;
    public static void main(String[] args) throws IOException {
        assert 1 == 2;// : String.format("Unknown learner \"%s\", use the list-learners command to see
                        // the list of available learners\n", "learnerName");
        CompactMealy<Word<Boolean>, Object> example = ExampleSUL.createExample();
        MealySimulatorSUL<Word<Boolean>, Object> sul = ExampleSUL.createExampleSUL();
        System.out.println("gamer");

        var cache = SULCache.createTreeCache(ExampleSUL.alphabet, sul);

        SULOracle<Word<Boolean>, Object> mqOracle = new SULOracle<>(sul);
        ClassicLStarMealy<Word<Boolean>, Word<Object>> clstar = new ClassicLStarMealy<>(ExampleSUL.alphabet, mqOracle,
                ObservationTableCEXHandlers.CLASSIC_LSTAR, ClosingStrategies.CLOSE_SHORTEST);

        ExtensibleLStarMealy<Word<Boolean>, Object> elstar = new ExtensibleLStarMealyBuilder<Word<Boolean>, Object>()
                .withAlphabet(ExampleSUL.alphabet) // input alphabet
                .withOracle(mqOracle) // membership oracle
                .withInitialSuffixes(Collections.emptyList()) // initial suffixes
                .withClosingStrategy(ClosingStrategies.CLOSE_SHORTEST)
                .withCexHandler(ObservationTableCEXHandlers.CLASSIC_LSTAR).create();

        Visualization.visualize(example, ExampleSUL.alphabet);
        // runExample(sul, clstar, mqOracle);
        var res = runExample(sul, elstar, mqOracle);
        System.out.println(MachineEquivalence.compactMealyEq(example, res, example.getInputAlphabet()));
    }

    static CompactMealy<Word<Boolean>, Object> runExample(SUL<Word<Boolean>, Object> sul,
            AbstractExtensibleAutomatonLStar m, SULOracle<Word<Boolean>, Object> mqOracle) throws IOException {
        CompleteExplorationEQOracle eq = new CompleteExplorationEQOracle(mqOracle, ExampleSUL.alphabet.size() + 7);
        EquivalenceOracle.MealyEquivalenceOracle<Word<Boolean>, Object> equivalenceOracle = new MealySimulatorEQOracle<>(
                ExampleSUL.createExample());
        EquivalenceOracle.MealyEquivalenceOracle<Word<Boolean>, Object> randomWalks = new RandomWalkEQOracle<>(sul,
                RESET_PROBABILITY, MAX_STEPS, false, new Random(RANDOM_SEED));

        Experiment.MealyExperiment<Word<Boolean>, Object> experiment = new Experiment.MealyExperiment<>(m, randomWalks,
                ExampleSUL.alphabet);

        experiment.setProfile(true);
        experiment.setLogModels(true);
        experiment.run();

        CompactMealy<Word<Boolean>, Object> result = (CompactMealy<Word<Boolean>, Object>) experiment
                .getFinalHypothesis();
        // result.removeAllTransitions(0);

        Visualization.visualize(result, ExampleSUL.alphabet);

        // System.out.println("Final observation table:");
        // new ObservationTableASCIIWriter<>().write(m.getObservationTable(),
        // System.out);

        // ObservationTable<Word<Boolean>, Object> tab = m.getObservationTable();
        // OTUtils.displayHTMLInBrowser(tab);
        return result;
    }
}
