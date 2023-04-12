package org.cs23sw612.Ladder.Visualization;
import org.cs23sw612.Ladder.Ladder;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;

public class Visualizer2 {
    public static SVGGraphics2D layoutSVG(Ladder ladder){
        var svg = new SVGGraphics2D(1920, 1080, SVGUnits.PX);
        var ladderBuilder = new SVGLadder.Builder(svg);

        for (Ladder.Rung rung : ladder.rungs) {
            SVGRung.Builder rungBuilder = new SVGRung.Builder(svg);

            if (rung instanceof Ladder.ORRung)
                rungBuilder.intoOrRung();
            else
                rungBuilder.withCoil(rung.outputgate.gate);

            for (Ladder.Gate gate: rung.gates) {
                if (gate instanceof Ladder.NOC)
                    rungBuilder.withNOC(gate.gate);
                if (gate instanceof Ladder.NCC)
                    rungBuilder.withNCC(gate.gate);
            }

            ladderBuilder.withRungBuilder(rungBuilder);
        }

        ladderBuilder.build().draw(svg);
        return svg;
    }
}
