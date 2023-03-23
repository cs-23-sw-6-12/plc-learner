package org.cs23sw612.Ladder;

import java.util.ArrayList;
import java.util.List;
import net.automatalib.commons.util.Pair;
import org.checkerframework.checker.nullness.qual.NonNull;

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
public class Equation<S, I, O> {
    public final O output;
    private List<Pair<I, S>> or;

    public Equation(@NonNull O output) {
        this.output = output;
    }

    /**
     * @param o
     *            A {@link EquationTable.EquationRow} to be augmented to an
     *            {@link Equation}
     */
    public Equation(EquationTable.EquationRow<S, I, O> o) {
        assert o.out() != null;
        this.output = o.out();
        or = new ArrayList<>();
        or.add(Pair.of(o.ins(), o.states()));
    }

    /**
     * Method used to extend how the output can be produced. Can be viewed as a pair
     * ({@code in,s}) that (also) produces {@code out}.
     *
     * @param in
     *            The input to extend {@code out} with
     * @param s
     *            The state to extend {@code out} with
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
        return o instanceof EquationTable.EquationRow && equals((EquationTable.EquationRow<S, I, O>) o);
    }

    private boolean equals(EquationTable.EquationRow<S, I, O> o) {
        return o.out() == output;
    }
}
