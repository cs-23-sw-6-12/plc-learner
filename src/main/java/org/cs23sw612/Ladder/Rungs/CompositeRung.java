package org.cs23sw612.Ladder.Rungs;

public class CompositeRung implements Rung {
    /*
     * TODO(DONE): When either is null, it is trivially true by the label. For
     * example if right == null: `(label AND right) OR (!label AND left)`, it can be
     * reduced to `(label) OR (left)` For example if left == null: `(label AND
     * right) OR (!label AND left)`, it can be reduced to `(!label) OR (right)`
     */
    public final String label;
    public Rung left, right;
    private Boolean _printRight, _printLeft;
    public CompositeRung(String label, Rung left, Rung right) {
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
    public int rungHeight() {
        return (left == null ? 0 : left.rungHeight()) + (right == null ? 0 : right.rungHeight());
    }

    @Override
    public int rungWidth() {
        return Math.max(left == null ? 0 : (printLeftLabel() ? 1 : 0) + left.rungWidth(),
                right == null ? 0 : (printRightLabel() ? 1 : 0) + right.rungWidth());
    }

    // TODO: Can check for equivalence (subset) instead of empty to potentially reduce even
    // more
    public boolean printRightLabel() {
        if (_printRight != null)
            return _printRight;
        _printRight = left != null && left.rungWidth() > 0;
        return _printRight;
    }
    public boolean printLeftLabel() {
        if (_printLeft != null)
            return _printLeft;
        _printLeft = right != null && right.rungWidth() > 0;
        return _printLeft;
    }
}
