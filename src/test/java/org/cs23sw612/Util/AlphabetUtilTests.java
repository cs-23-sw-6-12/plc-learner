package org.cs23sw612.Util;

import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AlphabetUtilTests {
    @Test
    public void testCreatedAlphabets() {
        assert AlphabetUtil.createAlphabet(-1).isEmpty();
        assert AlphabetUtil.createAlphabet(-1000).isEmpty();
        assert Alphabets.fromList(new ArrayList<Word<Boolean>>()).isEmpty();
        assert AlphabetUtil.createAlphabet(0).isEmpty();
        assert Alphabets.fromList(new ArrayList<Word<Boolean>>()).containsAll(AlphabetUtil.createAlphabet(0));
        assert AlphabetUtil.createAlphabet(1)
                .containsAll(Alphabets.fromArray(Word.fromSymbols(true), Word.fromSymbols(false)));
        assert AlphabetUtil.createAlphabet(2).containsAll(Alphabets.fromArray(Word.fromSymbols(true, false),
                Word.fromSymbols(true, true), Word.fromSymbols(false, true), Word.fromSymbols(false, false)));
    }
}
