package org.cs23sw612.Experiments.Util.Benchmark;

import de.learnlib.api.SUL;
import de.learnlib.api.query.Query;
import de.learnlib.filter.statistic.Counter;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Word;

import java.util.Collection;

public class MQStatisticsOracle<I, O> extends SULOracle<I, O> {
    private final Counter mqCounter = new Counter("Membership Queries", "#");

    /**
     * The MQ Counter counts how many MQs are tested on the sul.
     * 
     * @return the MQ counter.
     */
    public Counter getMqCounter() {
        return mqCounter;
    }
    private final Counter mqProcessCounter = new Counter("Membership Queries Processed", "#");

    /**
     * The MQ Process counter counts how many queries are processed. NOT how many
     * queries are tested.
     * 
     * @return the process counter.
     */
    public Counter getMqProcessCounter() {
        return mqProcessCounter;
    }
    public MQStatisticsOracle(SUL<I, O> sul) {
        super(sul);
    }

    public void processQueries(Collection<? extends Query<I, Word<O>>> queries) {
        mqProcessCounter.increment();
        super.processQueries(queries);
    }

    public Word<O> answerQuery(Word<I> prefix, Word<I> suffix) {
        mqCounter.increment();
        System.out.println("Answering MQ " + mqCounter.getCount());
        return super.answerQuery(prefix, suffix);
    }

}
