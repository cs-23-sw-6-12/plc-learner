package org.cs_23_sw_6_12;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;
import org.cs_23_sw_6_12.Interfaces.SULTimed;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int MAX_DEPTH = 3;

        Alphabet<Boolean[]> alphabet = Alphabets.fromArray(
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

        // Standard mealy membership oracle.
        SULOracle<Boolean[], Boolean[]> membershipOracle = new SULOracle<>(sul);

        EquivalenceOracle.MealyEquivalenceOracle<Boolean[], Boolean[]> equivalenceOracle =
                new ClockExplorationEQOracle<>(sul, MAX_DEPTH);

        MealyDHC<Boolean[], Boolean[]> learner = new MealyDHC<>(alphabet, membershipOracle);

        Experiment.MealyExperiment<Boolean[], Boolean[]> experiment =
                new Experiment.MealyExperiment<>(learner, equivalenceOracle, alphabet);

        experiment.run();

        MealyMachine<?, Boolean[], ?, Boolean[]> result = experiment.getFinalHypothesis();

        Visualization.visualize(result, alphabet);
    }
}