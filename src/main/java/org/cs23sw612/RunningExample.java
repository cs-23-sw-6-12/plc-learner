package org.cs23sw612;

import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.driver.util.MealySimulatorSUL;
import de.learnlib.oracle.equivalence.MealySimulatorEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

public class RunningExample {

    // private static final Alphabet<Word<Boolean>> alphabet =
    // AlphabetUtil.createAlphabet(2);
    public static void main(String[] args) throws IOException {
        CompactMealy<Word<Boolean>, Object> example = ExampleSUL.createExample();
        Alphabet<Word<Boolean>> alphabet = ExampleSUL.alphabet;
        MealySimulatorSUL<Word<Boolean>, Object> sul = ExampleSUL.createExampleSUL();

        // Standard mealy membership oracle.
        SULOracle<Word<Boolean>, Object> membershipOracle = new SULOracle<>(sul);

        MealySimulatorEQOracle<Word<Boolean>, Object> equivalenceOracle = new MealySimulatorEQOracle<>(example);

        ExtensibleLStarMealy<Word<Boolean>, Object> learnerLstar = new ExtensibleLStarMealyBuilder<Word<Boolean>, Object>()
                .withAlphabet(alphabet) // input alphabet
                .withOracle(membershipOracle) // membership oracle
                .withInitialSuffixes(Collections.emptyList()) // initial suffixes
                .create();
        TTTLearnerMealy<Word<Boolean>, Object> learnerTTT = new TTTLearnerMealy<>(alphabet, membershipOracle,
                AcexAnalyzers.BINARY_SEARCH_BWD);
        MealyDHC<Word<Boolean>, Object> learnerDHC = new MealyDHC<>(alphabet, membershipOracle);

        Experiment.MealyExperiment<Word<Boolean>, Object> experimentLstar = new Experiment.MealyExperiment<>(
                learnerLstar, equivalenceOracle, alphabet);
        Experiment.MealyExperiment<Word<Boolean>, Object> experimentTTT = new Experiment.MealyExperiment<>(learnerTTT,
                equivalenceOracle, alphabet);
        Experiment.MealyExperiment<Word<Boolean>, Object> experimentDHC = new Experiment.MealyExperiment<>(learnerDHC,
                equivalenceOracle, alphabet);

        experimentLstar.run();
        experimentTTT.run();
        experimentDHC.run();

        StringWriter actualDT = new StringWriter();
        System.out.println("Mealy stuff:");
        GraphDOT.write(example, actualDT);
        System.out.println(actualDT);

        System.out.println("L*:");
        new ObservationTableASCIIWriter<>().write(learnerLstar.getObservationTable(), System.out);
        OTUtils.displayHTMLInBrowser(learnerLstar.getObservationTable());

        System.out.println("TTT:");
        actualDT = new StringWriter();
        GraphDOT.write(learnerTTT.getHypothesisDS(), actualDT);
        System.out.println(actualDT);
        actualDT = new StringWriter();
        GraphDOT.write(learnerTTT.getDiscriminationTree(), actualDT);
        System.out.println(actualDT);

        System.out.println("DHC:");
        System.out.println(learnerDHC.getGlobalSuffixes());

        //TODO: Tables and equations
    }
}
