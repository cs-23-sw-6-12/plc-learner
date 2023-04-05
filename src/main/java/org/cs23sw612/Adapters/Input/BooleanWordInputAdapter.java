package org.cs23sw612.Adapters.Input;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.InputAdapter;

public class BooleanWordInputAdapter implements InputAdapter<Word<Boolean>> {
    @Override
    public Boolean[] getBits(Word<Boolean> word) {
        return word.stream().toList().toArray(new Boolean[] {});
    }
}
