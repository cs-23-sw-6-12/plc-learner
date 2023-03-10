package org.cs_23_sw_6_12;

import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;
import net.automatalib.words.impl.Alphabets;

import java.util.ArrayList;

public class WordAlphabets {

    @SafeVarargs
    public static <I> Alphabet<Word<I>> wordAlphabet(int symbolSize, I ...chars){
        var words =  new ArrayList<I>();

        /*
        words.ensureCapacity(symbolSize * symbolSize * chars.length);
        for (int i = 0; i < symbolSize; i++) {
            var combinations = new ArrayList<I>();

            for (int j = 0; j < symbolSize; j++) {

                var chararcters = new ArrayList<I>();
                for (int k = 0; k < chars.length; k++) {
                    chararcters.add(i+j+k, chars[k]);

                }

            }
        }
        */
        return null;
    }
}
