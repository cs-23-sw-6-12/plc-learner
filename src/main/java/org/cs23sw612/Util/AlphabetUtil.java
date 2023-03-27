package org.cs23sw612.Util;

import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;

import java.util.ArrayList;
import java.util.Collections;

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
}
