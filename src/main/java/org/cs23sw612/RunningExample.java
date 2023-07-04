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
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.Ladder.TruthTable;
import org.cs23sw612.SUL.ExampleSUL;
import org.cs23sw612.Util.Bit;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

public class RunningExample {
    private static void p(Object s) {
        System.out.println(s);
    }
    public static void main(String[] args) throws IOException {
        CompactMealy<Word<Bit>, Object> example = ExampleSUL.createExample();
        Alphabet<Word<Bit>> alphabet = example.getInputAlphabet();
        MealySimulatorSUL<Word<Bit>, Object> sul = ExampleSUL.createExampleSUL();

        // Standard mealy membership oracle.
        SULOracle<Word<Bit>, Object> membershipOracle = new SULOracle<>(sul);

        MealySimulatorEQOracle<Word<Bit>, Object> equivalenceOracle = new MealySimulatorEQOracle<>(example);

        ExtensibleLStarMealy<Word<Bit>, Object> learnerLstar = new ExtensibleLStarMealyBuilder<Word<Bit>, Object>()
                .withAlphabet(alphabet) // input alphabet
                .withOracle(membershipOracle) // membership oracle
                .withInitialSuffixes(Collections.emptyList()) // initial suffixes
                .create();
        TTTLearnerMealy<Word<Bit>, Object> learnerTTT = new TTTLearnerMealy<>(alphabet, membershipOracle,
                AcexAnalyzers.BINARY_SEARCH_BWD);
        MealyDHC<Word<Bit>, Object> learnerDHC = new MealyDHC<>(alphabet, membershipOracle);

        Experiment.MealyExperiment<Word<Bit>, Object> experimentLstar = new Experiment.MealyExperiment<>(learnerLstar,
                equivalenceOracle, alphabet);
        Experiment.MealyExperiment<Word<Bit>, Object> experimentTTT = new Experiment.MealyExperiment<>(learnerTTT,
                equivalenceOracle, alphabet);
        Experiment.MealyExperiment<Word<Bit>, Object> experimentDHC = new Experiment.MealyExperiment<>(learnerDHC,
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
        GraphDOT.write(learnerTTT.getDiscriminationTree(), actualDT);
        p(actualDT);

        p("DHC:");
        p(learnerDHC.getGlobalSuffixes());

        var t = new TruthTable(example, alphabet);
        p(t.toLatexTabularString());
        /*
         * EquationCollection<Integer, Word<Bit>, CompactMealyTransition<Object>,
         * Word<Bit>, CompactMealy<Word<Bit>, Object>, Alphabet<Word<Bit>>> ec = new
         * EquationCollection<>( example, alphabet); p("Truth table:");
         * p(ec.getTabularLatex());
         * 
         * p("Equations:"); ec.forEach(e -> p("\\item[]" + e));
         * 
         * p("Rungs:"); var ladder = new Ladder(ec);
         * ladder.outRungs.forEach(System.out::print);
         * ladder.stateRungs.forEach(System.out::print);
         * 
         * var visualizedSvg = Visualizer.layoutSVG(ladder);
         * Visualizer.showSVG(visualizedSvg);
         */
    }
}
