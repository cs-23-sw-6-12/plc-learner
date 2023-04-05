package org.cs23sw612.Adapters.Output;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.OutputAdapter;

import java.util.Arrays;

public class IntegerWordOutputAdapter implements OutputAdapter<Word<Integer>> {

    @Override
    public Word<Integer> fromBits(Boolean[] bits) {
        return Word.fromSymbols(Arrays.stream(bits).map(b -> b ? 1 : 0).toList().toArray(new Integer[] {}));
    }
}
