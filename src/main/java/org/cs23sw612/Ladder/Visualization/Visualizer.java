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

            var rung = ladder.rungs.get(i);
            var gateList = new ArrayList<SVGRungElement>();
            var isOrRung = rung instanceof Ladder.ORRung;
            var height = RUNG_HEIGHT * (i + 1);

            ArrayList<Ladder.Gate> gates = rung.gates;
            int j = 0;
            for (; j < gates.size(); j++) {
                Ladder.Gate gate = gates.get(j);

                gateList.add(gateToSvg(gate, (j + 1) * (GATE_WIDTH + SPACING), height));
            }

            var point = new Point2D.Double(0, 0);
            String coilString = null;
            Point2D.Double endpoint = null;
            if (isOrRung) {
                point.x = GATE_WIDTH / 2;
                point.y = RUNG_HEIGHT * i;
                endpoint = new Point2D.Double((GATE_WIDTH + SPACING) * (gateList.size() + 1) + GATE_WIDTH / 2, height - RUNG_HEIGHT);
            }
            else
                coilString = rung.outputgate.gate;

            var coil = new SVGCoil(svg.getWidth() - SVGCoil.WIDTH, height, coilString);
            new SVGRung(height, point, endpoint, coil, gateList).draw(svg);
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

