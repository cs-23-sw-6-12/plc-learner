package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

public class Ladder<S, I extends Word<?>, T extends CompactMealyTransition<O>, O, M extends TransitionOutputAutomaton<S, I, T, O>, A extends Alphabet<I>> {

    public Ladder(EquationCollection<S, I, T, O, M, A> ec) {
        
    }


}
