package org.cs23sw612.Util;

import net.automatalib.commons.util.Pair;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Bit implements Serializable, Comparable<Boolean> {
    public final boolean value;

    public static Bit HIGH() {
        return new Bit(true);
    }
    public static Bit LOW() {
        return new Bit(false);
    }

    public Bit(boolean val) {
        value = val;
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public int compareTo(Boolean abool) {
        return abool == value ? 0 : (value ? 1 : -1);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Bit) {
            return ((Bit) other).value == value;
        } else if (other instanceof Boolean) {
            return (Boolean) other == value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value ? 1 : 0;
    }

    public static Alphabet<Word<Bit>> createAlphabet(int num) {
        if (num <= 0)
            return Alphabets.fromList(Collections.emptyList());
        ArrayList<Word<Bit>> arr = new ArrayList<>((int) Math.pow(num, 2));
        int decr = (1 << num) - 1;
        assert Integer.bitCount(decr) == num;
        for (; decr >= 0; decr--)
            arr.add(Word.fromList(byteFromInt(decr, num)));
        return Alphabets.fromCollection(arr);
    }

    public static List<Bit> byteFromInt(long x, int size) {
        ArrayList<Bit> bs = new ArrayList<>();
        for (int i = 0; i < size; i++)
            bs.add(i, new Bit((x & (1 << i)) > 0));
        return bs;
    }

    public static Pair<@Nullable Word<Bit>, @Nullable Word<Bit>> parseBit(Map<String, String> attr) {
        String label = attr.get("label");
        if (label == null) {
            return Pair.of(null, null);
        } else {
            String[] tokens = label.split("/");
            return tokens.length != 2 ? Pair.of(null, null) : Pair.of(getWord(tokens[0]), getWord(tokens[1]));
        }
    }
    private static Word<Bit> getWord(String token) {
        return Word.fromList(Arrays.stream(token.trim().split(" "))
                .map(s -> Bit.fromBool(s.equals("true") || s.equals("1"))).collect(Collectors.toList()));
    }

    public static <T extends Comparable<Boolean>> Bit fromBool(T aBoolean) {
        return new Bit(aBoolean.compareTo(true) == 0);
    }
}