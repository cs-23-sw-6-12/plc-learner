package org.cs_23_sw_6_12.Adapters.Output;

import org.cs_23_sw_6_12.Adapters.OutputAdapter;

public class ByteArrayOutputAdapter implements OutputAdapter<byte[]> {
    @Override
    public byte[] fromBytes(byte[] bytes) {
        return bytes;
    }
}
