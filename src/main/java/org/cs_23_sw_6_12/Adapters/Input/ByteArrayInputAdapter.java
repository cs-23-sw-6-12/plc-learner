package org.cs_23_sw_6_12.Adapters.Input;

import org.cs_23_sw_6_12.Adapters.InputAdapter;

public class ByteArrayInputAdapter implements InputAdapter<byte[]> {
    @Override
    public byte[] toBytes(byte[] object) {
        return object;
    }
}
