package org.cs23sw612.Util;

import com.google.common.collect.Lists;
import de.learnlib.datastructure.observationtable.ObservationTable;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MachineEquivalence {
    public static <I, O> boolean compactMealyEq(CompactMealy<I, O> a, CompactMealy<I, O> b, Alphabet<I> alphabet) {
        if (!a.getStates().stream().dropWhile(s -> b.getStates().contains(s)).toList().isEmpty()
                || !b.getStates().stream().dropWhile(s -> a.getStates().contains(s)).toList().isEmpty())
            return false;

        for (var s1 : a) {
            var s2 = b.getState(b.getStateId(s1));
            if (s2 == null)
                return false;

            for (var w : alphabet) {
                var trans1 = a.getTransitions(s1, w);
                var trans2 = b.getTransitions(s2, w);
                var temp = trans1.containsAll(trans2) && trans2.containsAll(trans1);
                if (!temp)
                    return false;

                if (a.getTransition(s1, w).getOutput() == b.getTransition(s2, w).getOutput())
                    return false;
            }
        }

        return true;
    }

    public static <I extends Word<Boolean>, O> boolean compactMealyEqReduced(CompactMealy<I, O> a, CompactMealy<I, O> b,
            Alphabet<I> alphabet) {
        int biggest = Math.max(a.getStates().size(), b.getStates().size());

        ArrayList<List<I>> words = Lists
                .newArrayList(alphabet.stream().map(w -> Lists.newArrayList(Collections.singleton(w))).toList());
        for (int i = 0; i <= biggest + 3; i++) {
            for (var w : words) {
                if (!a.computeOutput(w).equals(b.computeOutput(w))) {
                    System.out.println("Word: " + w);
                    System.out.println("a: " + a.computeOutput(w));
                    System.out.println("b: " + b.computeOutput(w));
                    return false;
                }
            }
            words = AlphabetUtil.permutateWord(words, alphabet);
        }
        // System.out.println(words);

        return true;
    }
    public static <I extends Word<Boolean>, O> boolean compactMealyEqReduced(CompactMealy<I, O> m,
            ObservationTable<I, Object> tab, Alphabet<I> alphabet) {
        int width = tab.numberOfSuffixes();
        for (var row : tab.getAllRows()) {
            for (int i = 0; i < width; i++) {
                var suffix = tab.getSuffix(i);
                var word = row.getLabel().concat(suffix);
                if (!m.computeSuffixOutput(row.getLabel(), suffix).equals(tab.cellContents(row, i))) {
                    System.out.println(word);
                    System.out.println(m.computeOutput(word));
                    System.out.println(tab.cellContents(row, i));
                    return false;
                }
            }
        }
        return true;
    }
}
