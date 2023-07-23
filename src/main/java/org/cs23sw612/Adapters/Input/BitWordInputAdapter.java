package org.cs23sw612.Adapters.Input;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.InputAdapter;
import org.cs23sw612.Util.Bit;

public class BitWordInputAdapter implements InputAdapter<Word<Bit>> {
    @Override
    public Boolean[] getBits(Word<Bit> word) {
        return word.stream().map(a -> a.value).toList().toArray(new Boolean[]{});
    }
}
