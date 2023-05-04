package org.cs23sw612.OracleFactories;

import de.learnlib.api.SUL;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.oracle.equivalence.CompleteExplorationEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.words.Word;
import org.cs23sw612.Interfaces.OracleFactory;
import org.cs23sw612.OracleConfig;

public class CompleteExplorationOracleFactory<I, O> implements OracleFactory<I, O> {
    @Override
    public String getName() {
        return "complete-exploration";
    }

    @Override
    public EquivalenceOracle<MealyMachine<?, I, ?, O>, I, Word<O>> createOracle(SUL<I, O> sul,
            OracleConfig oracleConfig) {
        return new CompleteExplorationEQOracle<>(new SULOracle<>(sul), oracleConfig.depth);
    }
}
