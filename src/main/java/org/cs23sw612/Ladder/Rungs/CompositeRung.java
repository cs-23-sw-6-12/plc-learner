package org.cs23sw612.Ladder.Rungs;

public class CompositeRung implements NewRung {
    /*
     * TODO: When either is null, it is trivially true by the label. For example if
     * right == null: `(label AND right) OR (!label AND left)`, it can be reduced to
     * `(label) OR (left)` For example if right == null: `(label AND right) OR
     * (!label AND left)`, it can be reduced to `(!label) OR (right`
     */
    public final String label;
    public NewRung left, right;
    public CompositeRung(String label, NewRung left, NewRung right) {
        this.label = label;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + String.format("|/%s|", label) + (left != null ? left : "") + ") OR ("
                + String.format("|%s|", label) + (right != null ? right : "") + ")";
    }

    @Override
    public int verticalHeight() {
        return Math.max(left == null ? 0 : left.verticalHeight(), right == null ? 0 : right.verticalHeight());
    }

    @Override
    public int horizontalHeight() {
        return Math.max(left == null ? 0 : left.horizontalHeight(), right == null ? 0 : right.horizontalHeight());
    }
}
