package org.cs23sw612.Experiments.Util.Benchmark;

import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.query.DefaultQuery;
import de.learnlib.filter.statistic.Counter;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.words.Word;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class EQStatisticsOracle<I, O> implements EquivalenceOracle<MealyMachine<?, I, ?, O>, I, Word<O>> {

    private final Counter counter = new Counter("Equivalence Queries", "#");
    public Counter getCounter() {
        return counter;
    }

    private final EquivalenceOracle<MealyMachine<?, I, ?, O>, I, Word<O>> oracle;

    public EQStatisticsOracle(EquivalenceOracle<MealyMachine<?, I, ?, O>, I, Word<O>> EqOracle) {
        oracle = EqOracle;
    }

    @Override
    public @Nullable DefaultQuery<I, Word<O>> findCounterExample(MealyMachine<?, I, ?, O> a,
            Collection<? extends I> collection) {
        counter.increment();
        return oracle.findCounterExample(a, collection);
    }
}
