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
                int i = 1;
                for (Object w : vari.getFirst()) {
                    String gatetype;
                    if (w.toString() == "true") {
                        gatetype = "NO";
                    } else {
                        gatetype = "NC";
                    }
                    Gate gate = new Gate(gatetype, Integer.toString(i));
                    rung.gates.add(gate);
                    i++;
                }
                Gate stategate = new Gate("NO", "State " + vari.getSecond().toString());
                rung.gates.add(stategate);
                Gate outputgate = new Gate("Coil", equation.output.toString());
                rung.gates.add(outputgate);
                rung.outputvar = equation.output;
                rungs.add(rung);
            }
            System.out.println(equation);
            System.out.println(rungs);
        }

    }

    private class Rung {
        O outputvar;
        ArrayList<Gate> gates = new ArrayList<Gate>();
        @Override
        public String toString(){
            String start = "|---";
            for (Gate gate : gates){
                String g = gate.toString() + "---";
                start = start + g;
            }
            start = start + "|";
            return start;
        }

    }

    private class Gate {
        String Inputvar;
        String Type;

        public Gate(String type, String inputvar) {
            this.Type = type;
            this.Inputvar = inputvar;
        }
        @Override
        public String toString(){
            return  Type + ":"+ Inputvar;
        }
    }
}

//Joms |--|1|---|/2|---|state?|---(? ?)---| or
//Den her opgave er alt for meget string formatting lol i mean tru lol
