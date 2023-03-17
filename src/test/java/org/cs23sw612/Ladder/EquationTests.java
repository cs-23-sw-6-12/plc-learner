package org.cs23sw612.Ladder;

import net.automatalib.automata.concepts.StateIDs;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.cs23sw612.ExampleSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class EquationTests {
    CompactMealy<Word<Boolean>, Object> testSystem = ExampleSUL.createExample();
    Alphabet<Word<Boolean>> testAlphabet = ExampleSUL.alphabet;

    @Test
    public void testAllTransitionsInTable() {
        EquationTable<Integer, Word<Boolean>, CompactMealyTransition<Object>, Object, CompactMealy<Word<Boolean>, Object>, Alphabet<Word<Boolean>>>
                testTab = new EquationCollection<>(testSystem, testAlphabet).getTable();
        StateIDs<Integer> stateIds = testSystem.stateIDs();
        testTab.getRawEquations().forEach(eq -> {
            CompactMealyTransition<Object> trans = testSystem.getTransition(eq.states(), eq.ins());
            assert trans != null;
            assert trans.getOutput() == eq.out();
            assert trans.getSuccId() == stateIds.getStateId(eq.nextStates());
            assert stateIds.getState(trans.getSuccId()) != null;
            assert Objects.equals(stateIds.getState(trans.getSuccId()), eq.nextStates());
            testSystem.removeTransition(eq.states(), eq.ins(), trans);
            assert testSystem.getTransition(eq.states(), eq.ins()) == null;
        });

        testSystem.forEach(s -> testAlphabet.forEach(w -> {
            assert testSystem.getTransition(s, w) == null;
        }));
    }

    @Test
    public void testCorrectEquationCollection() {
        EquationCollection<Integer, Word<Boolean>, CompactMealyTransition<Object>, Object, CompactMealy<Word<Boolean>, Object>, Alphabet<Word<Boolean>>>
                testCol = new EquationCollection<>(testSystem, testAlphabet);
        testCol.getEquations().forEach(eq -> eq.getFullList().forEach(p -> {
            CompactMealyTransition<Object> trans = testSystem.getTransition(p.getSecond(), p.getFirst());
            assert trans != null;
            assert trans.getOutput() == eq.output;
            testSystem.removeTransition(p.getSecond(), p.getFirst(), trans);
            assert testSystem.getTransition(p.getSecond(), p.getFirst()) == null;
        }));
        testSystem.forEach(s -> testAlphabet.forEach(w -> {
            assert testSystem.getTransition(s, w) == null;
        }));
    }
}
