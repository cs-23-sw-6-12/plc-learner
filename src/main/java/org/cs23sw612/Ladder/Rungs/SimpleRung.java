package org.cs23sw612.Ladder.Rungs;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SimpleRung implements Rung {
    public final String label;
    public final boolean open;
    public List<Rung> followingRungs;

    public SimpleRung(String label, boolean open, Rung... followingRungs) {
        this.label = label;
        this.open = open;
        this.followingRungs = Arrays.stream(followingRungs).filter(Objects::nonNull).toList();
    }

    @Override
    public String toString() {
        return String.format("|%s%s|", open ? " " : "/", label) + "("
                + String.join(" OR ", followingRungs.stream().map(Object::toString).toList()) + ")";
    }

    @Override
    public int rungHeight() {
        return followingRungs.size() + followingRungs.stream().mapToInt(Rung::rungHeight).sum();
    }

    @Override
    public int rungWidth() {
        return 1 + followingRungs.stream().mapToInt(Rung::rungWidth).max().orElse(0);
    }
}
