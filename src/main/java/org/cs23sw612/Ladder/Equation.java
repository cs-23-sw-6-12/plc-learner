package org.cs23sw612.Ladder;

import java.util.List;

import com.google.common.collect.Lists;
import net.automatalib.commons.util.Pair;
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
    private final List<Pair<S, I>> or;

    /**
     * @param o
     *            A {@link TruthTable.TruthRow} to be augmented to an
     *            {@link Equation}
     */
    public Equation(TruthTable.TruthRow<S, I, O> o) {
        this.output = o.output();
        or = Lists.newArrayList(Pair.of(o.state(), o.input()));
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
        or.add(Pair.of(s, in));
    }

    public List<Pair<S, I>> getFullList() {
        return or;
    }

    @Override
    public String toString() {
        return AlphabetUtil.toBinaryString(output) + " = "
                + String.join(" + ", or.stream().map(this::formatPair).toList());
    }

    private String formatPair(Pair<S, I> p) {
        return String.format("(%s, %s)", AlphabetUtil.toBinaryString(p.getFirst()),
                AlphabetUtil.toBinaryString(p.getSecond()));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TruthTable.TruthRow && equals((TruthTable.TruthRow) o);
    }

    private boolean equals(TruthTable.TruthRow o) {
        return o.output() == output;
    }
}
