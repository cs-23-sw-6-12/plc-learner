package org.cs23sw612.Ladder;

import java.util.Objects;
import net.automatalib.automata.concepts.StateIDs;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.ExampleSUL;
import org.junit.jupiter.api.Test;

public class EquationTests {
    CompactMealy<Word<Boolean>, Object> testSystem = ExampleSUL.createExample();
    Alphabet<Word<Boolean>> testAlphabet = ExampleSUL.alphabet;

    private static int b(Word<Boolean> w) {
        int out = 0;
        for (Boolean b : w) {
            out <<= 1;
            out |= b ? 1 : 0;
        }
        return out;
    }
    @Test
    public void testAllTransitionsInTable() {
        var testTab = new EquationCollection<>(testSystem, testAlphabet).getTable();
        StateIDs<Integer> stateIds = testSystem.stateIDs();
        testTab.getEquations().forEach(eq -> {
            CompactMealyTransition<Object> trans = testSystem.getTransition(b(eq.state()), eq.input());
            assert trans != null;
            assert trans.getOutput() == eq.output();
            assert trans.getSuccId() == stateIds.getStateId(b(eq.nextState()));
            assert stateIds.getState(trans.getSuccId()) != null;
            assert Objects.equals(stateIds.getState(trans.getSuccId()), b(eq.nextState()));
            testSystem.removeTransition(b(eq.state()), eq.input(), trans);
            assert testSystem.getTransition(b(eq.state()), eq.input()) == null;
        });

        testSystem.forEach(s -> testAlphabet.forEach(w -> {
            assert testSystem.getTransition(s, w) == null;
        }));
    }

    @Test
    public void testCorrectEquationCollection() {
        var testCol = new EquationCollection<>(testSystem, testAlphabet);
        testCol.forEach(eq -> eq.getFullList().forEach(p -> {
            CompactMealyTransition<Object> trans = testSystem.getTransition(b(p.getFirst()), p.getSecond());
            assert trans != null;
            assert trans.getOutput() == eq.output;
            testSystem.removeTransition(b(p.getFirst()), p.getSecond(), trans);
            assert testSystem.getTransition(b(p.getFirst()), p.getSecond()) == null;
        }));
        testSystem.forEach(s -> testAlphabet.forEach(w -> {
            assert testSystem.getTransition(s, w) == null;
        }));
    }
}
