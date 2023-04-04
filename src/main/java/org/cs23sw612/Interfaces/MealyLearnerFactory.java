package org.cs23sw612.Interfaces;

import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;

public interface MealyLearnerFactory<I, O> {
    String getName();
    LearningAlgorithm.MealyLearner<I, O> createLearner(Alphabet<I> alphabet, SULOracle<I, O> membershipOracle);
}
