package org.cs23sw612.Adapters.Output;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.OutputAdapter;

public class IntegerWordOutputAdapter implements OutputAdapter<Word<Integer>> {
    @Override
    public Word<Integer> fromBytes(byte[] bytes) {
        Integer[] output = new Integer[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            output[i] = (int) bytes[i];
        }

        return Word.fromSymbols(output);
    }
}
