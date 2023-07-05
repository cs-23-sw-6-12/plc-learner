package org.cs23sw612.Ladder.Rungs;

public class CompositeRung implements NewRung {
    public final String label;
    public final NewRung left, right;

    public CompositeRung(String label, NewRung left, NewRung right) {
        this.label = label;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + String.format("|/%s|", label) + left.toString() + ") OR (" + String.format("|%s|", label) + right.toString() + ")";
    }
}
