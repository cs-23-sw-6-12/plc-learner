package org.cs23sw612.Ladder;

import java.util.ArrayList;
import java.util.List;
import net.automatalib.commons.util.Pair;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A representation of how an output can be produced. The given output can be
 * produced by a one of the list of "operations". This list can be viewed as a
 * OR operation ("x produces output OR y produces output OR ...").
 *
 * @param <S>
 *            States
 * @param <I>
 *            Input
 * @param <O>
 *            Output
 */
public class Equation<S, I, O> {
    public final O output;
    private List<Pair<I, S>> or;

    public Equation(@NonNull O output) {
        this.output = output;
    }

    /**
     * @param o
     *            A {@link TruthTable.TruthRow} to be augmented to an
     *            {@link Equation}
     */
    public Equation(TruthTable.TruthRow<S, I, O> o) {
        assert o.output() != null;
        this.output = o.output();
        or = new ArrayList<>();
        or.add(Pair.of(o.inputs(), o.states()));
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
    public void extend(I in, S s) {
        if (or == null)
            or = new ArrayList<>();
        or.add(Pair.of(in, s));
    }

    public List<Pair<I, S>> getFullList() {
        return or;
    }

    // TODO: Formatting shit?
    @Override
    public String toString() {
        return output.toString() + " = " + String.join(" + ",
                or.stream().map(p -> String.format("(%s %s)", p.getFirst(), p.getSecond())).toList());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TruthTable.TruthRow && equals((TruthTable.TruthRow<S, I, O>) o);
    }

    private boolean equals(TruthTable.TruthRow<S, I, O> o) {
        return o.output() == output;
    }
}
