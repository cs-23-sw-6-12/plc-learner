package org.cs23sw612.Util;

import java.util.Optional;

public class OneOF<L, R> {
    enum Which {Left, Right}
    Which which = null;
    L left = null;
    R right = null;

    public OneOF() { }
    public static <L, R> OneOF<L, R> fromLeft(L val) {
        OneOF<L, R> out = new OneOF<>();
        out.setLeft(val);
        return out;
    }
    public static <L, R> OneOF<L, R> fromRight(R val) {
        OneOF<L, R> out = new OneOF<>();
        out.setRight(val);
        return out;
    }
    public Optional<L> getLeft() {
        return switch (which) {
            case Left -> Optional.of(left);
            case Right -> Optional.empty();
        };
    }
    public Optional<R> getRight() {
        return switch (which) {
            case Left -> Optional.empty();
            case Right -> Optional.of(right);
        };
    }
    public void setLeft(L left) {
        which = Which.Left;
        this.left = left;
    }
    public void setRight(R right) {
        which = Which.Right;
        this.right = right;
    }

    public boolean isLeft() { return which == Which.Left; }
    public boolean isRight() { return which == Which.Right; }
}


