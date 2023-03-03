package org.cs_23_sw_6_12.InputAdapters;

import org.cs_23_sw_6_12.Interfaces.InputAdapter;

public class StringInputAdapter implements InputAdapter<String> {
    @Override
    public byte[] toBytes(String object) {
        return new byte[0];
    }
}
