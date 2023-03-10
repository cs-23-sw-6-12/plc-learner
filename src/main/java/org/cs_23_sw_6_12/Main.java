package org.cs_23_sw_6_12;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.concepts.Output;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.cs_23_sw_6_12.Adapters.InputAdapter;
import org.cs_23_sw_6_12.Adapters.OutputAdapter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int MAX_DEPTH = 3;

        SULClient<Word<Boolean>, InputAdapter<Word<Boolean>>, Word<Boolean>, OutputAdapter<Word<Boolean>>> sul = SULClient.createBooleanWordClient(
                new SULClientConfiguration(args[0], Integer.parseInt(args[1])));
        sul.numberofinputs = 2;
        sul.numberofoutputs = 2;

        Alphabet<Word<Boolean>> alphabet = Alphabets.fromArray(
                Word.fromSymbols(true, false),
                Word.fromSymbols(true, true),
                Word.fromSymbols(false,true),
                Word.fromSymbols(false, false)
        );

        SULWrapper<Word<Boolean>, Word<Boolean>> wrapper = new SULWrapper<>(sul);

        // Standard mealy membership oracle.
        SULCache<Word<Boolean>, Word<Boolean>> cache = SULCache.createTreeCache(alphabet,wrapper);
        SULOracle<Word<Boolean>, Word<Boolean>> membershipOracle = new SULOracle<>(cache);

        CompleteExplorationEQOracle<Output<Word<Boolean>, Word<Word<Boolean>>>, Word<Boolean>, Word<Word<Boolean>>> equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);

        MealyDHC<Word<Boolean>, Word<Boolean>> learner = new MealyDHC<>(alphabet, membershipOracle);

        Experiment.MealyExperiment<Word<Boolean>, Word<Boolean>> experiment =
                new Experiment.MealyExperiment<>(learner, equivalenceOracle,alphabet);

        experiment.run();

        System.out.println("Final hypothesis found in: " + wrapper.getCounter() + " steps.");

        MealyMachine<?, Word<Boolean>, ?, Word<Boolean>> result = experiment.getFinalHypothesis();

        Visualization.visualize(result, alphabet);
    }
}