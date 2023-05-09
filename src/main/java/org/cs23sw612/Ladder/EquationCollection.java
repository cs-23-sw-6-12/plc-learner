package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.TransitionOutputAutomaton;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.*;

/**
 * A collection of {@link Equation}s. From a given {@link MealyMachine}, it
 * creates all the equations produced from it.
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
 *            Machine. Can only be a {@link TransitionOutputAutomaton}
 * @param <A>
 *            Alphabet over {@code I}
 */
public class EquationCollection<S extends Number, I extends Word<?>, T extends CompactMealyTransition<? super O>, O extends Word<?>, M extends TransitionOutputAutomaton<S, I, T, ? super O>, A extends Alphabet<I>>
        implements
            Collection<Equation<Word<Boolean>, I, O>> {
    private final List<Equation<Word<Boolean>, I, O>> equations = new ArrayList<>();
    private final TruthTable<S, I, T, O, M, A> table;

    /**
     * @param machine
     *            The machine to create the equation table over
     * @param alphabet
     *            The given input-alphabet
     */
    public EquationCollection(M machine, A alphabet) {
        this(new TruthTable<>(machine, alphabet));
    }

    private EquationCollection(TruthTable<S, I, T, O, M, A> tab) {
        table = tab;
        extractEquations(table.getEquations());
    }
    /*
     * public EquationCollection(Collection<EquationTable.EquationRow<S, I, O>> eqs)
     * { this.extractEquations(eqs); }
     */

    private void extractEquations(List<TruthTable.TruthRow<Word<Boolean>, I, O>> eqs) {
        eqs.forEach(eq -> {
            List<Equation<Word<Boolean>, I, O>> index = equations.stream().filter(e -> e.output.equals(eq.output()))
                    .toList();
            assert index.size() <= 1;
            if (index.isEmpty())
                equations.add(new Equation<>(eq));
            else
                index.get(0).extend(eq.state(), eq.nextState(), eq.input());
        });
    }

    TruthTable<S, I, T, O, M, A> getTable() {
        return table;
    }

    public String getTabularLatex() {
        return table.toLatexTabularString();
    }

    // region overrides
    @Override
    public String toString() {
        return String.join("\n", equations.stream().map(Object::toString).toList());
    }

    @Override
    public int size() {
        return equations.size();
    }

    @Override
    public boolean isEmpty() {
        return equations.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return equations.contains(o);
    }

    @Override
    public Iterator<Equation<Word<Boolean>, I, O>> iterator() {
        return equations.iterator();
    }

    @Override
    public Object[] toArray() {
        return equations.toArray();
    }

    @Override
    public <TT> TT[] toArray(TT[] ts) {
        return equations.toArray(ts);
    }

    @Override
    public boolean add(Equation<Word<Boolean>, I, O> equation) {
        return equations.add(equation);
    }

    @Override
    public boolean remove(Object o) {
        return equations.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return new HashSet<>(equations).containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Equation<Word<Boolean>, I, O>> collection) {
        return equations.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return equations.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return equations.retainAll(collection);
    }

    @Override
    public void clear() {
        equations.clear();
    }
    // endregion
}
