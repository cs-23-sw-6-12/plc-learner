package org.cs23sw612.Ladder.BDD;

import net.automatalib.commons.util.Pair;
import org.cs23sw612.Util.Bit;

import java.util.List;

public abstract class BDDNode {

    public BDDNode reduce() {
        return this;
    }

    public abstract boolean value(); // TODO: Trace

    public abstract void insert(List<Pair<String, Bit>> vars, boolean value);
    private final static class TrueBDD extends BDDNode {

        @Override
        public boolean value() {
            return true;
        }

        @Override
        public void insert(List<Pair<String, Bit>> vars, boolean value) {
        }

        @Override
        public String toString() {
            return "TRUE";
        }
    }
    private final static class FalseBDD extends BDDNode {
        @Override
        public boolean value() {
            return false;
        }

        @Override
        public void insert(List<Pair<String, Bit>> vars, boolean value) {
        }

        @Override
        public String toString() {
            return "FALSE";
        }
    }
    static FalseBDD FALSE = new FalseBDD();
    static TrueBDD TRUE = new TrueBDD();
}
