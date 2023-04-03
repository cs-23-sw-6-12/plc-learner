package org.cs23sw612.Ladder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.automatalib.commons.util.Pair;
import net.automatalib.words.Word;

/**
 * A representation of how an output can be produced. The given output can be
 * produced by one of the elements from the list of boolean equations. This list
 * can be viewed as a OR operation ("x produces output OR y produces output OR
 * ...").
 *
 * @param <S>
 *            States
 * @param <I>
 *            Input
 * @param <O>
 *            Output
 */
public class Equation<S extends Word<Boolean>, I extends Word<?>, O extends Word<?>> {
    public final O output;
    private List<Pair<S, I>> or;


    /**
     * @param o
     *            A {@link TruthTable.TruthRow} to be augmented to an
     *            {@link Equation}
     */
    public Equation(TruthTable.TruthRow<S, I, O> o) {
        this.output = o.output();
        or = new ArrayList<>();
        or.add(Pair.of(o.state(), o.input()));
    }

    /**
     * Method used to extend how the output can be produced. Can be viewed as a pair
     * ({@code in,s}) that (also) produces {@code output}.
     *
     * @param in
     *            The input to extend {@code output} with
     * @param s
     *            The state to extend {@code output} with
     */
    public void extend(S s, I in) {
        if (or == null)
            or = new ArrayList<>();
        or.add(Pair.of(s, in));
    }

    public List<Pair<S, I>> getFullList() {
        return or;
    }

    // TODO: Formatting shit?
    @Override
    public String toString() {
        return output.toString() + " = "
                + String.join(" + ",
                        or.stream()
                                .map(p -> String.format("(%s %s)", p.getSecond(),
                                        p.getFirst().stream().map(i -> i ? "1" : "0").collect(Collectors.joining(""))))
                                .toList());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TruthTable.TruthRow && equals((TruthTable.TruthRow) o);
    }

    private boolean equals(TruthTable.TruthRow o) {
        return o.output() == output;
    }
}
