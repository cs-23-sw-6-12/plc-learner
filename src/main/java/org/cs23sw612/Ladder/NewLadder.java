package org.cs23sw612.Ladder;

import net.automatalib.commons.util.Pair;
import org.cs23sw612.Ladder.BDD.BDDNode;
import org.cs23sw612.Ladder.Rungs.NewRung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewLadder {
    public final HashMap<ArrayList<String>, NewRung> gates;

    public NewLadder(HashMap<String, BDDNode> bdds) {
        var sortedBdds = new HashMap<ArrayList<String>, BDDNode>();
        for (var pair : bdds.entrySet().stream().map(e -> Pair.of(new ArrayList<>(List.of(e.getKey())), e.getValue()))
                .toList())
            sortedBdds.entrySet().stream().filter(b -> b.getValue().equalNodes(pair.getSecond())).findFirst()
                    .ifPresentOrElse(e -> e.getKey().addAll(pair.getFirst()),
                            () -> sortedBdds.put(pair.getFirst(), pair.getSecond()));

        gates = new HashMap<>();
        for (var entry : sortedBdds.entrySet())
            gates.put(entry.getKey(), entry.getValue().makeRung());

    }

}
