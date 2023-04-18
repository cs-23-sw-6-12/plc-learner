package org.cs23sw612.Ladder;

import java.util.List;

import com.google.common.collect.Lists;
import net.automatalib.commons.util.Triple;
import net.automatalib.words.Word;
import org.cs23sw612.Util.AlphabetUtil;

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
    private final List<Triple<S, S, I>> or;

    /**
     * @param o
     *            A {@link TruthTable.TruthRow} to be augmented to an
     *            {@link Equation}
     */
    public Equation(TruthTable.TruthRow<S, I, O> o) {
        this.output = o.output();
        or = Lists.newArrayList(Triple.of(o.state(), o.nextState(), o.input()));
    }

    /**
     * Method used to extend how the output can be produced. Can be viewed as a
     * triple ({@code s, s', i}) that (also) produces {@code output}.
     *
     * @param in
     *            The input to extend {@code output} with
     * @param s
     *            The state to extend {@code output} with
     */
    public void extend(S s, S sn, I in) {
        or.add(Triple.of(s, sn, in));
    }

    public List<Triple<S, S, I>> getFullList() {
        return or;
    }

    @Override
    public String toString() {
        return AlphabetUtil.toBinaryString(output) + " = "
                + String.join(" + ", or.stream().map(this::formatEquation).toList());
    }

    private String formatEquation(Triple<S, S, I> eq) {
        return String.format("(%s, %s, %s)", AlphabetUtil.toBinaryString(eq.getFirst()),
                AlphabetUtil.toBinaryString(eq.getSecond()), AlphabetUtil.toBinaryString(eq.getThird()));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TruthTable.TruthRow && equals((TruthTable.TruthRow) o);
    }

    private boolean equals(TruthTable.TruthRow o) {
        return o.output() == output;
    }
}
