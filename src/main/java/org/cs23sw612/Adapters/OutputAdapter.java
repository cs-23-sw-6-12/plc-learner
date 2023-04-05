package org.cs23sw612.Adapters;

public interface OutputAdapter<O> {
    O fromBits(Boolean[] bits);
}
