package org.cs23sw612;

import org.cs23sw612.Ladder.Ladder;
import org.cs23sw612.Ladder.Visualization.Visualizer;

import java.io.IOException;

public class SmallLad {
    public static void main(String[] args) throws IOException {

        var ladder = Ladder.newSimp();
        ladder.outRungs.forEach(System.out::print);
        ladder.stateRungs.forEach(System.out::print);

        var visualizedSvg = Visualizer.layoutSVG(ladder);
        Visualizer.showSVG(visualizedSvg);
    }
}
