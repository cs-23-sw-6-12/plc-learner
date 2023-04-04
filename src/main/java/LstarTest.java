import de.learnlib.algorithms.lstar.AbstractExtensibleAutomatonLStar;
import de.learnlib.algorithms.lstar.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstar.closing.ClosingStrategies;
import de.learnlib.algorithms.lstar.mealy.ClassicLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.SUL;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.oracle.equivalence.mealy.RandomWalkEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;
import org.cs23sw612.ExampleSUL;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

public class LstarTest {
    private static final double RESET_PROBABILITY = 0.05;
    private static final int MAX_STEPS = 10_000;
    private static final int RANDOM_SEED = 46_346_293;
    public static void main(String[] args) throws IOException {
        SUL<Word<Boolean>, Object> sul = ExampleSUL.createExampleSUL();
        SULOracle<Word<Boolean>, Object> mqOracle = new SULOracle<>(sul);

        ClassicLStarMealy<Word<Boolean>, Word<Object>> clstar = new ClassicLStarMealy<>(ExampleSUL.alphabet, mqOracle,
                ObservationTableCEXHandlers.CLASSIC_LSTAR, ClosingStrategies.CLOSE_SHORTEST);

        ExtensibleLStarMealy<Word<Boolean>, Object> elstar = new ExtensibleLStarMealyBuilder<Word<Boolean>, Object>()
                .withAlphabet(ExampleSUL.alphabet) // input alphabet
                .withOracle(mqOracle) // membership oracle
                .withInitialSuffixes(Collections.emptyList()) // initial suffixes
                .create();

        Visualization.visualize(ExampleSUL.createExample(), ExampleSUL.alphabet);
        runExample(sul, clstar);
        runExample(sul, elstar);
    }

    static void runExample(SUL<Word<Boolean>, Object> sul, AbstractExtensibleAutomatonLStar m) throws IOException {
        EquivalenceOracle.MealyEquivalenceOracle<Word<Boolean>, Object> randomWalks = new RandomWalkEQOracle<>(sul, // system
                                                                                                                    // under
                                                                                                                    // learning
                RESET_PROBABILITY, // reset SUL w/ this probability before a step
                MAX_STEPS, // max steps (overall)
                false, // reset step count after counterexample
                new Random(RANDOM_SEED) // make results reproducible
        );

        Experiment.MealyExperiment<Word<Boolean>, Object> experiment = new Experiment.MealyExperiment<>(m, randomWalks,
                ExampleSUL.alphabet);

        experiment.setProfile(true);
        experiment.setLogModels(true);
        experiment.run();

        // get learned model
        MealyMachine<?, Word<Boolean>, ?, Object> result = experiment.getFinalHypothesis();

        Visualization.visualize(result, ExampleSUL.alphabet);

        System.out.println("Final observation table:");
        new ObservationTableASCIIWriter<>().write(m.getObservationTable(), System.out);

        OTUtils.displayHTMLInBrowser(m.getObservationTable());
    }
}
