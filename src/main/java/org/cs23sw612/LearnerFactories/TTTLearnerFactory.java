package org.cs23sw612.LearnerFactories;

import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Alphabet;
import org.cs23sw612.Interfaces.MealyLearnerFactory;

public class TTTLearnerFactory<I,O> implements MealyLearnerFactory<I,O> {
    @Override
    public String getName() {
        return "TTT";
    }

    @Override
    public LearningAlgorithm.MealyLearner<I,O> createLearner(Alphabet<I> alphabet, SULOracle<I, O> membershipOracle) {
        return new TTTLearnerMealy<>(alphabet, membershipOracle, AcexAnalyzers.BINARY_SEARCH_BWD);
    }
}
