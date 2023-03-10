package org.cs_23_sw_6_12;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.filter.statistic.oracle.MealyCounterOracle;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int MAX_DEPTH = 3;

        // System under learning.
/*
 */
        var sul = SULClient.createBooleanWordClient(
                new SULClientConfiguration(args[0], Integer.parseInt(args[1])));
        sul.numberofinputs = 2;
        sul.numberofoutputs = 2;

        var alphabet = Alphabets.fromArray(
                Word.fromSymbols(true, false),
                Word.fromSymbols(true, true),
                Word.fromSymbols(false,true),
                Word.fromSymbols(false, false)
        );


        //var esul = ExampleSUL.createExample();
        //var compare = new CompareSULWrapper<>(sul, esul);
        var wrapper = new SULWrapper<>(sul);

        // Standard mealy membership oracle.
        //var cache = SULCache.createTreeCache(alphabet,wrapper);
        var membershipOracle = new SULOracle<>(wrapper);

        var equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);
        /*
        var equivalenceOracle =
                new ClockExplorationEQOracle<>(sul, MAX_DEPTH);
        */

        var learner = new MealyDHC<>(alphabet, membershipOracle);

        var experiment =
                new Experiment.MealyExperiment<>(learner, equivalenceOracle,alphabet);

        experiment.run();

        System.out.println(experiment.getRounds().getSummary());
        System.out.println(wrapper.getCounter());

        var result = experiment.getFinalHypothesis();

        Visualization.visualize(result, alphabet);
    }
}