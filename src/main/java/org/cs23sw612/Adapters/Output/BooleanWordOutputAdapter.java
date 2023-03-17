package org.cs23sw612.Adapters.Output;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.OutputAdapter;

public class BooleanWordOutputAdapter implements OutputAdapter<Word<Boolean>> {
    @Override
    public Word<Boolean> fromBytes(byte[] bytes) {
        Boolean[] output = new Boolean[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            output[i] = bytes[i] == 1;
        }

        return Word.fromSymbols(output);
    }
}
