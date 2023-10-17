package org.cs23sw612.Ladder;

import org.cs23sw612.Ladder.BDD.BDDNode;
import org.cs23sw612.Ladder.Rungs.Rung;
import org.cs23sw612.Ladder.Rungs.OutGate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Ladder {

    // In both maps, the key is the output gate, and values are the rungs
    public final HashMap<List<String>, Rung> gates;
    public final HashMap<String, String> stateUpd;
    public double horizontalMaxLength;

    public Ladder(HashMap<List<OutGate>, BDDNode> bdds) {
        gates = new HashMap<>();
        stateUpd = new HashMap<>();
        for (var entry : bdds.entrySet()) {
            gates.put(entry.getKey().stream().map(OutGate::label).toList(), entry.getValue().makeRung());
            entry.getKey().forEach(o -> o.actualState().ifPresent(l -> stateUpd.put(l, o.label())));
        }
        horizontalMaxLength = CalculateHorizontalMaxLength();
    }

    private double CalculateHorizontalMaxLength() {
        if (this.horizontalMaxLength == 0.0) {
            horizontalMaxLength = this.gates.values().stream().map(Rung::rungWidth).max(Comparator.comparing(w -> w))
                    .orElse(0);
        }
        return horizontalMaxLength;
    }
}
