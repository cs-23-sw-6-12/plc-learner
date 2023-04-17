package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.commons.util.Pair;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Ladder {
    public ArrayList<Rung> rungs;
    public <S extends Number, IO extends Word<Boolean>, T extends CompactMealyTransition<? super IO>> Ladder(
            EquationCollection<S, IO, T, IO, ? extends TransitionOutputAutomaton<S, IO, T, ? super IO>, Alphabet<IO>> ec) {
        rungs = new ArrayList<>();

        for (Equation<Word<Boolean>, IO, IO> equation : ec) {
            boolean first = true;
            for (Pair<Word<Boolean>, ? extends Word<Boolean>> StateInputPair : equation.getFullList()) {
                Rung rung;
                if ((long) equation.getFullList().size() > 1 && !first)
                    rung = new ORRung();
                else {
                    rung = new Rung();
                    rung.outputgate = new Coil(String.format("%S", convertState(equation.output)));
                }
                int inputParam = 1;
                for (Boolean word : StateInputPair.getSecond()) {
                    rung.add(word ? new NOC(Integer.toString(inputParam)) : new NCC((Integer.toString(inputParam))));
                    inputParam++;
                }
                for (int i = 0; i < StateInputPair.getFirst().length(); i++) {
                    rung.add(StateInputPair.getFirst().asList().get(i)
                            ? new NOC("S" + (i + 1))
                            : new NCC("S" + (i + 1)));
                }
                rungs.add(rung);
                first = false;
            }
        }
    }
    private String convertState(Word<Boolean> state) {
        return state.stream().map(i -> i ? "1" : "0").collect(Collectors.joining(""));
    }

    public class Rung {
        public Gate outputgate;
        public final ArrayList<Gate> gates = new ArrayList<>();
        @Override
        public String toString() {
            return "\n|----" + String.join("---", gates.stream().map(Gate::toString).toList()) + "----" + outputgate
                    + "--|";
        }
        public void add(Gate gate) {
            this.gates.add(gate);
        }
    }

    public class ORRung extends Rung {
        @Override
        public String toString() {
            return "\n  ᒻ--" + String.join("---", gates.stream().map(Gate::toString).toList()) + "--ᒽ";
        }
        @Override
        public void add(Gate gate) {
            this.gates.add(gate);
        }
    }

    public class Gate {
        public final String gate;
        public Gate(String gate) {
            this.gate = gate;
        }
        @Override
        public String toString() {
            return gate;
        }
    }

    public class NOC extends Gate {

        public NOC(String variable) {
            super(variable);
        }

        @Override
        public String toString() {
            return String.format("| %s|", gate);
        }
    }

    public class NCC extends Gate {

        public NCC(String variable) {
            super(variable);
        }

        @Override
        public String toString() {
            return String.format("|/%s|", gate);
        }
    }
    public class Coil extends Gate {
        public Coil(String variable) {
            super(variable);
        }

        @Override
        public String toString() {
            return String.format("(%s)", gate);
        }
    }
}
