package org.cs23sw612.Adapters.Input;

import net.automatalib.words.Word;
import org.cs23sw612.Adapters.InputAdapter;

public class BooleanWordInputAdapter implements InputAdapter<Word<Boolean>> {
  @Override
  public byte[] toBytes(Word<Boolean> object) {
    byte[] word = new byte[object.length()];
    java.util.List<Boolean> chars = object.asList();
    for (int i = 0; i < object.length(); i++) {
      word[i] = (byte) (chars.get(i) ? 1 : 0);
    }
    return word;
  }
}
