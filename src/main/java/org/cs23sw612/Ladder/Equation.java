package org.cs23sw612.Ladder;

import net.automatalib.commons.util.Pair;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Equation<S, I, O> {
    public final O output;
    private List<Pair<I, S>> or;

    public Equation(@NonNull O output) {
        this.output = output;
    }
    public Equation(EquationTable.EquationRow<S, I, O> o) {
        assert o.out() != null;
        this.output = o.out();
        or = new ArrayList<>();
        or.add(Pair.of(o.ins(), o.states()));
    }


    public void extend(I in, S s) {
        if (or == null) or = new ArrayList<>();
        or.add(Pair.of(in, s));
    }

    //TODO: Question formatting (states=variables, states on lhs? etc.)
    @Override
    public String toString() {
        return output.toString() + " = " +
                String.join(" + ", or.stream().map(p ->
                        String.format("(%s %s)", p.getFirst(), p.getSecond()))
                        .toList());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EquationTable.EquationRow<?, ?, ?> &&
                equals((EquationTable.EquationRow<S, I, O>) o);
    }

    private boolean equals(EquationTable.EquationRow<S, I, O> o) {
        return o.out() == output;
    }
}