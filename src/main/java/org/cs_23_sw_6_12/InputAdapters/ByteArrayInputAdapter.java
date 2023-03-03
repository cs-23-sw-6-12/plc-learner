package org.cs_23_sw_6_12.InputAdapters;

import org.cs_23_sw_6_12.Interfaces.InputAdapter;

public class ByteArrayInputAdapter implements InputAdapter<byte[]> {
    @Override
    public byte[] toBytes(byte[] object) {
        return object;
    }
}
