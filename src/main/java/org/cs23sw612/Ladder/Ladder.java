package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.commons.util.Pair;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Ladder<S extends Number, I extends Word<Boolean>, T extends CompactMealyTransition<? super O>, O extends Word<Boolean>, M extends TransitionOutputAutomaton<S, I, T, ? super O>, A extends Alphabet<I>> {
    public ArrayList<Rung> rungs;
    public Ladder(EquationCollection<S, I, T, O, M, A> ec) {
        rungs = new ArrayList<>();

        for (Equation<Word<Boolean>, I, O> equation : ec) {
            boolean first = true;
            for (Pair<Word<Boolean>, I> StateInputPair : equation.getFullList()) {
                Rung rung;
                if ((long) equation.getFullList().size() > 1 && !first)
                    rung = new ORRung();
                else {
                    rung = new Rung();
                    rung.outputgate = new Gate(String.format("(%S)", convertState(equation.output)));
                }
                int inputParam = 1;
                for (Boolean word : StateInputPair.getSecond()) {
                    rung.add(new Gate(word ? String.format("| %s|", inputParam) : String.format("|/%s|", inputParam)));
                    inputParam++;
                }
                rung.add(new Gate(String.format("|State %s|", convertState(StateInputPair.getFirst()))));
                rungs.add(rung);
                first = false;
            }
        }
    }
    private String convertState(Word<Boolean> state) {
        return state.stream().map(i -> i ? "1" : "0").collect(Collectors.joining(""));
    }

    private class Rung {
        private Gate outputgate;
        private final ArrayList<Gate> gates = new ArrayList<>();
        @Override
        public String toString() {
            return "\n|----" + String.join("---", gates.stream().map(Gate::toString).toList()) + "----" + outputgate
                    + "--|";
        }
        public void add(Gate gate) {
            this.gates.add(gate);
        }
    }

    private class ORRung extends Rung {
        private final ArrayList<Gate> gates = new ArrayList<>();
        @Override
        public String toString() {
            return "\n  ᒻ--" + String.join("---", gates.stream().map(Gate::toString).toList()) + "--ᒽ";
        }
        @Override
        public void add(Gate gate) {
            this.gates.add(gate);
        }
    }

    private class Gate {
        private final String gate;
        public Gate(String gate) {
            this.gate = gate;
        }
        @Override
        public String toString() {
            return gate;
        }
    }
}
