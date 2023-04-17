package org.cs23sw612.Interfaces;

import de.learnlib.api.SUL;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.words.Word;
import org.cs23sw612.OracleConfig;

public interface OracleFactory {
    String getName();
    //The config should be different for each factory, and then that config should be built into picocli at run time
    //However this takes alot of time, so for now it will just be a global config
    EquivalenceOracle<MealyMachine<?, Word<Integer>, ?, Word<Integer>>, Word<Integer>, Word<Word<Integer>>> createOracle(SUL<Word<Integer>, Word<Integer>> sul, OracleConfig oracleConfig);
}
