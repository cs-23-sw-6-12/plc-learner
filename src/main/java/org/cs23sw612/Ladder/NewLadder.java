package org.cs23sw612.Ladder;

import net.automatalib.commons.util.Pair;
import org.cs23sw612.Ladder.BDD.BDDNode;
import org.cs23sw612.Ladder.Rungs.NewRung;
import org.cs23sw612.Ladder.Rungs.OutGate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewLadder {

    // In both maps, the key is the output gate, and values are the rungs
    public final HashMap<List<String>, NewRung> gates;
    public final HashMap<String, String> stateUpd;

    public NewLadder(HashMap<OutGate, BDDNode> bdds) {
        var sortedBdds = new HashMap<ArrayList<OutGate>, BDDNode>();
        for (var pair : bdds.entrySet().stream().map(e -> Pair.of(new ArrayList<>(List.of(e.getKey())), e.getValue()))
                .toList())
            sortedBdds.entrySet().stream().filter(b -> b.getValue().equalNodes(pair.getSecond())).findFirst()
                    .ifPresentOrElse(e -> e.getKey().addAll(pair.getFirst()),
                            () -> sortedBdds.put(pair.getFirst(), pair.getSecond()));

        gates = new HashMap<>();
        stateUpd = new HashMap<>();
        for (var entry : sortedBdds.entrySet()) {
            gates.put(entry.getKey().stream().map(OutGate::label).toList(), entry.getValue().makeRung());
            entry.getKey().forEach(o -> o.actualState().ifPresent(l -> stateUpd.put(l, o.label())));
        }

    }

}
