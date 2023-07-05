package org.cs23sw612.Ladder.Rungs;

public record CompositeRung(String label, NewRung left, NewRung right) implements NewRung {

    @Override
    public String toString() {
        return "(" + String.format("|/%s|", label) + left.toString() + ") OR (" + String.format("|%s|", label) + right.toString() + ")";
    }
}
