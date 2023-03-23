package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.commons.util.Pair;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.ArrayList;

public class Ladder<S, I extends Word<?>, T extends CompactMealyTransition<O>, O, M extends TransitionOutputAutomaton<S, I, T, O>, A extends Alphabet<I>> {

    public Ladder(EquationCollection<S, I, T, O, M, A> ec) {

        ArrayList<Rung> rungs = new ArrayList<>();
        for (Equation<S, I, O> equation : ec) {
            for (Pair<I, S> vari : equation.getFullList()) {
                Rung rung = new Rung();
                for (Object w : vari.getFirst()) {
                    String gatetype;
                    if (w.toString() == "true") {
                        gatetype = "NO";
                    } else {
                        gatetype = "NC";
                    }
                    Gate gate = new Gate(gatetype);
                    rung.gates.add(gate);
                }
                rung.outputvar = equation.output;
                rungs.add(rung);
            }

            System.out.println(rungs);
            // System.out.println(equation);
            // System.out.println(String.join("---",
            // equation.getFullList().stream().map(Object::toString).toList()));

        }

    }

    private class Rung {
        O outputvar;
        ArrayList<Gate> gates = new ArrayList<Gate>();

    }

    private class Gate {
        String Inputvar;
        String Type;

        public Gate(String type) {
            this.Type = type;
        }
    }
}
