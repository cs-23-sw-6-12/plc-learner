package org.cs23sw612.OracleFactories;

import de.learnlib.api.SUL;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.oracle.equivalence.mealy.RandomWalkEQOracle;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.words.Word;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;

import java.util.Random;

public class RandomWalkOracleFactory implements OracleFactory {
    public RandomWalkOracleFactory() {

    }

    @Override
    public String getName() {
        return "random-walk";
    }

    @Override
    public EquivalenceOracle<MealyMachine<?, Word<Integer>, ?, Word<Integer>>, Word<Integer>, Word<Word<Integer>>> createOracle(
            SUL<Word<Integer>, Word<Integer>> sul, OracleConfig oracleConfig) {
        return new RandomWalkEQOracle<>(sul, oracleConfig.restartProbability, oracleConfig.maxSteps, false,
                new Random());
    }
}
