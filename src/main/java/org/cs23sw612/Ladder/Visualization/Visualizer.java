package org.cs23sw612.Ladder.Visualization;

import org.cs23sw612.Ladder.Ladder;
import org.jfree.svg.SVGGraphics2D;

import java.util.ArrayList;

public class Visualizer {
    public static SVGGraphics2D layoutSVG(Ladder ladder){
        var svg = new SVGGraphics2D(1920, 1080);

        for (int i = 0; i < ladder.rungs.size(); i++) {
            drawRung(svg, layoutRung(ladder.rungs.get(i), (i+1) * RUNG_HEIGHT));
        }

        return svg;
    }

    private static void drawRung(SVGGraphics2D svg, SVGRung rung) {
        rung.draw(svg);
    }


    public static float GATE_WIDTH = 50f;
    public static float RUNG_HEIGHT = 50f;

    public static SVGRung layoutRung(Ladder.Rung rung, float height){

        var gateList = new ArrayList<SVGRungElement>();
        ArrayList<Ladder.Gate> gates = rung.gates;
        for (int i = 0; i < gates.size(); i++) {
            Ladder.Gate gate = gates.get(i);

            gateList.add(gateToSvg(gate, i * GATE_WIDTH, height));
        }


        return new SVGRung(RUNG_HEIGHT, gateList);
    }

    private static SVGRungElement gateToSvg(Ladder.Gate gate, float x, float y){
        if (gate instanceof Ladder.NOC)
            return new SVGNOC(x, y);
        else if (gate instanceof Ladder.NCC) {
            return new SVGNCC(x, y);
        }
        else throw new RuntimeException("Unexpexted gate type");
    }

}


