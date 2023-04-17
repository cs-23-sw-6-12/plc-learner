package org.cs23sw612.LearnerFactories;

import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;
import org.cs23sw612.Interfaces.MealyLearnerFactory;

public class LStarLearnerFactory<I, O> implements MealyLearnerFactory<I, O> {
    @Override
    public String getName() {
        return "Lstar";
    }

    @Override
    public LearningAlgorithm.MealyLearner<I, O> createLearner(Alphabet<I> alphabet, SULOracle<I, O> membershipOracle) {
        return new ExtensibleLStarMealyBuilder<I, O>().withAlphabet(alphabet).withOracle(membershipOracle).create();
    }
}