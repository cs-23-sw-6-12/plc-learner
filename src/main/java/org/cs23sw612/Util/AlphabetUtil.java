package org.cs23sw612.Util;

import com.google.common.collect.Lists;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;

import java.util.*;
import java.util.stream.Collectors;

public class AlphabetUtil {
    public static Alphabet<Word<Boolean>> createAlphabet(int num) {
        if (num <= 0)
            return Alphabets.fromList(Collections.emptyList());
        ArrayList<Word<Boolean>> arr = new ArrayList<>((int) Math.pow(num, 2));
        int decr = (1 << num) - 1;
        assert Integer.bitCount(decr) == num;
        for (; decr >= 0; decr--)
            arr.add(Word.fromArray(booleanArrayFromInt(decr, num), 0, num));
        return Alphabets.fromCollection(arr);
    }
    private static Boolean[] booleanArrayFromInt(int x, int size) {
        Boolean[] bs = new Boolean[size];
        for (int i = 0; i < size; i++)
            bs[i] = (x & (1 << i)) > 0;
        return bs;
    }

    public static Alphabet<Word<Integer>> createIntAlphabet(int num) {
        if (num <= 0)
            return Alphabets.fromList(Collections.emptyList());
        ArrayList<Word<Integer>> arr = new ArrayList<>((int) Math.pow(num, 2));
        int decr = (1 << num) - 1;
        assert Integer.bitCount(decr) == num;
        for (; decr >= 0; decr--)
            arr.add(Word.fromList(Arrays.stream(booleanArrayFromInt(decr, num)).map(bool -> bool ? 1 : 0)
                    .collect(Collectors.toList())));
        return Alphabets.fromCollection(arr);
    }

    public static String toBinaryString(Object b) {
        if (b instanceof Boolean)
            return (Boolean) b ? "1" : "0";
        else if (b instanceof Number)
            return ((Number) b).longValue() != 0 ? "1" : "0";
        else if (b instanceof Word<?>)
            return ((Word<?>) b).stream().map(AlphabetUtil::toBinaryString).collect(Collectors.joining());
        else
            throw new TypeNotPresentException("lol", new Throwable());
    }

    public static <I extends Word<Boolean>> ArrayList<List<I>> permutateWord(ArrayList<List<I>> old,
            Alphabet<I> alphabet) {
        ArrayList<List<I>> news = new ArrayList<>(Collections.emptyList());
        for (var o : old)
            for (var w : alphabet) {
                var n = new ArrayList<>(o.stream().toList());
                n.addAll(Lists.newArrayList(List.of(w)));
                news.add(n);
            }
        return news;// .addAll(news);
    }
}
