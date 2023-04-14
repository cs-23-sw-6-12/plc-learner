package org.cs23sw612.BAjER;

import java.io.IOException;

public interface IBAjERClient {
    Boolean[] Step(Boolean[] bits) throws IOException, BAjERException;
    void Setup(byte inBits, byte outBits) throws IOException, BAjERException;
    void Reset() throws IOException, BAjERException;
}
