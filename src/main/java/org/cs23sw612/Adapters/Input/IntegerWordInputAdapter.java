package org.cs23sw612.Adapters.Input;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.InputAdapter;

public class IntegerWordInputAdapter implements InputAdapter<Word<Integer>> {
    @Override
    public byte[] toBytes(Word<Integer> object) {
        byte[] word = new byte[object.length()];
        java.util.List<Integer> chars = object.asList();
        for (int i = 0; i < object.length(); i++) {
            word[i] = chars.get(i).byteValue();
        }
        return word;
    }
}
