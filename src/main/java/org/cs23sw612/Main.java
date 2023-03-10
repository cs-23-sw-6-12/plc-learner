package org.cs23sw612;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.filter.cache.sul.SULCache;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import java.io.IOException;
import net.automatalib.automata.concepts.Output;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Adapters.OutputAdapter;

public class Main {
  public static void main(String[] args) throws IOException {

    SULClient<
            Word<Boolean>, InputAdapter<Word<Boolean>>, Word<Boolean>, OutputAdapter<Word<Boolean>>>
        sul =
            SULClient.createBooleanWordClient(
                new SULClientConfiguration(args[0], Integer.parseInt(args[1])));
    sul.numberofinputs = 2;
    sul.numberofoutputs = 2;

    Alphabet<Word<Boolean>> alphabet =
        Alphabets.fromArray(
            Word.fromSymbols(true, false),
            Word.fromSymbols(true, true),
            Word.fromSymbols(false, true),
            Word.fromSymbols(false, false));

    // This cache reduces the amount of "queries" that are actually executed on the SUL.
    SULCache<Word<Boolean>, Word<Boolean>> cache = SULCache.createTreeCache(alphabet, sul);

    // Standard mealy membership oracle.
    SULOracle<Word<Boolean>, Word<Boolean>> membershipOracle = new SULOracle<>(cache);

    CompleteExplorationEQOracle<
            Output<Word<Boolean>, Word<Word<Boolean>>>, Word<Boolean>, Word<Word<Boolean>>>
        equivalenceOracle = new CompleteExplorationEQOracle<>(membershipOracle, 3);

    MealyDHC<Word<Boolean>, Word<Boolean>> learner = new MealyDHC<>(alphabet, membershipOracle);

    Experiment.MealyExperiment<Word<Boolean>, Word<Boolean>> experiment =
        new Experiment.MealyExperiment<>(learner, equivalenceOracle, alphabet);

    experiment.run();

    MealyMachine<?, Word<Boolean>, ?, Word<Boolean>> result = experiment.getFinalHypothesis();

    Visualization.visualize(result, alphabet);
  }
}
