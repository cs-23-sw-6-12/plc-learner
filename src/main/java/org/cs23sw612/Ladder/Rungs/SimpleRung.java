package org.cs23sw612.Ladder.Rungs;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SimpleRung implements NewRung {
    public final String label;
    public final boolean open;
    public List<NewRung> followingRungs;

    public SimpleRung(String label, boolean open, NewRung... followingRungs) {
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
        return followingRungs.size() + followingRungs.stream().mapToInt(NewRung::rungHeight).sum();
    }

    @Override
    public int rungWidth() {
        return 1 + followingRungs.stream().mapToInt(NewRung::rungWidth).max().orElse(0);
    }
}
