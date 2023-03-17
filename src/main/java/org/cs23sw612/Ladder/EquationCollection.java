package org.cs23sw612.Ladder;

import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.words.Alphabet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of {@link Equation}s.
 * From a given {@link MealyMachine}, it creates all the equations produced from it.
 * @param <S> States
 * @param <I> Input
 * @param <T> Transitions. Can only be of input/output types, which (currently) is only {@link CompactMealyTransition}
 * @param <O> Output
 * @param <M> Machine. Can only be a {@link MealyMachine}
 * @param <A> Alphabet over {@code I}
 */
public class EquationCollection<S, I, T extends CompactMealyTransition<O>, O, M extends MealyMachine<S, I, T, O>, A extends Alphabet<I>> {
    private final List<Equation<S, I, O>> equations = new ArrayList<>();
    private final EquationTable<S, I, T, O, M, A> table;

    /**
     * @param machine The machine to create the equation table over
     * @param alphabet The given input-alphabet
     */
    public EquationCollection(M machine, A alphabet) {
        this(new EquationTable<>(machine, alphabet));
    }

    private EquationCollection(EquationTable<S, I, T, O, M, A> tab) {
        table = tab;
        extractEquations(table.getRawEquations());
    }
    /*public EquationCollection(Collection<EquationTable.EquationRow<S, I, O>> eqs) {
        this.extractEquations(eqs);
    }*/

    private void extractEquations(Collection<EquationTable.EquationRow<S, I, O>> eqs) {
        eqs.forEach(eq -> {
            int index = equations.indexOf(eq);
            if (index == -1) {
                equations.add(new Equation<>(eq));
            } else {
                equations.get(index).extend(eq.ins(), eq.states());
            }
        });
    }

    public EquationTable<S, I, T, O, M, A> getTable() {return table;}

    public Collection<Equation<S, I, O>> getEquations() {return equations;}

    @Override
    public String toString() {
        return String.join("\n", equations.stream().map(Object::toString).toList());
    }

    public String toLatexTabularXString() {
        return table.toLatexTabularXString();
    }

}

