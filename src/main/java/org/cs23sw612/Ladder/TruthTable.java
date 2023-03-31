package org.cs23sw612.Ladder;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.automatalib.automata.concepts.StateIDs;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.cs23sw612.Util.AlphabetUtil;

// TODO: Transitions for outputs is defined to only be of this type. Maybe be better, lol
// TODO: Remove next_states

/**
 * A table over the "equations" from a given machine. Generally, it represents a
 * collection of all the transitions (input, state, output, next state).
 *
 * @param <S>
 *            States
 * @param <I>
 *            Input
 * @param <T>
 *            Transitions. Can only be of input/output types, which (currently)
 *            is only {@link CompactMealyTransition}
 * @param <O>
 *            Output
 * @param <M>
 *            Machine. Can only be a {@link MealyMachine}
 * @param <A>
 *            Alphabet over {@code I}
 */
class TruthTable<S extends Number, I extends Word<?>, T extends CompactMealyTransition<? super O>, O extends Word<?>, M extends TransitionOutputAutomaton<S, I, T, ? super O>, A extends Alphabet<I>> {
    private final int inputCount;
    private final List<O> outputs = new ArrayList<>();
    private final List<TruthRow<S, I, O>> rows = new ArrayList<>();
    private final StateIDs<S> stateIds;
    private final int varCount;
    private List<TruthRow<Word<Boolean>, I, O>> equations = null;

    /**
     * @param machine
     *            The machine to create the equaton table over
     * @param alphabet
     *            The given input-alphabet
     */
    TruthTable(M machine, A alphabet) {
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
        this.stateIds = machine.stateIDs();

        int stateCount = 0;
        for (S state : machine.getStates()) {
            alphabet.forEach(word -> {
                T trans = machine.getTransition(state, word);
                assert trans != null;
                O output = (O) trans.getOutput();
                if (!this.outputs.contains(output))
                    this.outputs.add(output);

                rows.add(new TruthRow<>(word, state, stateIds.getState(trans.getSuccId()), output));
            });
            stateCount += 1;
        }
        varCount = (int) Math.max(Math.ceil(Math.log(stateCount) / Math.log(2)), 1); // Log base 2
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

    /**
     * @return The raw {@link TruthTable}
     */
    List<TruthRow<Word<Boolean>, I, O>> getEquations() {
        if (equations == null)
            equations = rows.stream().map(e -> new TruthRow<>(e.input, convertState(e.state.longValue()),
                    convertState(e.nextState.longValue()), e.output)).toList();
        return equations;
    }

    private Word<Boolean> convertState(long state) {
        Boolean[] arr = Collections.nCopies(varCount, false).toArray(new Boolean[varCount]);
        for (int i = Math.toIntExact(varCount - 1); state != 0; state >>= 1, i--)
            arr[i] = (state & 1) == 1;

        return Word.fromArray(arr, 0, Math.toIntExact(varCount));
    }

    @Override
    public String toString() {
        return asString("|", "||");
    }

    public String toLatexTabularXString() {
        Supplier<String> latexHeader = () -> new String(new char[inputCount]).replace("\0", "|X") + "|" + // Input
                new String(new char[varCount]).replace("\0", "|X") + "|" + // Vars/state
                new String(new char[varCount]).replace("\0", "|X") + "||" + // Updated vars/nextstates
                new String(new char[outputs.get(0).length()]).replace("\0", "|X") + "|"; // Output

        String lineSep = "\\\\\\hline\n";

        return String.format("\\begin{tabularx}{\\linewidth}{%s}\\hline\n%s%s\\end{tabularx}", latexHeader.get(),
                asString("&", "&", lineSep, lineSep), lineSep);
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
                + String.join(" " + sep + " ", IntStream.range(0, outputs.get(0).length()).boxed()
                        .map(i -> String.format("$O_%d$", i)).toList())
                // .collect(Collectors.joining())
                // + "$O$"
                + headerSep + String.join(lineSep,
                        rows.stream().map(row -> row.asString(sep, catSep, AlphabetUtil::toBinaryString)).toList());
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
     * @param <S>
     *            States
     * @param <I>
     *            Input
     * @param <O>
     *            Output
     */
    record TruthRow<S, I extends Word<?>, O extends Word<?>>(@NonNull I input, @NonNull S state, @NonNull S nextState,
            @NonNull O output) {
        @Override
        public String toString() {
            return String.format("%s | %s | %s || %s", input, state, nextState, output);
        }

        String asString(String sep, String catSep, Function<Object, String> conv) {
            return String.join("",
                    new String[]{
                            input.stream().map(conv).map(s -> String.format("%s %s ", s, catSep))
                                    .collect(Collectors.joining()),
                            conv.apply(state), " " + catSep + " ", conv.apply(nextState), " " + catSep + " ",
                            String.join(" " + catSep + " ",
                                    output.stream().map(conv).map(s -> String.format("%s", s)).toList())});
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Equation && equals((Equation) o);
        }

        private boolean equals(Equation eq) {
            return output == eq.output;
        }
    }
}
