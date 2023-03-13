package org.cs_23_sw_6_12;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.driver.util.MealySimulatorSUL;
import de.learnlib.oracle.equivalence.MealySimulatorEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
/*
        MealyMachine example = ExampleSUL.createExample();
        int MAX_DEPTH = 3;

        TruthTable t = new TruthTable(example, ExampleSUL.alphabet);
        t.generateTable();
        Visualization.visualize(example, ExampleSUL.alphabet);

        Alphabet<I> alphabet = Alphabets.fromArray(
                new Boolean[]{true, false},
                new Boolean[]{true, true},
                new Boolean[]{false, true},
                new Boolean[]{false, false}
        );

        // System under learning. TODO: Change to example
        SULClient sul = SULClient.createBooleanArrayClient(
                new SULClientConfiguration(args[0], Integer.parseInt(args[1])));

        sul.numberofinputs = 2;
        sul.numberofoutputs = 2;
 */
        MealyMachine<Integer, Word<Boolean>, CompactMealyTransition<Object>, Object> example = ExampleSUL.createExample();
        MealySimulatorSUL<Word<Boolean>, Object> sul = ExampleSUL.createExampleSUL();
        // Standard mealy membership oracle.
        SULOracle<Word<Boolean>, Object> membershipOracle = new SULOracle<>(sul);

        MealySimulatorEQOracle<Word<Boolean>, Object> equivalenceOracle = new MealySimulatorEQOracle<>(example);

        MealyDHC<Word<Boolean>, Object> learner = new MealyDHC<>(ExampleSUL.alphabet, membershipOracle);

        Experiment.MealyExperiment<Word<Boolean>, Object> experiment =
                new Experiment.MealyExperiment<>(learner, equivalenceOracle, ExampleSUL.alphabet);

        experiment.run();

        MealyMachine<?, Word<Boolean>, ?, Object> result = experiment.getFinalHypothesis();

        TruthTable t = new TruthTable(result, ExampleSUL.alphabet);
        t.generateTable();
        Visualization.visualize(result, ExampleSUL.alphabet);

    }
}