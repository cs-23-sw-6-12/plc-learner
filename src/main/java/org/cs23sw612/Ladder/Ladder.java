package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.commons.util.Triple;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class Ladder {
    public ArrayList<Rung> rungs;

    public <S extends Number, IO extends Word<Boolean>, T extends CompactMealyTransition<? super IO>> Ladder(
            EquationCollection<S, IO, T, IO, ? extends TransitionOutputAutomaton<S, IO, T, ? super IO>, Alphabet<IO>> ec) {
        rungs = new ArrayList<>();

        for (Equation<Word<Boolean>, IO, IO> equation : ec) {
            boolean first = true;
            Rung mainRung = new Rung();
            for (Triple<Word<Boolean>, Word<Boolean>, ? extends Word<Boolean>> eqVals : equation.getFullList()) {
                Rung rung;
                if ((long) equation.getFullList().size() > 1 && !first)
                    rung = new ORRung();
                else {
                    rung = mainRung;
                    rung.outputGates.add(new Coil(String.format("%S", convertState(equation.output))));
                }

                if(eqVals.getSecond().toString().contains("true")){
                    for (int i = 0; i < eqVals.getSecond().length(); i++) {
                        if (eqVals.getSecond().asList().get(i).equals(true)) {
                            mainRung.outputGates.add(new Coil("S"+(i+1)));
                        }
                    }
                }

                int inputParam = 1;
                for (Boolean word : eqVals.getThird()) {
                    rung.add(word ? new NOC(Integer.toString(inputParam)) : new NCC((Integer.toString(inputParam))));
                    inputParam++;
                }
                for (int i = 0; i < eqVals.getFirst().length(); i++) {
                    rung.add(eqVals.getFirst().asList().get(i) ? new NOC("S" + (i + 1)) : new NCC("S" + (i + 1)));
                }

                if(!first)
                    mainRung.ORRungs.add(rung);
                else
                    rungs.add(rung);

                first = false;
            }
        }

    }
    private String convertState(Word<Boolean> state) {
        return state.stream().map(i -> i ? "1" : "0").collect(Collectors.joining(""));
    }

    public class Rung {
        public ArrayList<Rung> ORRungs = new ArrayList<>();
        public LinkedHashSet<Gate> outputGates = new LinkedHashSet<>();
        public final ArrayList<Gate> gates = new ArrayList<>();
        @Override
        public String toString() { //todo fix ift outputGates
            return "\n|----" + String.join("---", gates.stream().map(Gate::toString).toList()) + "----" + String.join("---", outputGates.stream().map(Gate::toString).toList())
                    + "--|" + ORRungs;
        }
        public void add(Gate gate) {
            this.gates.add(gate);
        }
    }

    public class ORRung extends Rung {
        @Override
        public String toString() {
            return "\n  ᒻ--" + String.join("---", gates.stream().map(Gate::toString).toList()) + "--ᒽ" + String.join("---", outputGates.stream().map(Gate::toString).toList())
      ;
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

        @Override
        public int hashCode(){
            return gate.hashCode();
        }

        @Override
        public boolean equals(Object obj){
            return ((Gate) obj).gate.equals(gate);
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

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }
    }
}
