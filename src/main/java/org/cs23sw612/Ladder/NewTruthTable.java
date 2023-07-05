package org.cs23sw612.Ladder;

import net.automatalib.automata.concepts.StateIDs;
import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.commons.util.Pair;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.cs23sw612.Ladder.BDD.BDDNode;
import org.cs23sw612.Ladder.BDD.SimpleBDDNode;
import org.cs23sw612.Util.Bit;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A truth table over the transitions in the mealy machine
 * @param <IO>
 *            Input/Output
 */
public class NewTruthTable<IO extends Word<? extends Comparable<Boolean>>> {
    private int inputCount;
    private final List<IO> outputs = new ArrayList<>();
    private final List<TruthRow<Word<Bit>>> rows = new ArrayList<>();
    private int varCount;
    private final Function<String, String> latexHeader = ph -> new String(new char[inputCount]).replace("\0", "|" + ph)
            + "|" + // Input
            new String(new char[varCount]).replace("\0", "|" + ph) + "|" + // Vars/state
            new String(new char[varCount]).replace("\0", "|" + ph) + "|" + // vars/nextstates
            "|" + new String(new char[outputs.get(0).length()]).replace("\0", "|" + ph) + "|"; // Output

    /**
     * @param machine
     *            The machine to create the truth table over
     * @param alphabet
     *            The given input-alphabet
     */
    public <S extends Number, T extends CompactMealyTransition<? super IO>, M extends TransitionOutputAutomaton<S, IO, T, ? super IO>, A extends Alphabet<IO>> NewTruthTable(
            M machine, A alphabet) {
        // region assert input alphabet
        {
            var min = alphabet.stream().min(Comparator.comparing(w -> w.stream().count()));
            var max = alphabet.stream().max(Comparator.comparing(w -> w.stream().count()));
            assert min.isPresent();
            assert max.isPresent();
            assert min.get().stream().count() == max.get().stream().count();
        }
        // endregion

        inputCount = (int) alphabet.getSymbol(0).stream().count();
        StateIDs<S> stateIds = machine.stateIDs();

        var states = machine.getStates();
        varCount = (int) Math.max(Math.ceil(Math.log(states.size()) / Math.log(2)), 1); // Log base 2
        for (S state : machine.getStates()) {
            for (var word : alphabet) {

                T trans = machine.getTransition(state, word);
                assert trans != null;
                IO output = (IO) trans.getOutput();
                if (!this.outputs.contains(output))
                    this.outputs.add(output);

                rows.add(new TruthRow<>(Word.fromList(word.stream().map(Bit::fromBool).toList()),
                        convertState(state.longValue()), convertState(stateIds.getState(trans.getSuccId()).longValue()),
                        Word.fromList(output.stream().map(Bit::fromBool).toList())));
            }
        }
        // region assert output alphabet
        {
            var min = outputs.stream().min(Comparator.comparing(w -> w.stream().count()));
            var max = outputs.stream().min(Comparator.comparing(w -> w.stream().count()));

            assert min.isPresent();
            assert max.isPresent();
            assert min.get().stream().count() == max.get().stream().count();
        }
        // endregion
    }

    private Word<Bit> convertState(long state) {
        return Word.fromList(Bit.byteFromInt(state, varCount));
    }

    public HashMap<String, BDDNode> encodeBDDs() {
        HashMap<String, BDDNode> map = new HashMap<>();
        var list = rows.stream().map(TruthRow::getHighOutputAndStates).filter(Optional::isPresent).map(Optional::get).toList();
        for (var pair : list) {
            for (String str : pair.getFirst()) {
                if (!map.containsKey(str))
                    map.put(str, new SimpleBDDNode());
                map.get(str).insert(pair.getSecond(), true);
            }
        }
        map.values().forEach(BDDNode::reduce);

        return map;
    }

    @Override
    public String toString() {
        return asString("|", "||");
    }

    public String toLatexTabularString() {
        String lineSep = "\\\\\\hline\n";
        String headSep = "\\\\\\hline\\hline\n";

        return String.format("\\begin{tabular}{%s}\\hline\n%s%s\\end{tabular}", latexHeader.apply("c"),
                asString("&", "&", lineSep, headSep), lineSep);
    }

    private String asString(String sep, String catSep) {
        return asString(sep, catSep, "\n", "");
    }

    private String asString(String sep, String catSep, String lineSep, String headerSep) {
        return IntStream.range(0, inputCount).boxed().map(i -> String.format("$I_%d$ %s ", i, sep))
                .collect(Collectors.joining())
                + IntStream.range(0, varCount).boxed().map(i -> String.format("$S_%d$ %s ", i, sep))
                        .collect(Collectors.joining())
                + IntStream.range(0, varCount).boxed().map(i -> String.format("$S'_%d$ %s ", i, sep))
                        .collect(Collectors.joining())
                + String.join(" " + sep + " ",
                        IntStream.range(0, outputs.get(0).length()).boxed().map(i -> String.format("$O_%d$", i))
                                .toList())
                + headerSep + String.join(lineSep, rows.stream().map(row -> row.asString(catSep)).toList());
    }

    /**
     * @param input
     *            The input for the equation
     * @param state
     *            The state the for the equation
     * @param nextState
     *            The state following the input
     * @param output
     *            The output
     * @param <T>
     *            Input/Output/States
     */
    record TruthRow<T extends Word<Bit>>(@NonNull T input, @NonNull T state, @NonNull T nextState, @NonNull T output) {
        @Override
        public String toString() {
            return String.format("%s | %s | %s || %s", input, state, nextState, output);
        }

        String asString(String sep) {
            return String.join("", new String[]{
                    input.stream().map(Object::toString).map(s -> String.format("%s %s ", s, sep))
                            .collect(Collectors.joining()),
                    state.toString(), " " + sep + " ", nextState.toString(), " " + sep + " ",
                    String.join(" " + sep + " ", output.stream().map(Object::toString).toList())});
        }

        private Optional<Pair<List<String>, List<Pair<String, Bit>>>> getHighOutputAndStates() {
            ArrayList<String> outs = new ArrayList<>(IntStream.range(0, output.length()).boxed()
                    .filter(i -> output.getSymbol(i).value).map(i -> String.format("O[%d]", i)).toList());
            outs.addAll(IntStream.range(0, nextState.length()).boxed().filter(i -> nextState.getSymbol(i).value)
                    .map(i -> String.format("S'[%d]", i)).toList());

            if (outs.isEmpty())
                return Optional.empty();
            else {
                //System.out.println(outs + " = " + this.asString(" & "));
                // System.out.println(this.encodeInputAndState());
                return Optional.of(Pair.of(outs, this.encodeInputAndState()));
            }
        }

        private List<Pair<String, Bit>> encodeInputAndState() {
            ArrayList<Pair<String, Bit>> out = new ArrayList<>(IntStream.range(0, input.length()).boxed()
                    .map(i -> Pair.of(String.format("I[%d]", i), input.getSymbol(i))).toList());
            out.addAll(IntStream.range(0, state.length()).boxed()
                    .map(i -> Pair.of(String.format("S[%d]", i), state.getSymbol(i))).toList());
            return out;
        }
    }
}
