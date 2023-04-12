package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUnits;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Visualizer {
    public static final double SPACING = 30d;
    public static double GATE_WIDTH = 25d;
    public static double RUNG_HEIGHT = 50d;

    public static SVGGraphics2D layoutSVG(Ladder ladder){
        var svg = new SVGGraphics2D(1920, 1080, SVGUnits.PX);

        for (int i = 0; i < ladder.rungs.size(); i++) {

            var gateList = new ArrayList<SVGRungElement>();
            var isOrRung = ladder.rungs.get(i) instanceof Ladder.ORRung;
            var height = RUNG_HEIGHT * (i + 1);

            ArrayList<Ladder.Gate> gates = ladder.rungs.get(i).gates;
            int j = 0;
            for (; j < gates.size(); j++) {
                Ladder.Gate gate = gates.get(j);

                gateList.add(gateToSvg(gate, (j + 1) * (GATE_WIDTH + SPACING), height));
            }

            var point = new Point2D.Double(0, 0);
            Point2D.Double endpoint = null;
            if (isOrRung) {
                point.x = GATE_WIDTH / 2;
                point.y = RUNG_HEIGHT * i;
                endpoint = new Point2D.Double((GATE_WIDTH + SPACING) * (gateList.size() + 1) + GATE_WIDTH / 2, height - RUNG_HEIGHT);
                new SVGRung(height, point, endpoint, gateList).draw(svg);
            } else {
                new SVGRung(height, point, ladder.rungs.get(i).outputgate.gate, gateList).draw(svg);
            }
        }
        return svg;
    }

    private static SVGRungElement gateToSvg(Ladder.Gate gate, Double x, Double y){
        if (gate instanceof Ladder.NOC)
            return new SVGNOC(x, y, gate.gate);
        else if (gate instanceof Ladder.NCC) {
            return new SVGNCC(x, y, gate.gate);
        }
        else throw new RuntimeException("Unexpexted gate type");
    }

}

