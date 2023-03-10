package org.cs_23_sw_6_12.Adapters;

public interface OutputAdapter<O> {
    O fromBytes(byte[] bytes);
}
