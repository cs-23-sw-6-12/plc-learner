package org.cs23sw612.Ladder.BDD;

import net.automatalib.commons.util.Pair;
import org.cs23sw612.Ladder.Rungs.NewRung;
import org.cs23sw612.Util.Bit;

import java.util.List;

public abstract class BDDNode {

    public BDDNode reduce() {
        return this;
    }

    public abstract void insert(List<Pair<String, Bit>> vars, boolean value);
    public abstract boolean equalNodes(BDDNode other);

    public abstract NewRung makeRung();

    private final static class TrueBDD extends BDDNode {
        @Override
        public void insert(List<Pair<String, Bit>> vars, boolean value) {
        }

        @Override
        public boolean equalNodes(BDDNode other) {
            return other == this;
        }

        @Override
        public NewRung makeRung() {
            return null;
        }

        @Override
        public String toString() {
            return "TRUE";
        }
    }
    private final static class FalseBDD extends BDDNode {
        @Override
        public void insert(List<Pair<String, Bit>> vars, boolean value) {
        }

        @Override
        public boolean equalNodes(BDDNode other) {
            return other == this;
        }

        @Override
        public NewRung makeRung() {
            return null;
        }

        @Override
        public String toString() {
            return "FALSE";
        }
    }
    static FalseBDD FALSE = new FalseBDD();
    static TrueBDD TRUE = new TrueBDD();
}
