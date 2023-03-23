package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

public class Ladder<S extends Number, I extends Word<?>, T extends CompactMealyTransition<O>, O extends Word<?>, M extends TransitionOutputAutomaton<S, I, T, O>, A extends Alphabet<I>> {

    public Ladder(EquationCollection<S, I, T, O, M, A> ec) {
        ec.forEach(e -> {
            System.out.println("based");
            e.getFullList().get(0).getFirst().forEach(System.out::println);
        });
    }
}
