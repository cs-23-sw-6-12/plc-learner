package org.cs_23_sw_6_12.Adapters.Input;

import org.cs_23_sw_6_12.Adapters.InputAdapter;

public class StringInputAdapter implements InputAdapter<String> {
    @Override
    public byte[] toBytes(String object) {
        return new byte[0];
    }
}
