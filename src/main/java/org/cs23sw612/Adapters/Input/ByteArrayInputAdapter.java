package org.cs23sw612.Adapters.Input;

import org.cs23sw612.Adapters.InputAdapter;

public class ByteArrayInputAdapter implements InputAdapter<byte[]> {
	@Override
	public byte[] toBytes(byte[] object) {
		return object;
	}
}
