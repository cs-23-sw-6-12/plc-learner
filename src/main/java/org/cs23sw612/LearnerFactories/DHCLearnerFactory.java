package org.cs23sw612.LearnerFactories;

import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;
import org.cs23sw612.Interfaces.MealyLearnerFactory;

public class DHCLearnerFactory<I, O> implements MealyLearnerFactory<I, O> {
    @Override
    public String getName() {
        return "DHC";
    }

    @Override
    public LearningAlgorithm.MealyLearner<I, O> createLearner(Alphabet<I> alphabet, SULOracle<I, O> membershipOracle) {
        return new MealyDHC<>(alphabet, membershipOracle);
    }
}
