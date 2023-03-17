package org.cs23sw612.Adapters.Input;

import org.cs23sw612.Adapters.InputAdapter;

public class StringInputAdapter implements InputAdapter<String> {
    @Override
    public byte[] toBytes(String object) {
        return new byte[0];
    }
}
