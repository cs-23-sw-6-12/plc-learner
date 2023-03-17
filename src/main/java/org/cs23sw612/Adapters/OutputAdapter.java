package org.cs23sw612.Adapters;

public interface OutputAdapter<O> {
    O fromBytes(byte[] bytes);
}
