package org.cs23sw612.Adapters.Input;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.InputAdapter;

public class IntegerWordInputAdapter implements InputAdapter<Word<Integer>> {
    @Override
    public Boolean[] getBits(Word<Integer> word) {
        return word.stream().map(i -> (Boolean) (i != 0)).toList().toArray(new Boolean[]{});

    }
}
