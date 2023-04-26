package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.commons.util.Triple;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.*;
import java.util.stream.Collectors;

public class Ladder {
    public ArrayList<Rung> rungs;

    public <S extends Number, IO extends Word<Boolean>, T extends CompactMealyTransition<? super IO>> Ladder(
            EquationCollection<S, IO, T, IO, ? extends TransitionOutputAutomaton<S, IO, T, ? super IO>, Alphabet<IO>> ec) {
        rungs = new ArrayList<>();

        for (Equation<Word<Boolean>, IO, IO> equation : ec) {
            rungs.add(new Rung(equation));
        }

    }
    private String convertState(Word<Boolean> state) {
        return state.stream().map(i -> i ? "1" : "0").collect(Collectors.joining(""));
    }

    public class Rung {
        public final ArrayList<GateSequence> orRungs = new ArrayList<>();
        public final LinkedHashSet<Gate> outputGates = new LinkedHashSet<>();
        public GateSequence gates;

        public <IO extends Word<Boolean>> Rung(Equation<Word<Boolean>, IO, IO> equation) {
            this.outputGates.add(new Coil(convertState(equation.output))); // todo: fix
            for (Triple<Word<Boolean>, Word<Boolean>, IO> eqVals : equation.getFullList()) {
                this.add(new Rung(eqVals));
            }
        }

        private <IO extends Word<Boolean>> Rung(Triple<Word<Boolean>, Word<Boolean>, IO> eqVals) {
            gates = new GateSequence(eqVals.getThird()); //Inputs added

            int inputParam = 1;
            for (Boolean b : eqVals.getFirst())
                gates.add(new Gate("S" + inputParam++, b)); //States added

            inputParam = 1;
            for (Boolean b : eqVals.getSecond()) {
                if (b)
                    this.outputGates.add(new Coil("S" + inputParam++)); // Output states added
            }
        }

        @Override
        public String toString() { // todo fix ift outputGates
            return "\n|----" + gates + "----" + String.join("---", outputGates.stream().map(Gate::toString).toList())
                    + "--|\n" + String.join("\n",
                            orRungs.stream().map(GateSequence::toString).map(s -> "  ᒻ--" + s + "--ᒽ").toList());
        }

        private void add(Rung rung) {
            if (this.gates == null) {
                this.gates = rung.gates;
                this.outputGates.addAll(rung.outputGates);
            } else
                this.orRungs.add(rung.gates);
        }
    }

    public class GateSequence {
        private final ArrayList<Gate> gates;

        public GateSequence() {
            gates = new ArrayList<>();
        }

        public GateSequence(Word<Boolean> gates) {
            this();
            int i = 1;
            for (Boolean b : gates) {
                this.gates.add(new Gate(Integer.toString(i++), b));
            }
        }

        public void add(Gate gate) {
            this.gates.add(gate);
        }

        public int count() {
            return gates.size();
        }
        public Gate get(int i) {
            return gates.get(i);
        }
        @Override
        public String toString() { // todo fix ift outputGates
            return String.join("---", gates.stream().map(Gate::toString).toList());
        }
    }

    public class Gate {
        public final String gate;
        private final boolean open;
        public Gate(String gate, boolean open) {
            this.gate = gate;
            this.open = open;
        }

        public final boolean isOpen() {
            return open;
        }
        @Override
        public String toString() {
            return String.format("|%s%s|",open ? " " : "/", gate);
        }

        @Override
        public int hashCode() {
            return gate.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Gate && ((Gate) obj).gate.equals(gate);
        }
    }

    public class Coil extends Gate {

        public Coil(String variable) {
            super(variable, true);
        }

        @Override
        public String toString() {
            return String.format("(%s)", gate);
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }
    }
}
