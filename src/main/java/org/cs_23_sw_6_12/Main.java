package org.cs_23_sw_6_12;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import org.cs_23_sw_6_12.Interfaces.SULTimed;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        int MAX_DEPTH = 3;
        Alphabet<String> alphabet = null;

        // System under learning.
        SULTimed<String, String> sul = null;
        try {
            sul = SULClient.createStringClient(
                    new SULClientConfiguration("localhost",3000));
        } catch (IOException e) {
            // If the connection to SULClient fails.
            throw new RuntimeException(e);
        }

        // Standard mealy membership oracle.
        SULOracle<String, String> membershipOracle = new SULOracle<>(sul);

        EquivalenceOracle.MealyEquivalenceOracle<String,String> equivalenceOracle =
                new ClockExplorationEQOracle<>(sul, MAX_DEPTH);

        MealyDHC<String, String> learner = new MealyDHC<>(alphabet, membershipOracle);

        Experiment.MealyExperiment<String, String> experiment =
                new Experiment.MealyExperiment<>(learner, equivalenceOracle,alphabet);

        experiment.run();

        MealyMachine<?, String, ?, String> result = experiment.getFinalHypothesis();

        Visualization.visualize(result, alphabet);
    }
}