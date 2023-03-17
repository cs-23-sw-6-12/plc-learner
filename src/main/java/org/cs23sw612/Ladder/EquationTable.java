package org.cs23sw612.Ladder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.automatalib.automata.concepts.StateIDs;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import org.checkerframework.checker.nullness.qual.NonNull;

// TODO: Transitions for outputs is defined to only be of this type. Maybe be better, lol
// TODO: Remove next_states

// TODO: rename?

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
class EquationTable<S, I, T extends CompactMealyTransition<O>, O, M extends MealyMachine<S, I, T, O>, A extends Alphabet<I>> {
    // TODO: Remove names?
    private final List<String> inputs;
    private final List<String> outputs = new ArrayList<>();
    private final List<String> states;
    private final List<EquationRow<S, I, O>> equations = new ArrayList<>();
    private final StateIDs<S> stateIds;
    private static final Function<? super Boolean, String> boolToInt = i -> i ? "1" : "0";

    /**
     * @param machine
     *            The machine to create the equaton table over
     * @param alphabet
     *            The given input-alphabet
     */
    EquationTable(M machine, A alphabet) {
        this.stateIds = machine.stateIDs();
        this.states = machine.getStates().stream().map(S::toString).toList();
        this.inputs = alphabet.stream().map(I::toString).toList();

        machine.getStates().forEach(state -> alphabet.forEach(word -> {
            T trans = machine.getTransition(state, word);
            assert trans != null;
            O output = trans.getOutput();
            if (!this.outputs.contains(output.toString()))
                this.outputs.add(output.toString());

            equations.add(new EquationRow<>(word, state, stateIds.getState(trans.getSuccId()), trans.getOutput()));
        }));
    }

    /**
     * @return The raw {@link EquationTable}
     */
    Collection<EquationRow<S, I, O>> getRawEquations() {
        return equations;
    }

    @Override
    public String toString() {
        return asString("|", "||");
    }

    String toLatexTabularXString() {
        String sep = "\\\\\\hline\n";
        /*
         * return String.format(
         * "\\begin{tabularx}{\\linewidth}{%s}\\hline\n%s%s\\end{tabularx}",
         * latexTableHeader(), asString("&", "&", sep, "\\hline"), sep);
         */
        return String.format("\\begin{tabularx}{\\linewidth}{|X|X|X||X|}\\hline\n%s%s\\end{tabularx}",
                // latexTableHeader(),
                asString("&", "&", sep, "\\hline"), sep);
    }

    private String asString(String sep) {
        return asString(sep, sep, "\n");
    }

    private String asString(String sep, String catSep) {
        return asString(sep, catSep, "\n", "");
    }

    private String asString(String sep, String catSep, String lineSep) {
        return asString(sep, catSep, lineSep, "");
    }

    private String asString(String sep, String catSep, String lineSep, String headerSep) {
        String header = "INPUT & STATE & NEXT STATE & OUTPUT";
        String body = String.join(lineSep,
                equations.stream().map(row -> row.asString(sep, catSep, boolToInt)).toList());
        return header + lineSep + headerSep + body;
    }

    private String latexTableHeader() {
        return "|" + String.join("|", inputs.stream().map((i) -> "X").toList()) + "||"
                + String.join("|", states.stream().map((i) -> "X").toList()) + "||"
                + String.join("|", states.stream().map((i) -> "X").toList()) + "|||" +
                // String.join("|", outputs.stream().map((i) -> "X").toList()) + "|";
                "X|";
    }

    /**
     * @param ins
     *            The input for the equation
     * @param states
     *            The state the for the equation
     * @param nextStates
     *            The state following the input
     * @param out
     *            The ouput
     * @param <S>
     *            States
     * @param <I>
     *            Input
     * @param <O>
     *            Output
     */
    record EquationRow<S, I, O>(@NonNull I ins, @NonNull S states, @NonNull S nextStates, @NonNull O out) {
        @Override
        public String toString() {
            return String.format("%s | %s | %s || %s", ins, states, nextStates, out);
        }

        String asString(String sep) {
            return asString(sep, sep);
        }

        String asString(String sep, String catSep) {
            return asString(sep, catSep, Object::toString);
        }

        String asString(String sep, String catSep, Function<? super Boolean, String> conv) {
            return String.join(" " + catSep + " ",
                    new String[]{ins.toString(), states.toString(), nextStates.toString(), out.toString()});
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Equation && equals((Equation<S, I, O>) o);
        }

        private boolean equals(Equation<S, I, O> eq) {
            return out == eq.output;
        }
    }
}
