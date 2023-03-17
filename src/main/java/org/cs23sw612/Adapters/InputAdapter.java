package org.cs23sw612.Adapters;

public interface InputAdapter<I> {
	byte[] toBytes(I object);
}
