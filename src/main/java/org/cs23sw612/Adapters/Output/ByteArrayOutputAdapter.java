package org.cs23sw612.Adapters.Output;

import org.cs23sw612.Adapters.OutputAdapter;

public class ByteArrayOutputAdapter implements OutputAdapter<byte[]> {
  @Override
  public byte[] fromBytes(byte[] bytes) {
    return bytes;
  }
}
