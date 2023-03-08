package org.cs_23_sw_6_12.InputAdapters;

import org.cs_23_sw_6_12.Interfaces.InputAdapter;

public class BooleanArrayInputAdapter implements InputAdapter<Boolean[]> {
    @Override
    public byte[] toBytes(Boolean[] object) {
        byte[] output = new byte[object.length];

        for (int i = 0; i < object.length; i++){
            output[i] = (byte) (object[i]? 1 : 0);
        }

        return output;
    }
}
