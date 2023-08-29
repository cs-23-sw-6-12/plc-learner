package org.cs23sw612.Ladder;

import org.cs23sw612.Ladder.BDD.BDDNode;
import org.cs23sw612.Ladder.Rungs.NewRung;
import org.cs23sw612.Ladder.Rungs.OutGate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewLadder {

    // In both maps, the key is the output gate, and values are the rungs
    public final HashMap<List<String>, NewRung> gates;
    public final HashMap<String, String> stateUpd;
    public double horizontalMaxLength;

    public NewLadder(HashMap<List<OutGate>, BDDNode> bdds) {
        gates = new HashMap<>();
        stateUpd = new HashMap<>();
        for (var entry : bdds.entrySet()) {
            gates.put(entry.getKey().stream().map(OutGate::label).toList(), entry.getValue().makeRung());
            entry.getKey().forEach(o -> o.actualState().ifPresent(l -> stateUpd.put(l, o.label())));
        }
        horizontalMaxLength = CalculateHorizontalMaxLength();
    }

    private double CalculateHorizontalMaxLength(){
        var horizontalMaxLength = 0;
        for (Map.Entry<List<String>, NewRung> listNewRungEntry : this.gates.entrySet()) {
            NewRung rung = listNewRungEntry.getValue();
            if(horizontalMaxLength < rung.horizontalLength())
                horizontalMaxLength = rung.horizontalLength();
        }
        return horizontalMaxLength;
    }

}
