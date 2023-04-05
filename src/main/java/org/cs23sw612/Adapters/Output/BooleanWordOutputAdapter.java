package org.cs23sw612.Adapters.Output;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.OutputAdapter;

public class BooleanWordOutputAdapter implements OutputAdapter<Word<Boolean>> {
    @Override
    public Word<Boolean> fromBits(Boolean[] bits) {
        return Word.fromSymbols(bits);
    }
}
