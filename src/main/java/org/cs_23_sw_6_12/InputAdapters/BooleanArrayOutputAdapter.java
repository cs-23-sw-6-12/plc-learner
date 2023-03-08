package org.cs_23_sw_6_12.InputAdapters;

import org.cs_23_sw_6_12.Interfaces.OutputAdapter;

public class BooleanArrayOutputAdapter implements OutputAdapter<Boolean[]> {
    @Override
    public Boolean[] fromBytes(byte[] bytes) {
        Boolean[] output = new Boolean[bytes.length];

        for (int i = 0; i < bytes.length; i++){
            output[i] = bytes[i]==1;
        }

        return output;
    }
}
