package org.cs23sw612.Ladder;

import com.google.common.collect.Lists;
import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.*;
import java.util.stream.IntStream;

public class Ladder {
    public final ArrayList<Rung> outRungs, stateRungs, stateUpd;

    public <S extends Number, IO extends Word<Boolean>, T extends CompactMealyTransition<? super IO>> Ladder(
            EquationCollection<S, IO, T, IO, ? extends TransitionOutputAutomaton<S, IO, T, ? super IO>, Alphabet<IO>> ec) {
        SnoopDoggContainer snoop = new SnoopDoggContainer();

        for (Equation<Word<Boolean>, IO, IO> equation : ec)
            snoop.add(equation);

        outRungs = snoop.getOutRungs();
        stateRungs = snoop.getStateRungs();
        stateUpd = snoop.getUpdates();
    }

    private static class SnoopDoggContainer {
        private final HashMap<Integer, ArrayList<GateSequence>> outs, states;
        private ArrayList<Rung> outRungs = null, stateRungs = null;
        private final ArrayList<Rung> stateUpd = null;
        int stateCount = 0;

        public SnoopDoggContainer() {
            outs = new HashMap<>();
            states = new HashMap<>();
        }

        <IO extends Word<Boolean>> void add(Equation<Word<Boolean>, IO, IO> equation) {
            int[] outBits = IntStream.range(0, equation.output.size()).filter(equation.output::getSymbol).toArray();
            ArrayList<GateSequence> ins = new ArrayList<>(equation.getFullList().size());

            for (var e : equation.getFullList()) {
                stateCount = Math.max(stateCount, e.getFirst().size());
                GateSequence gs = new GateSequence(e.getThird(), e.getFirst());

                IntStream.range(0, e.getSecond().size()).filter(i -> e.getSecond().getSymbol(i)).forEach(i -> {
                    // TODO: Temp vars
                    if (states.containsKey(i))
                        states.get(i).add(gs);
                    else
                        states.put(i, Lists.newArrayList(gs));
                });
                ins.add(gs);
            }
            for (int i : outBits) {
                if (outs.containsKey(i))
                    outs.get(i).addAll(ins);
                else
                    outs.put(i, ins);
            }
        }

        ArrayList<Rung> getOutRungs() {
            if (outRungs == null)
                outRungs = new ArrayList<>(
                        outs.entrySet().stream().map(e -> new Rung(e.getKey(), e.getValue(), "O")).toList());
            return outRungs;
        }
        ArrayList<Rung> getStateRungs() {
            if (stateRungs == null)
                stateRungs = new ArrayList<>(
                        states.entrySet().stream().map(e -> new Rung(e.getKey(), e.getValue(), "S'")).toList());
            return stateRungs;
        }

        ArrayList<Rung> getUpdates() {
            if (stateUpd == null)
                stateRungs = new ArrayList<>(
                        IntStream.range(0, stateCount).mapToObj(i -> new Rung(i, "S'", "S")).toList());
            return stateRungs;
        }
    }

    public static class Rung {
        public final ArrayList<GateSequence> orRungs = new ArrayList<>();
        public final LinkedHashSet<Gate> outputGates = new LinkedHashSet<>();
        public GateSequence gates;

        Rung(Integer output, ArrayList<GateSequence> gates, String symbol) {
            outputGates.add(Gate.coil(String.format("%s[%d]", symbol, output)));
            this.gates = gates.get(0);
            orRungs.addAll(gates.subList(1, gates.size()));
        }
        Rung(Integer output, String symbol, String symbolOut) {
            this.outputGates.add(Gate.coil(String.format("%s[%d]", symbolOut, output)));
            this.gates = new GateSequence();
            var gate = new Gate(String.format("%s[%d]", symbol, output), true);
            gates.add(gate);
        }

        @Override
        public String toString() {
            return "\n|----" + gates + "----" + String.join("---", outputGates.stream().map(Gate::toString).toList())
                    + "--|\n" + String.join("\n",
                            orRungs.stream().map(GateSequence::toString).map(s -> "  ᒻ--" + s + "--ᒽ").toList());
        }
    }

    public static class GateSequence {
        private final ArrayList<Gate> gates;

        public GateSequence() {
            gates = new ArrayList<>();
        }

        public GateSequence(Word<Boolean> inputGates, Word<Boolean> stateGates) {
            this();
            int i = 0;
            for (Boolean b : inputGates) {
                this.gates.add(new Gate(String.format("I[%d]", i++), b));
            }
            i = 0;
            for (Boolean b : stateGates) {
                this.gates.add(new Gate(String.format("S[%d]", i++), b));
            }
        }

        public int count() {
            return gates.size();
        }
        public Gate get(int i) {
            return gates.get(i);
        }

        public void add(Gate g) {
            gates.add(g);
        }

        @Override
        public String toString() { // todo fix ift outputGates
            return String.join("---", gates.stream().map(Gate::toString).toList());
        }
    }

    public record Gate(String gate, boolean open) {
        static Gate coil(String symbol) {
            return new Gate(symbol, true);
        }

        public String getSymbol() {
            return gate;
        }

        @Override
        public String toString() {
            return String.format("|%s%s|", open ? " " : "/", gate);
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
}
