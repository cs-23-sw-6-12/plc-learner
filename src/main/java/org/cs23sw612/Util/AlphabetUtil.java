package org.cs23sw612.Util;

import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public static String toBinaryString(Object obj) {
        if (obj instanceof Boolean)
            return (Boolean) obj ? "1" : "0";
        else if (obj instanceof Number)
            return ((Number) obj).longValue() != 0 ? "1" : "0";
        else if (obj instanceof Word<?>)
            return ((Word<?>) obj).stream().map(AlphabetUtil::toBinaryString).collect(Collectors.joining());
        else
            throw new TypeNotPresentException("Not accepted type", new Throwable());
    }
}
