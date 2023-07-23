package org.cs23sw612.Adapters.Output;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.OutputAdapter;
import org.cs23sw612.Util.Bit;

import java.util.Arrays;

public class BitWordOutputAdapter implements OutputAdapter<Word<Bit>> {
    @Override
    public Word<Bit> fromBits(Boolean[] bits) {
        return Word.fromList(Arrays.stream(bits).map(Bit::new).toList());
    }
}