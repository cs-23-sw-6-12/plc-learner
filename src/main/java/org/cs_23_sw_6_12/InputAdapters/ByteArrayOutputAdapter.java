package org.cs_23_sw_6_12.InputAdapters;

import org.cs_23_sw_6_12.Interfaces.OutputAdapter;

public class ByteArrayOutputAdapter implements OutputAdapter<byte[]> {
    @Override
    public byte[] fromBytes(byte[] bytes) {
        return bytes;
    }
}
