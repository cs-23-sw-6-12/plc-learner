package org.cs23sw612.Adapters.Output;

import org.cs23sw612.Adapters.OutputAdapter;

public class BooleanArrayOutputAdapter implements OutputAdapter<Boolean[]> {
  @Override
  public Boolean[] fromBytes(byte[] bytes) {
    Boolean[] output = new Boolean[bytes.length];

    for (int i = 0; i < bytes.length; i++) {
      output[i] = bytes[i] == 1;
    }

    return output;
  }
}
