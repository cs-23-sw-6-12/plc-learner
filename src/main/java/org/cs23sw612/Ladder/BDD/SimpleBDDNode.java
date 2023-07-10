package org.cs23sw612.Ladder.BDD;

import net.automatalib.commons.util.Pair;
import org.cs23sw612.Ladder.Rungs.*;
import org.cs23sw612.Util.Bit;

import java.util.List;

public class SimpleBDDNode extends BDDNode {
    // Left is false, right is true
    public BDDNode left, right;
    public String label;

    @Override
    boolean isInnerNode() {
        return left instanceof SimpleBDDNode && right instanceof SimpleBDDNode;
    }
    @Override
    public BDDNode reduce() { // TODO: Variable ordering
        left = left == null ? null : left.reduce();
        right = right == null ? null : right.reduce();

        if (left == right)
            return left;
        else if (isInnerNode()) { // Checking whether the children have identical sub paths
            // System.out.println("Orderable");
            if (((SimpleBDDNode) left).left == ((SimpleBDDNode) right).left) {
                SimpleBDDNode replacer = new SimpleBDDNode();
                replacer.label = ((SimpleBDDNode) left).label;
                SimpleBDDNode copy = new SimpleBDDNode();
                copy.label = this.label;
                copy.left = ((SimpleBDDNode) left).right;
                copy.right = ((SimpleBDDNode) right).right;
                replacer.left = ((SimpleBDDNode) left).left;
                replacer.right = copy;
                return replacer.reduce();
            } else if (((SimpleBDDNode) left).right == ((SimpleBDDNode) right).right) {
                SimpleBDDNode replacer = new SimpleBDDNode();
                replacer.label = ((SimpleBDDNode) left).label;
                SimpleBDDNode copy = new SimpleBDDNode();
                copy.label = this.label;
                copy.left = ((SimpleBDDNode) left).left;
                copy.right = ((SimpleBDDNode) right).left;
                replacer.right = ((SimpleBDDNode) left).right;
                replacer.left = copy;
                return replacer.reduce();
            }
        }

        return this;
    }

    @Override
    public void insert(List<Pair<String, Bit>> vars, boolean value) {
        assert vars.size() > 0;
        var p = vars.get(0);
        if (label != null) {
            assert label.equals(p.getFirst());
        } else
            label = p.getFirst();
        if (vars.size() == 1) {
            // System.out.println("(" + p.getFirst() + ", " + p.getSecond() + ")");
            // System.out.println(p.getSecond().value);
            if (p.getSecond().value)
                right = value ? BDDNode.TRUE : BDDNode.FALSE;
            else
                left = value ? BDDNode.TRUE : BDDNode.FALSE;
        }
        if (p.getSecond().value) {
            if (right == null)
                right = new SimpleBDDNode();
            right.insert(vars.subList(1, vars.size()), value);
        } else {
            if (left == null)
                left = new SimpleBDDNode();
            left.insert(vars.subList(1, vars.size()), value);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SimpleBDDNode)
            return ((SimpleBDDNode) other).left == left && ((SimpleBDDNode) other).right == right;
        return false;
    }

    @Override
    public String toString() {
        return label + String.format("{\nleft: %s,\nright: %s}", left == null ? "NULL" : left.toString(),
                right == null ? "NULL" : right.toString());
    }

    @Override
    public boolean equalNodes(BDDNode other) {
        if (other instanceof SimpleBDDNode o) {
            // System.out.println(label + ": " + label.equals(o.label));
            return label.equals(o.label) && (left == null ? o.left == null : left.equalNodes(o.left))
                    && (right == null ? o.right == null : right.equalNodes(o.right));
        } else {
            System.out.println(label + ": ");
        }
        return false;
    }

    @Override
    public NewRung makeRung() {
        if (left == null) { // If we only have on the right-branch (true branch)
            return new SimpleRung(label, true, right.makeRung());
        } else if (right == null) { // If we only have on the left-branch (false branch)
            return new SimpleRung(label, false, left.makeRung());
        } else {
            return new CompositeRung(label, left.makeRung(), right.makeRung());
        }
    }
}
