package org.cs23sw612;

import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.driver.util.MealySimulatorSUL;
import de.learnlib.oracle.equivalence.MealySimulatorEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Ladder.EquationCollection;
import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.Visualizer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.function.Consumer;

public class RunningExample {

    // private static final Alphabet<Word<Boolean>> alphabet =
    // AlphabetUtil.createAlphabet(2);
    private static void p(Object s) {
        System.out.println(s);
    }
    private static Consumer<? super Object> p = System.out::println;

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
        p("Mealy stuff:");
        GraphDOT.write(example, actualDT);
        p(actualDT);

        p("L*:");
        new ObservationTableASCIIWriter<>().write(learnerLstar.getObservationTable(), System.out);
        // OTUtils.displayHTMLInBrowser(learnerLstar.getObservationTable());

        p("TTT (Sp & DT):");
        actualDT = new StringWriter();
        GraphDOT.write(learnerTTT.getHypothesisDS(), actualDT);
        // p(actualDT);
        // actualDT = new StringWriter();
        GraphDOT.write(learnerTTT.getDiscriminationTree(), actualDT);
        p(actualDT);

        p("DHC:");
        p(learnerDHC.getGlobalSuffixes());

        EquationCollection<Integer, Word<Boolean>, CompactMealyTransition<Object>, Word<Boolean>, CompactMealy<Word<Boolean>, Object>, Alphabet<Word<Boolean>>> ec = new EquationCollection<>(
                example, alphabet);
        p("Truth table:");
        p(ec.getTabularLatex());

        p("Equations:");
        ec.forEach(e -> p("\\item[]" + e));

        p("Rungs:");
        new Ladder(ec).rungs.forEach(System.out::print);

        var e = new EquationCollection(example, alphabet);
        var l = new Ladder(e);

        Visualizer.showSVG(l);
    }
}
